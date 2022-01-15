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

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

abstract class JsonParserTest<P extends JsonParser> {

    private static final double EPSILON = 1e-23;

    static final JsonParserConfig CONFIG = JsonParserConfig.newBuilder()
        .setBufferSize(8)
        .setMinNestingCapacity(2)
        .setMaxNestingCapacity(4)
        .setMinFieldNameCapacity(4)
        .setMaxFieldNameCapacity(8)
        .setMinStringCapacity(8)
        .setMaxStringCapacity(16)
        .build();

    P parser;

    abstract void reset(String input) throws IOException;

    // JsonToken.VALUE_NULL

    @Test
    void valueNull() throws Exception {
        assertTokenEquals(JsonToken.VALUE_NULL, parse("null", 1));
    }

    @Test
    void unexpectedAsciiCharWithinValueNull() throws Exception {
        assertParseError("Expected 'l' but got 'x'", () -> parse("nulx", 1));
    }

    @Test
    void unexpectedAsciiCharAfterValueNull() throws Exception {
        assertParseError("Unexpected 'x'", () -> parse("nullx", 2));
    }

    @Test
    void unexpectedEofWithinValueNull() throws Exception {
        assertParseError("Unexpected end of input", () -> parse("nul", 1));
    }

    // JsonToken.VALUE_TRUE

    @Test
    void valueTrue() throws Exception {
        assertValueEquals(true, JsonToken.VALUE_TRUE, parse("true", 1));
    }

    @Test
    void unexpectedAsciiCharWithinValueTrue() throws Exception {
        assertParseError("Expected 'e' but got 'x'", () -> parse("trux", 1));
    }

    @Test
    void unexpectedAsciiCharAfterValueTrue() throws Exception {
        assertParseError("Unexpected 'x'", () -> parse("truex", 2));
    }

    @Test
    void unexpectedEofWithinValueTrue() throws Exception {
        assertParseError("Unexpected end of input", () -> parse("tru", 1));
    }

    // JsonToken.VALUE_FALSE

    @Test
    void valueFalse() throws Exception {
        assertValueEquals(false, JsonToken.VALUE_FALSE, parse("false", 1));
    }

    @Test
    void unexpectedAsciiCharWithinValueFalse() throws Exception {
        assertParseError("Expected 'e' but got 'x'", () -> parse("falsx", 1));
    }

    @Test
    void unexpectedAsciiCharAfterValueFalse() throws Exception {
        assertParseError("Unexpected 'x'", () -> parse("falsex", 2));
    }

    @Test
    void unexpectedEofWithinValueFalse() throws Exception {
        assertParseError("Unexpected end of input", () -> parse("fals", 1));
    }

    // JsonToken.VALUE_NUMBER_INT

    @Test
    void valueNumberIntZero() throws Exception {
        assertValueEquals(0L, "0", parse("0", 1));
    }

    @Test
    void valueNumberIntOne() throws Exception {
        assertValueEquals(1L, "1", parse("1", 1));
    }

    @Test
    void valueNumberIntMaxValue() throws Exception {
        assertValueEquals(9007199254740991L, "9007199254740991",
                parse("9007199254740991", 1));
    }

    @Test
    void valueNumberIntMinusZero() throws Exception {
        assertValueEquals(0L, "-0", parse("-0", 1));
    }

    @Test
    void valueNumberIntMinusOne() throws Exception {
        assertValueEquals(-1L, "-1", parse("-1", 1));
    }

    @Test
    void valueNumberIntMinValue() throws Exception {
        assertValueEquals(-9007199254740991L, "-9007199254740991",
                parse("-9007199254740991", 1));
    }

    @Test
    void tooLargeValueNumberInt() throws Exception {
        assertParseError("Too high precision in numeric value", () -> parse("9007199254740992", 1));
    }

    @Test
    void tooSmallValueNumberInt() throws Exception {
        assertParseError("Too high precision in numeric value", () -> parse("-9007199254740992", 1));
    }

    @Test
    void leadingZeroInValueNumberIntZero() throws Exception {
        assertParseError("Leading zero in numeric value", () -> parse("00", 1));
    }

    @Test
    void leadingZeroInValueNumberIntOne() throws Exception {
        assertParseError("Leading zero in numeric value", () -> parse("01", 1));
    }

    // JsonToken.VALUE_NUMBER_FLOAT

