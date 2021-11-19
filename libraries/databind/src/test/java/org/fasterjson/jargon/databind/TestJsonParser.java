/*
 * Copyright 2021 Jargon authors
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
package org.fasterjson.jargon.databind;

import java.util.ArrayList;
import java.util.List;
import org.fasterjson.jargon.core.JsonParseException;
import org.fasterjson.jargon.core.JsonParser;
import org.fasterjson.jargon.core.JsonToken;

class TestJsonParser implements JsonParser {

    private final List<State> states;

    private int index;

    private State currentState;

    TestJsonParser() {
        states = new ArrayList<>();

        index = -1;

        currentState = null;
    }

    void push(final JsonToken token) {
        push(token, null, null, null);
    }

    void push(final JsonToken token, final double doubleValue) {
        push(token, doubleValue, null, null);
    }

    void push(final JsonToken token, final long longValue) {
        push(token, null, longValue, null);
    }

    void push(final JsonToken token, final String text) {
        push(token, null, null, text);
    }

    private void push(final JsonToken token, final Double doubleValue, final Long longValue, final String text) {
        states.add(new State(token, doubleValue, longValue, text));
    }

    @Override
    public CharSequence currentName() {
        if (index < 1)
            return null;

        State previousState = states.get(index - 1);
        if (previousState.token != JsonToken.FIELD_NAME)
            return null;

        return previousState.text;
    }

    @Override
    public JsonToken currentToken() {
        if (currentState == null)
            return null;

        return currentState.token;
    }

    @Override
    public boolean getBooleanValue() throws JsonParseException {
        if (currentState == null)
            noCurrentToken();

        JsonToken token = currentState.token;

        if (!token.isBoolean())
            notBooleanValue();

        return token == JsonToken.VALUE_TRUE;
    }

    @Override
    public double getDoubleValue() throws JsonParseException {
        if (currentState == null)
            noCurrentToken();

        if (!currentState.token.isNumeric())
            notNumericValue();

        if (currentState.doubleValue != null)
            return currentState.doubleValue;

        return currentState.longValue;
    }

    @Override
    public long getLongValue() throws JsonParseException {
        if (currentState == null)
            noCurrentToken();

        if (currentState.longValue == null)
            notIntegerValue();

        return currentState.longValue;
    }

    @Override
    public CharSequence getText() {
        if (currentState == null)
            return null;

        if (currentState.text != null)
            return currentState.text;

        if (currentState.longValue != null)
            return currentState.longValue.toString();

        if (currentState.doubleValue != null)
            return currentState.doubleValue.toString();

        return currentState.token.asString();
    }

    @Override
    public JsonToken nextToken() {
        if (index == states.size() - 1) {
            currentState = null;

            return null;
        }

        currentState = states.get(++index);

        return currentState.token;
    }

    private static class State {
        final JsonToken token;
        final Double doubleValue;
        final Long longValue;
        final String text;

        State(final JsonToken token, final Double doubleValue, final Long longValue, final String text) {
            this.token = token;
            this.doubleValue = doubleValue;
            this.longValue = longValue;
            this.text = text;
        }
    }

    private static void noCurrentToken() throws JsonParseException {
        parseError("No current token");
    }

    private static void notBooleanValue() throws JsonParseException {
        parseError("Not a boolean value");
    }

    private static void notNumericValue() throws JsonParseException {
        parseError("Not a numeric value");
    }

    private static void notIntegerValue() throws JsonParseException {
        parseError("Not an integer value");
    }

    private static void parseError(final String message) throws JsonParseException {
        throw new JsonParseException(message);
    }

}
