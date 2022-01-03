/*
 * Copyright 2022 Jargon authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fasterjson.jargon.core;

import java.io.IOException;
import java.util.Arrays;
import org.fasterjson.jargon.core.io.JsonEOFException;

abstract class AbstractJsonParser implements JsonParser {

    private static final double[] POWERS_OF_TEN = {
        1e-22,
        1e-21,
        1e-20,
        1e-19,
        1e-18,
        1e-17,
        1e-16,
        1e-15,
        1e-14,
        1e-13,
        1e-12,
        1e-11,
        1e-10,
        1e-9,
        1e-8,
        1e-7,
        1e-6,
        1e-5,
        1e-4,
        1e-3,
        1e-2,
        1e-1,
        1e0,
        1e+1,
        1e+2,
        1e+3,
        1e+4,
        1e+5,
        1e+6,
        1e+7,
        1e+8,
        1e+9,
        1e+10,
        1e+11,
        1e+12,
        1e+13,
        1e+14,
        1e+15,
        1e+16,
        1e+17,
        1e+18,
        1e+19,
        1e+20,
        1e+21,
        1e+22,
    };

    private static final int MAX_DIGITS = 38;

    private static final int MAX_MANTISSA_DIGITS = 16;

    private static final long MAX_MANTISSA = 9007199254740991L;

    private static final int EXPONENT_OFFSET = 22;

    private static final int MAX_EXPONENT = 22;

    private static final int HT = 0x09;
    private static final int LF = 0x0a;
    private static final int CR = 0x0d;
    private static final int SP = 0x20;

    private enum StructType {
        ARRAY,
        OBJECT,
    }

    private final int maxNestingCapacity;
    private final int maxFieldNameCapacity;
    private final int maxStringCapacity;

    private StructType[] structTypes;

    private int depth;

    private StructType currentStructType;

    private int lastCh;

    private JsonToken previousToken;
    private JsonToken currentToken;

    private StringBuilder fieldName;
    private StringBuilder text;

    private long mantissa;
    private int exponent;

    AbstractJsonParser(final JsonParserConfig config) {
        maxNestingCapacity = config.getMaxNestingCapacity();
        maxFieldNameCapacity = config.getMaxFieldNameCapacity();
        maxStringCapacity = config.getMaxStringCapacity();

        structTypes = new StructType[config.getMinNestingCapacity()];

        depth = 0;

        currentStructType = null;

        lastCh = -1;

        previousToken = null;
        currentToken = null;

        fieldName = new StringBuilder(config.getMinFieldNameCapacity());
        text = new StringBuilder(config.getMinStringCapacity());
    }

    void reset() {
        depth = 0;

        currentStructType = null;

        lastCh = -1;

        previousToken = null;
        currentToken = null;
    }

    @Override
    public CharSequence currentName() {
        if (previousToken == JsonToken.FIELD_NAME)
            return fieldName;

        if (currentToken == JsonToken.FIELD_NAME)
            return fieldName;

        return null;
    }

    @Override
    public JsonToken currentToken() {
        return currentToken;
    }

    @Override
    public boolean getBooleanValue() throws JsonParseException {
        if (currentToken == null || !currentToken.isBoolean())
            notBooleanValue();

        return currentToken == JsonToken.VALUE_TRUE;
    }

    @Override
    public double getDoubleValue() throws JsonParseException {
        if (currentToken == JsonToken.VALUE_NUMBER_FLOAT)
            return mantissa * POWERS_OF_TEN[EXPONENT_OFFSET + exponent];

        if (currentToken != JsonToken.VALUE_NUMBER_INT)
            notDoubleValue();

        return mantissa;
    }

    @Override
    public long getLongValue() throws JsonParseException {
        if (currentToken != JsonToken.VALUE_NUMBER_INT)
            notLongValue();

        return mantissa;
    }

    @Override
    public CharSequence getText() {
        if (currentToken == JsonToken.VALUE_STRING)
            return text;

        if (currentToken == JsonToken.FIELD_NAME)
            return fieldName;

        if (currentToken == null)
            return null;

        if (currentToken.isNumeric())
            return text;

        return currentToken.asString();
    }

    @Override
    public JsonToken nextToken() throws IOException {
        previousToken = currentToken;

        int ch;

        if (lastCh != -1) {
            ch = lastCh;

            lastCh = -1;
        }
        else {
            ch = nextNonWhitespaceAsciiChar();
            if (ch == -1) {
                handleEof();

                return null;
            }
        }

        if (depth == 0 && previousToken != null)
            unexpectedAsciiChar(ch);

        if (currentStructType == StructType.OBJECT) {
            if (currentToken == JsonToken.FIELD_NAME) {
                return currentToken = parseValue(ch);
            }
            else {
                if (ch == '}') {
                    handleEndStruct(StructType.OBJECT);

                    return currentToken = JsonToken.END_OBJECT;
                }

                if (currentToken != JsonToken.START_OBJECT) {
                    if (ch != ',')
                        unexpectedAsciiChar(ch, ',');

                    ch = nextNonWhitespaceAsciiChar();
                }

                if (ch != '"')
                    unexpectedAsciiChar(ch);

                parseText(fieldName, maxFieldNameCapacity, "field name");

                ch = nextNonWhitespaceAsciiChar();
                if (ch != ':')
                    unexpectedAsciiChar(ch, ':');

                return currentToken = JsonToken.FIELD_NAME;
            }
        }
        else if (currentStructType == StructType.ARRAY) {
            if (ch == ']') {
                handleEndStruct(StructType.ARRAY);

                return currentToken = JsonToken.END_ARRAY;
            }

            if (currentToken != JsonToken.START_ARRAY) {
                if (ch != ',')
                    unexpectedAsciiChar(ch, ',');

                ch = nextNonWhitespaceAsciiChar();
            }

            return currentToken = parseValue(ch);
        }
        else {
            return currentToken = parseValue(ch);
        }
    }

    private JsonToken parseValue(final int ch) throws IOException {
        switch (ch) {
        case '{':
            handleStartStruct(StructType.OBJECT);

            return currentToken = JsonToken.START_OBJECT;
        case '[':
            handleStartStruct(StructType.ARRAY);

            return currentToken = JsonToken.START_ARRAY;
        case '"':
            parseText(text, maxStringCapacity, "string value");

            return JsonToken.VALUE_STRING;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            return currentToken = parseNumber(+1, ch);
        case '-': {
            int firstCh = nextAsciiChar();
            if (!isDigit(firstCh))
                unexpectedAsciiChar(firstCh);

            return currentToken = parseNumber(-1, firstCh);
        }
        case 't':
            parseTrue();

            return JsonToken.VALUE_TRUE;
        case 'f':
            parseFalse();

            return JsonToken.VALUE_FALSE;
        case 'n':
            parseNull();

            return JsonToken.VALUE_NULL;
        default:
            unexpectedAsciiChar(ch);

            return null;
        }
    }

    abstract int nextAsciiChar() throws IOException;

    private int nextNonWhitespaceAsciiChar() throws IOException {
        while (true) {
            int ch = nextAsciiChar();
            if (ch == SP || ch == LF || ch == CR || ch == HT)
                continue;

            return ch;
        }
    }

    private void handleStartStruct(final StructType structType) throws JsonParseException {
        if (depth == structTypes.length)
            increaseNestingCapacity();

        structTypes[depth++] = structType;

        currentStructType = structType;
    }

    private void handleEndStruct(final StructType structType) throws JsonParseException {
        if (currentStructType != structType)
            unexpectedEndStruct(structType);

        depth--;

        currentStructType = depth == 0 ? null : structTypes[depth - 1];
    }

    private void parseFalse() throws IOException {
        matchAsciiChar('a');
        matchAsciiChar('l');
        matchAsciiChar('s');
        matchAsciiChar('e');
    }

    private void parseNull() throws IOException {
        matchAsciiChar('u');
        matchAsciiChar('l');
        matchAsciiChar('l');
    }

    private void parseTrue() throws IOException {
        matchAsciiChar('r');
        matchAsciiChar('u');
        matchAsciiChar('e');
    }

    private JsonToken parseNumber(final int signum, final int firstCh) throws IOException {
        text.setLength(0);

        if (signum < 0)
            text.append('-');

        text.append((char)firstCh);

        mantissa = firstCh - '0';
        exponent = 0;

        int digits = 1;

        int mantissaDigits = 1;

        JsonToken token = JsonToken.VALUE_NUMBER_INT;

        int ch = nextAsciiChar();

        if (mantissa == 0 && isDigit(ch))
            leadingZero();

        while (mantissaDigits < MAX_MANTISSA_DIGITS) {
            if (!isDigit(ch))
                break;

            text.append((char)ch);

            mantissa *= 10;
            mantissa += ch - '0';

            digits++;

            mantissaDigits++;

            ch = nextAsciiChar();
        }

        if (ch == '0') {
            token = JsonToken.VALUE_NUMBER_FLOAT;

            text.append((char)ch);

            digits++;

            exponent++;

            while (exponent <= MAX_EXPONENT) {
                ch = nextAsciiChar();
                if (ch != '0')
                    break;

                text.append((char)ch);

                digits++;

                exponent++;
            }

            if (ch == '0')
                tooHighMagnitude();
        }

        if (isDigit(ch))
            tooHighPrecision();

        if (ch == '.') {
            token = JsonToken.VALUE_NUMBER_FLOAT;

            text.append('.');

            ch = nextAsciiChar();
            if (!isDigit(ch))
                unexpectedAsciiChar(ch);

            while (mantissaDigits < MAX_MANTISSA_DIGITS) {
                text.append((char)ch);

                mantissa *= 10;
                mantissa += ch - '0';

                digits++;

                mantissaDigits++;

                exponent--;

                ch = nextAsciiChar();
                if (!isDigit(ch))
                    break;
            }

            while (digits <= MAX_DIGITS) {
                if (!isDigit(ch))
                    break;

                if (ch != '0')
                    tooHighPrecision();

                text.append((char)ch);

                digits++;

                ch = nextAsciiChar();
            }
        }

        if (mantissa > MAX_MANTISSA)
            tooHighPrecision();

        lastCh = ch;

        if (signum < 0)
            mantissa = -mantissa;

        return token;
    }

    private void parseText(final StringBuilder text, final int maxTextCapacity,
            final String textType) throws IOException {
        text.setLength(0);

        int count = 0;

        int ch;

        while ((ch = nextAsciiChar()) != -1) {
            if (ch == '"')
                return;

            count++;
            if (count > maxTextCapacity)
                tooLongText(textType);

            text.append((char)ch);
        }

        if (ch == -1)
            unexpectedEof();
    }

    void handleEof() throws JsonParseException {
        if (currentStructType != null)
            unexpectedEof();

        currentToken = null;
    }

    private void matchAsciiChar(final char expectedCh) throws IOException {
        int actualCh = nextAsciiChar();

        if (actualCh != expectedCh)
            unexpectedAsciiChar(actualCh, expectedCh);
    }

    private void increaseNestingCapacity() throws JsonParseException {
        int currentNestingCapacity = structTypes.length;
        int newNestingCapacity = Math.min(2 * currentNestingCapacity, maxNestingCapacity);

        if (newNestingCapacity == currentNestingCapacity)
            parseError("Maximum nesting capacity exceeded");

        structTypes = Arrays.copyOf(structTypes, newNestingCapacity);
    }

    private static boolean isDigit(final int ch) {
        return '0' <= ch && ch <= '9';
    }

    private static void notBooleanValue() throws JsonParseException {
        parseError("Not a boolean value");
    }

    private static void notLongValue() throws JsonParseException {
        parseError("Not a long value");
    }

    private static void notDoubleValue() throws JsonParseException {
        parseError("Not a double value");
    }

    private static void expectedAsciiChar(final char ch) throws JsonParseException {
        parseError("Expected '" + ch + "'");
    }

    private static void unexpectedEndStruct(final StructType structType) throws JsonParseException {
        unexpectedAsciiChar(structType == StructType.ARRAY ? ']' : '}');
    }

    private static void unexpectedAsciiChar(final int ch) throws JsonParseException {
        if (ch == -1)
            unexpectedEof();
        else
            parseError("Unexpected '" + (char)ch + "'");
    }

    private static void unexpectedAsciiChar(final int actualCh, final char expectedCh) throws JsonParseException {
        if (actualCh == -1)
            unexpectedEof();
        else
            parseError("Expected '" + expectedCh + "' but got '" + (char)actualCh + "'");
    }

    private static void leadingZero() throws JsonParseException {
        parseError("Leading zero in numeric value");
    }

    private static void tooHighPrecision() throws JsonParseException {
        parseError("Too high precision in numeric value");
    }

    private static void tooHighMagnitude() throws JsonParseException {
        parseError("Too high magnitude in numeric value");
    }

    private static void tooLongText(final String textType) throws JsonParseException {
        parseError("Too long " + textType);
    }

    private static void unexpectedEof() throws JsonParseException {
        throw new JsonEOFException("Unexpected end of input");
    }

    private static void parseError(final String message) throws JsonParseException {
        throw new JsonParseException(message);
    }

}