    @Test
    void valueNumberFloatZero() throws Exception {
        assertValueEquals(0.0, "0.0", parse("0.0", 1));
    }

    @Test
    void valueNumberFloatEpsilon() throws Exception {
        assertValueEquals(0.000000000000001, "0.000000000000001",
                parse("0.000000000000001", 1));
    }

    @Test
    void valueNumberFloatZeroPointOne() throws Exception {
        assertValueEquals(0.1, "0.1", parse("0.1", 1));
    }

    @Test
    void valueNumberFloatOne() throws Exception {
        assertValueEquals(1.0, "1.0", parse("1.0", 1));
    }

    @Test
    void valueNumberFloatMaxValue() throws Exception {
        assertValueEquals(90071992547409910000000000000000000000.0,
                "90071992547409910000000000000000000000.0",
                parse("90071992547409910000000000000000000000.0", 1));
    }

    @Test
    void valueNumberFloatMinusZero() throws Exception {
        assertValueEquals(0.0, "-0.0", parse("-0.0", 1));
    }

    @Test
    void valueNumberFloatMinusEpsilon() throws Exception {
        assertValueEquals(-0.000000000000001, "-0.000000000000001",
                parse("-0.000000000000001", 1));
    }

    @Test
    void valueNumberFloatMinusZeroPointOne() throws Exception {
        assertValueEquals(-0.1, "-0.1", parse("-0.1", 1));
    }

    @Test
    void valueNumberFloatMinusOne() throws Exception {
        assertValueEquals(-1.0, "-1.0", parse("-1.0", 1));
    }

    @Test
    void valueNumberFloatMinValue() throws Exception {
        assertValueEquals(-90071992547409910000000000000000000000.0,
                "-90071992547409910000000000000000000000.0",
                parse("-90071992547409910000000000000000000000.0", 1));
    }

    @Test
    void tooLowMagnitudeInValueNumberFloat() throws Exception {
        assertParseError("Too high precision in numeric value",
                () -> parse("0.0000000000000001", 1));
    }

    @Test
    void tooLowMagnitudeInNegativeValueNumberFloat() throws Exception {
        assertParseError("Too high precision in numeric value",
                () -> parse("-0.0000000000000001", 1));
    }

    @Test
    void tooHighPrecisionInValueNumberFloat() throws Exception {
        assertParseError("Too high precision in numeric value",
                () -> parse("10000000000000001000000000000000000000.0", 1));
    }

    @Test
    void tooHighPrecisionInNegativeValueNumberFloat() throws Exception {
        assertParseError("Too high precision in numeric value",
                () -> parse("-10000000000000001000000000000000000000.0", 1));
    }

    @Test
    void tooHighMagnitudeInValueNumberFloat() throws Exception {
        assertParseError("Too high magnitude in numeric value",
                () -> parse("100000000000000000000000000000000000000.0"));
    }

    @Test
    void tooHighMagnitudeInNegativeValueNumberFloat() throws Exception {
        assertParseError("Too high magnitude in numeric value",
                () -> parse("-100000000000000000000000000000000000000.0"));
    }

    @Test
    void leadingZeroInValueNumberFloatZero() throws Exception {
        assertParseError("Leading zero in numeric value", () -> parse("00.0"));
    }

    @Test
    void leadingZeroInValueNumberFloatOne() throws Exception {
        assertParseError("Leading zero in numeric value", () -> parse("01.0"));
    }

    @Test
    void unexpectedEofWithinValueNumberFloat() throws Exception {
        assertParseError("Unexpected end of input", () -> parse("1."));
    }

    // JsonToken.VALUE_STRING

    @Test
    void valueString() throws Exception {
        assertTextEquals("foo", JsonToken.VALUE_STRING, parse("\"foo\"", 1));
    }

    @Test
    void valueStringMinLength() throws Exception {
        assertTextEquals("", JsonToken.VALUE_STRING, parse("\"\"", 1));
    }

    @Test
    void valueStringMaxLength() throws Exception {
        assertTextEquals("aaaaaaaaaaaaaaaa", JsonToken.VALUE_STRING,
                parse("\"aaaaaaaaaaaaaaaa\"", 1));
    }

    @Test
    void tooLongValueString() throws Exception {
        assertParseError("Too long string value", () -> parse("\"aaaaaaaaaaaaaaaaa\"", 1));
    }

    @Test
    void unexpectedEofWithinValueString() throws Exception {
        assertParseError("Unexpected end of input", () -> parse("\"fo"));
    }

