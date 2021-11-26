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
 * A JSON token.
 */
public enum JsonToken {

    /**
     * The end of an array ({@code ]}).
     */
    END_ARRAY("]"),

    /**
     * The end of an object (<code>}</code>).
     */
    END_OBJECT("}"),

    /**
     * A field name.
     */
    FIELD_NAME(null),

    /**
     * The start of an array ({@code [}).
     */
    START_ARRAY("["),

    /**
     * The start of an object (<code>{</code>).
     */
    START_OBJECT("{"),

    /**
     * The false value ({@code false}).
     */
    VALUE_FALSE("false"),

    /**
     * The null value ({@code null}).
     */
    VALUE_NULL("null"),

    /**
     * A decimal value.
     */
    VALUE_NUMBER_FLOAT(null),

    /**
     * An integer value.
     */
    VALUE_NUMBER_INT(null),

    /**
     * A string value.
     */
    VALUE_STRING(null),

    /**
     * The true value ({@code true}).
     */
    VALUE_TRUE("true");

    private final String asString;

    private JsonToken(final String asString) {
        this.asString = asString;
    }

    /**
     * Returns a string representing the constant value of this token or
     * {@code null} if this token has no constant value.
     *
     * @return a string representing the constant value of this token or
     *     {@code null} if this token has no constant value
     */
    public String asString() {
        return asString;
    }

    /**
     * Returns true if this token is {@link #VALUE_FALSE} or
     * {@link #VALUE_TRUE}.
     *
     * @return true if this token is {@link #VALUE_FALSE} or
     *     {@link #VALUE_TRUE}, otherwise false
     */
    public boolean isBoolean() {
        return this == VALUE_FALSE || this == VALUE_TRUE;
    }

    /**
     * Returns true if this token is {@link #VALUE_NUMBER_FLOAT} or
     * {@link #VALUE_NUMBER_INT}.
     *
     * @return true if this token is {@link #VALUE_NUMBER_FLOAT} or
     *     {@link #VALUE_NUMBER_INT}, otherwise false
     */
    public boolean isNumeric() {
        return this == VALUE_NUMBER_FLOAT || this == VALUE_NUMBER_INT;
    }

    /**
     * <p>Returns true if this token represents a scalar value. The following
     * tokens represent a scalar value:</p>
     * <ul>
     *   <li>{@link #VALUE_FALSE}</li>
     *   <li>{@link #VALUE_NULL}</li>
     *   <li>{@link #VALUE_NUMBER_FLOAT}</li>
     *   <li>{@link #VALUE_NUMBER_INT}</li>
     *   <li>{@link #VALUE_STRING}</li>
     *   <li>{@link #VALUE_TRUE}</li>
     * </ul>
     *
     * @return true if this token represents a scalar value, otherwise false
     */
    public boolean isScalarValue() {
        switch (this) {
        case VALUE_FALSE:
        case VALUE_NULL:
        case VALUE_NUMBER_FLOAT:
        case VALUE_NUMBER_INT:
        case VALUE_STRING:
        case VALUE_TRUE:
            return true;
        }

        return false;
    }

    /**
     * Returns true if this token is {@link #END_ARRAY} or {@link #END_OBJECT}.
     *
     * @return true if this token is {@link #END_ARRAY} or {@link #END_OBJECT},
     *     otherwise false
     */
    public boolean isStructEnd() {
        return this == END_ARRAY || this == END_OBJECT;
    }

    /**
     * Returns true if this token is {@link #START_ARRAY} or
     * {@link #START_OBJECT}.
     *
     * @return true if this token is {@link #START_ARRAY} or
     *     {@link #START_OBJECT}, otherwise false
     */
    public boolean isStructStart() {
        return this == START_ARRAY || this == START_OBJECT;
    }

}
