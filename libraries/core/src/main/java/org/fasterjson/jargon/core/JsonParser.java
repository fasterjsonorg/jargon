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
package org.fasterjson.jargon.core;

/**
 * A JSON parser.
 */
public interface JsonParser {

    /**
     * Get the field name associated with the current token. If the current
     * token is {@link JsonToken#FIELD_NAME}, this method is equal to
     * {@link #getText()}. If the current token represents an object value,
     * this method returns the preceding field name. Otherwise this method
     * returns {@code null}.
     *
     * @return the field name associated with the current token or {@code null}
     *     if there is no current token or the current token is not associated
     *     with a field name
     */
    CharSequence currentName();

    /**
     * Get the current token or {@code null} if there is no current token.
     *
     * @return the current token or {@code null} if there is no current token
     */
    JsonToken currentToken();

    /**
     * Get the boolean value of the current token. The current token must be
     * {@link JsonToken#VALUE_TRUE} or {@link JsonToken#VALUE_FALSE}.
     *
     * @return true if the current token is {@link JsonToken#VALUE_TRUE} or
     *     false if it is {@link JsonToken#VALUE_FALSE}
     * @throws JsonParseException if the current token is not
     *     {@link JsonToken#VALUE_TRUE} or {@link JsonToken#VALUE_FALSE}
     */
    boolean getBooleanValue() throws JsonParseException;

    /**
     * Get the double value of the current token. The current token must be
     * {@link JsonToken#VALUE_NUMBER_FLOAT} or
     * {@link JsonToken#VALUE_NUMBER_INT}.
     *
     * @return the double value of the current token
     * @throws JsonParseException if the current token is not
     *     {@link JsonToken#VALUE_NUMBER_FLOAT} or
     *     {@link JsonToken#VALUE_NUMBER_INT}
     */
    double getDoubleValue() throws JsonParseException;

    /**
     * Get the long value of the current token. The current token must be
     * {@link JsonToken#VALUE_NUMBER_INT}.
     *
     * @return the long value of the current token
     * @throws JsonParseException if the current token is not
     *     {@link JsonToken#VALUE_NUMBER_INT}
     */
    long getLongValue() throws JsonParseException;

    /**
     * Get a textual representation of the current token or {@code null} if
     * there is no current token.
     *
     * @return a textual representation of the current token or {@code null}
     *     if there is no current token
     */
    CharSequence getText();

    /**
     * Get the next token or {@code null} if there are no more tokens.
     *
     * @return the next token or {@code null} if there are no more tokens
     */
    JsonToken nextToken();

}