    // JsonToken.FIELD_NAME

    @Test
    void fieldName() throws Exception {
        assertTextEquals("foo", JsonToken.FIELD_NAME, parse("{\"foo\":", 2));
    }

    @Test
    void fieldNameMinLength() throws Exception {
        assertTextEquals("", JsonToken.FIELD_NAME, parse("{\"\":", 2));
    }

    @Test
    void fieldNameMaxLength() throws Exception {
        assertTextEquals("aaaaaaaa", JsonToken.FIELD_NAME, parse("{\"aaaaaaaa\":", 2));
    }

    @Test
    void tooLongFieldName() throws Exception {
        assertParseError("Too long field name", () -> parse("{\"aaaaaaaaa\":null}"));
    }

    @Test
    void unexpectedEofBeforeDoubleQuoteWithinFieldName() throws Exception {
        assertParseError("Unexpected end of input", () -> parse("{\"fo"));
    }

    @Test
    void unexpectedEofAfterDoubleQuoteWithinFieldName() throws Exception {
        assertParseError("Unexpected end of input", () -> parse("{\"foo\""));
    }

    // JsonToken.START_ARRAY

    @Test
    void startArray() throws Exception {
        assertTokenEquals(JsonToken.START_ARRAY, parse("[", 1));
    }

    // JsonToken.END_ARRAY

    @Test
    void endArray() throws Exception {
        assertTokenEquals(JsonToken.END_ARRAY, parse("[]", 2));
    }

    // JsonToken.START_OBJECT

    @Test
    void startObject() throws Exception {
        assertTokenEquals(JsonToken.START_OBJECT, parse("{", 1));
    }

    // JsonToken.END_OBJECT

    @Test
    void endObject() throws Exception {
        assertTokenEquals(JsonToken.END_OBJECT, parse("{}", 2));
    }

    // Arrays

    @Test
    void array() throws Exception {
        assertEquals("[ ]", parse("[]"));
    }

    @Test
    void arrayWithValueNull() throws Exception {
        assertEquals("[ null ]", parse("[null]"));
    }

    @Test
    void arrayWithValueFalseValueTrue() throws Exception {
        assertEquals("[ false true ]", parse("[false,true]"));
    }

    @Test
    void arrayWithValueNumberInt() throws Exception {
        assertEquals("[ 123 ]", parse("[123]"));
    }

    @Test
    void arrayWithValueNumberFloat() throws Exception {
        assertEquals("[ 12.3 ]", parse("[12.3]"));
    }

    @Test
    void arrayWithValueString() throws Exception {
        assertEquals("[ \"foo\" ]", parse("[\"foo\"]"));
    }

    @Test
    void arrayWithArray() throws Exception {
        assertEquals("[ [ ] ]", parse("[[]]"));
    }

    @Test
    void arrayWithObject() throws Exception {
        assertEquals("[ { } ]", parse("[{}]"));
    }

    @Test
    void unexpectedEofWithinArray() throws Exception {
        assertParseError("Unexpected end of input", () -> parse("["));
    }

    @Test
    void unexpectedEofBeforeCommaWithinArray() throws Exception {
        assertParseError("Unexpected end of input", () -> parse("[null"));
    }

    @Test
    void unexpectedEofAfterCommaWithinArray() throws Exception {
        assertParseError("Unexpected end of input", () -> parse("[null,"));
    }

    @Test
    void unexpectedEndArrayAfterComma() throws Exception {
        assertParseError("Unexpected ']'", () -> parse("[null,]"));
    }

    @Test
    void missingCommaWithinArray() throws Exception {
        assertParseError("Expected ',' but got 't'", () -> parse("[false true]"));
    }

    @Test
    void unexpectedEndObjectWithinArray() throws Exception {
        assertParseError("Unexpected '}'", () -> parse("[}"));
    }

    // Objects

    @Test
    void object() throws Exception {
        assertEquals("{ }", parse("{}"));
    }

    @Test
    void objectWithValueNull() throws Exception {
        assertEquals("{ \"foo\": null }", parse("{\"foo\":null}"));
    }

    @Test
    void objectWithValueFalseValueTrue() throws Exception {
        assertEquals("{ \"foo\": false \"bar\": true }",
                parse("{\"foo\":false,\"bar\":true}"));
    }

    @Test
    void objectWithValueNumberInt() throws Exception {
        assertEquals("{ \"foo\": 123 }", parse("{\"foo\":123}"));
    }

    @Test
    void objectWithValueString() throws Exception {
        assertEquals("{ \"foo\": \"bar\" }", parse("{\"foo\":\"bar\"}"));
    }

    @Test
    void objectWithArray() throws Exception {
        assertEquals("{ \"foo\": [ ] }", parse("{\"foo\":[]}"));
    }

    @Test
    void objectWithObject() throws Exception {
        assertEquals("{ \"foo\": { } }", parse("{\"foo\":{}}"));
    }

    @Test
    void unexpectedValueNullAfterStartObject() throws Exception {
        assertParseError("Unexpected 'n'", () -> parse("{null"));
    }

    @Test
    void unexpectedValueTrueAfterStartObject() throws Exception {
        assertParseError("Unexpected 't'", () -> parse("{true"));
    }

    @Test
    void unexpectedValueFalseAfterStartObject() throws Exception {
        assertParseError("Unexpected 'f'", () -> parse("{false"));
    }

    @Test
    void unexpectedValueNumberIntAfterStartObject() throws Exception {
        assertParseError("Unexpected '1'", () -> parse("{1"));
    }

    @Test
    void unexpectedValueNumberFloatAfterStartObject() throws Exception {
        assertParseError("Unexpected '1'", () -> parse("{1.0"));
    }

    @Test
    void unexpectedStartArrayAfterStartObject() throws Exception {
        assertParseError("Unexpected '['", () -> parse("{["));
    }

    @Test
    void unexpectedStartObjectAfterStartObject() throws Exception {
        assertParseError("Unexpected '{'", () -> parse("{{"));
    }

    @Test
    void unexpectedEofWithinObject() throws Exception {
        assertParseError("Unexpected end of input", () -> parse("{"));
    }

    @Test
    void unexpectedEofAfterFieldNameWithinObject() throws Exception {
        assertParseError("Unexpected end of input", () -> parse("{\"foo\":"));
    }

    @Test
    void unexpectedEofBeforeCommaWithinObject() throws Exception {
        assertParseError("Unexpected end of input", () -> parse("{\"foo\":null"));
    }

    @Test
    void unexpectedEofAfterCommaWithinObject() throws Exception {
        assertParseError("Unexpected end of input", () -> parse("{\"foo\":null,"));
    }

    @Test
    void unexpectedEndObjectBeforeColon() throws Exception {
        assertParseError("Expected ':' but got '}'", () -> parse("{\"foo\"}"));
    }

    @Test
    void unexpectedEndObjectAfterColon() throws Exception {
        assertParseError("Unexpected '}'", () -> parse("{\"foo\":}"));
    }

    @Test
    void unexpectedEndObjectAfterComma() throws Exception {
        assertParseError("Unexpected '}'", () -> parse("{\"foo\":null,}"));
    }

    @Test
    void missingCommaWithinObject() throws Exception {
        assertParseError("Expected ',' but got '\"'", () -> parse("{\"foo\":false \"bar\":true}"));
    }

    @Test
    void unexpectedEndArrayWithinObject() throws Exception {
        assertParseError("Unexpected ']'", () -> parse("{]"));
    }

    // Whitespace

    @Test
    void sp() throws Exception {
        assertEquals("[ null ]", parse(" [ null ] "));
    }

    @Test
    void lf() throws Exception {
        assertEquals("[ null ]", parse("\n[\nnull\n]\n"));
    }

    @Test
    void cr() throws Exception {
        assertEquals("[ null ]", parse("\r[\rnull\r]\r"));
    }

    @Test
    void ht() throws Exception {
        assertEquals("[ null ]", parse("\t[\tnull\t]\t"));
    }

    @Test
    void spAfterValueNumberIntWithinArray() throws Exception {
        assertEquals("[ 0 1 ]", parse("[0 , 1]"));
    }

    // Current name

    @Test
    void currentNameForFirstFieldNameWithinObject() throws Exception {
        assertTrue("foo".contentEquals(parse("{\"foo\":", 2).currentName()));
    }

    @Test
    void currentNameForFirstValueWithinObject() throws Exception {
        assertTrue("foo".contentEquals(parse("{\"foo\":false", 3).currentName()));
    }

    @Test
    void currentNameForSecondFieldNameWithinObject() throws Exception {
        assertTrue("bar".contentEquals(parse("{\"foo\":false,\"bar\":", 4).currentName()));
    }

    @Test
    void currentNameForSecondValueWithinObject() throws Exception {
        assertTrue("bar".contentEquals(parse("{\"foo\":false,\"bar\":true", 5).currentName()));
    }

    @Test
    void currentNameForEndObjectWithinObject() throws Exception {
        assertNull(parse("{\"foo\":false,\"bar\":true}", 6).currentName());
    }

    @Test
    void currentNameWithinArray() throws Exception {
        assertNull(parse("[null", 2).currentName());
    }

    // State

    @Test
    void beforeFirstToken() throws Exception {
        assertNull(parser.currentToken());
    }

    @Test
    void afterLastToken() throws Exception {
        parse("null", 1);

        assertNull(parser.nextToken());
        assertNull(parser.currentToken());

        assertNull(parser.nextToken());
        assertNull(parser.currentToken());
    }

    @Test
    void trailingToken() throws Exception {
        assertParseError("Unexpected 't'", () -> parse("false true"));
    }

    @Test
    void maxNestingCapacity() throws Exception {
        parse("[[[[]]]]");
    }

    @Test
    void exceedMaxNestingCapacity() throws Exception {
        assertParseError("Maximum nesting capacity exceeded", () -> parse("[[[[["));
    }

    private JsonParser parse(final String input, final int numTokens) throws IOException {
        reset(input);

        for (int c = 0; c < numTokens; c++)
            parser.nextToken();

        return parser;
    }

    private String parse(final String input) throws IOException {
        reset(input);

        List<String> tokens = new ArrayList<>();

        JsonToken currentToken;

        while ((currentToken = parser.nextToken()) != null) {
            switch (currentToken) {
            case FIELD_NAME:
                tokens.add(String.format("\"%s\":", parser.getText()));
                break;
            case VALUE_STRING:
                tokens.add(String.format("\"%s\"", parser.getText()));
                break;
            default:
                tokens.add(parser.getText().toString());
                break;
            }
        }

        return String.join(" ", tokens);
    }

    private static void assertTokenEquals(final JsonToken token,
            final JsonParser parser) throws JsonParseException {
        assertEquals(token, parser.currentToken());
        assertParseError("Not a boolean value", () -> parser.getBooleanValue());
        assertParseError("Not a long value", () -> parser.getLongValue());
        assertParseError("Not a double value", () -> parser.getDoubleValue());
        assertTrue(token.asString().contentEquals(parser.getText()));
    }

    private static void assertTextEquals(final String text,
            final JsonToken token, final JsonParser parser) throws JsonParseException {
        assertEquals(token, parser.currentToken());
        assertParseError("Not a boolean value", () -> parser.getBooleanValue());
        assertParseError("Not a long value", () -> parser.getLongValue());
        assertParseError("Not a double value", () -> parser.getDoubleValue());
        assertTrue(text.contentEquals(parser.getText()));
    }

    private static void assertValueEquals(final boolean booleanValue,
            final JsonToken token, final JsonParser parser) throws JsonParseException {
        assertEquals(token, parser.currentToken());
        assertEquals(booleanValue, parser.getBooleanValue());
        assertParseError("Not a long value", () -> parser.getLongValue());
        assertParseError("Not a double value", () -> parser.getDoubleValue());
        assertTrue(token.asString().contentEquals(parser.getText()));
    }

    private static void assertValueEquals(final long longValue, final String text,
            final JsonParser parser) throws JsonParseException {
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.currentToken());
        assertParseError("Not a boolean value", () -> parser.getBooleanValue());
        assertEquals(longValue, parser.getLongValue());
        assertEquals((double)longValue, parser.getDoubleValue(), EPSILON);
        assertTrue(text.contentEquals(parser.getText()));
    }

    private static void assertValueEquals(final double doubleValue, final String text,
            final JsonParser parser) throws JsonParseException {
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.currentToken());
        assertParseError("Not a boolean value", () -> parser.getBooleanValue());
        assertParseError("Not a long value", () -> parser.getLongValue());
        assertEquals(doubleValue, parser.getDoubleValue(), EPSILON);
        assertTrue(text.contentEquals(parser.getText()));
    }

    private static void assertParseError(final String message, final Executable executable) {
        Exception exception = assertThrows(JsonParseException.class, executable);

        assertEquals(message, exception.getMessage());
    }

}
