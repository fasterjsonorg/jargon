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

/**
 * A JSON parser configuration.
 */
public class JsonParserConfig {

    /**
     * The default buffer size.
     */
    public static final int DEFAULT_BUFFER_SIZE = 65536;

    /**
     * The default minimum capacity for nesting depth.
     */
    public static final int DEFAULT_MIN_NESTING_CAPACITY = 2;

    /**
     * The default maximum capacity for nesting depth.
     */
    public static final int DEFAULT_MAX_NESTING_CAPACITY = Integer.MAX_VALUE;

    /**
     * The default minimum capacity for a field name.
     */
    public static final int DEFAULT_MIN_FIELD_NAME_CAPACITY = 8;

    /**
     * The default maximum capacity for a field name.
     */
    public static final int DEFAULT_MAX_FIELD_NAME_CAPACITY = Integer.MAX_VALUE;

    /**
     * The default minimum capacity for a string value.
     */
    public static final int DEFAULT_MIN_STRING_CAPACITY = 8;

    /**
     * The default maximum capacity for a string value.
     */
    public static final int DEFAULT_MAX_STRING_CAPACITY = Integer.MAX_VALUE;

    /**
     * The defaults.
     */
    public static final JsonParserConfig DEFAULTS = JsonParserConfig.newBuilder().build();

    private final int bufferSize;

    private final int minNestingCapacity;
    private final int maxNestingCapacity;

    private final int minFieldNameCapacity;
    private final int maxFieldNameCapacity;

    private final int minStringCapacity;
    private final int maxStringCapacity;

    private JsonParserConfig(final int bufferSize, final int minNestingCapacity,
            final int maxNestingCapacity, final int minFieldNameCapacity,
            final int maxFieldNameCapacity, final int minStringCapacity,
            final int maxStringCapacity) {
        this.bufferSize = bufferSize;

        this.minNestingCapacity = minNestingCapacity;
        this.maxNestingCapacity = maxNestingCapacity;

        this.minFieldNameCapacity = minFieldNameCapacity;
        this.maxFieldNameCapacity = maxFieldNameCapacity;

        this.minStringCapacity = minStringCapacity;
        this.maxStringCapacity = maxStringCapacity;
    }

    /**
     * Create a new configuration builder.
     *
     * @return a new configuration builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Get the buffer size.
     *
     * @return the buffer size
     */
    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * Get the minimum capacity for nesting depth. The JSON parser preallocates
     * storage for handling a nesting depth less than or equal to this value.
     *
     * @return the minimum capacity for nesting depth
     */
    public int getMinNestingCapacity() {
        return minNestingCapacity;
    }

    /**
     * Get the maximum capacity for nesting depth. The JSON parser throws a
     * {@link JsonParseException} if the nesting depth exceeds this value.
     *
     * @return the maximum capacity for nesting depth
     */
    public int getMaxNestingCapacity() {
        return maxNestingCapacity;
    }

    /**
     * Get the minimum capacity for a field name. The JSON parser preallocates
     * storage for handling a field name with length less than or equal to this
     * value.
     *
     * @return the minimum capacity for a field name
     */
    public int getMinFieldNameCapacity() {
        return minFieldNameCapacity;
    }

    /**
     * Get the maximum capacity for a field name. The JSON parser throws a
     * {@link JsonParseException} if it encounters a field name longer than
     * this value.
     *
     * @return the maximum capacity for a field name
     */
    public int getMaxFieldNameCapacity() {
        return maxFieldNameCapacity;
    }

    /**
     * Get the minimum capacity for a string. The JSON parser preallocates
     * storage for handling a string with length less than or equal to this
     * value.
     *
     * @return the minimum capacity for a string
     */
    public int getMinStringCapacity() {
        return minStringCapacity;
    }

    /**
     * Get the maximum capacity for a string. The JSON parser throws a
     * {@link JsonParseException} if it encounters a string longer than this
     * value.
     *
     * @return the maximum capacity for a string
     */
    public int getMaxStringCapacity() {
        return maxStringCapacity;
    }

    /**
     * A JSON parser configuration builder.
     */
    public static class Builder {

        private int bufferSize;

        private int minNestingCapacity;
        private int maxNestingCapacity;

        private int minFieldNameCapacity;
        private int maxFieldNameCapacity;

        private int minStringCapacity;
        private int maxStringCapacity;

        private Builder() {
            bufferSize = DEFAULT_BUFFER_SIZE;

            minNestingCapacity = DEFAULT_MIN_NESTING_CAPACITY;
            maxNestingCapacity = DEFAULT_MAX_NESTING_CAPACITY;

            minFieldNameCapacity = DEFAULT_MIN_FIELD_NAME_CAPACITY;
            maxFieldNameCapacity = DEFAULT_MAX_FIELD_NAME_CAPACITY;

            minStringCapacity = DEFAULT_MIN_STRING_CAPACITY;
            maxStringCapacity = DEFAULT_MAX_STRING_CAPACITY;
        }

        /**
         * Set the buffer size.
         *
         * @param bufferSize the buffer size
         * @return this instance
         * @see JsonParserConfig#getBufferSize
         */
        public Builder setBufferSize(final int bufferSize) {
            this.bufferSize = bufferSize;

            return this;
        }

        /**
         * Set the minimum capacity for nesting depth.
         *
         * @param minNestingCapacity the minimum capacity for nesting depth
         * @return this instance
         * @see JsonParserConfig#getMinNestingCapacity
         */
        public Builder setMinNestingCapacity(final int minNestingCapacity) {
            this.minNestingCapacity = minNestingCapacity;

            return this;
        }

        /**
         * Set the maximum capacity for nesting depth.
         *
         * @param maxNestingCapacity the maximum capacity for nesting depth
         * @return this instance
         * @see JsonParserConfig#getMaxNestingCapacity
         */
        public Builder setMaxNestingCapacity(final int maxNestingCapacity) {
            this.maxNestingCapacity = maxNestingCapacity;

            return this;
        }

        /**
         * Set the minimum capacity for a field name.
         *
         * @param minFieldNameCapacity the minimum capacity for a field name
         * @return this instance
         * @see JsonParserConfig#getMinFieldNameCapacity
         */
        public Builder setMinFieldNameCapacity(final int minFieldNameCapacity) {
            this.minFieldNameCapacity = minFieldNameCapacity;

            return this;
        }

        /**
         * Set the maximum capacity for a field name.
         *
         * @param maxFieldNameCapacity the maximum capacity for a field name
         * @return this instance
         * @see JsonParserConfig#getMinFieldNameCapacity
         */
        public Builder setMaxFieldNameCapacity(final int maxFieldNameCapacity) {
            this.maxFieldNameCapacity = maxFieldNameCapacity;

            return this;
        }

        /**
         * Set the minimum capacity for a string.
         *
         * @param minStringCapacity the minimum capacity for a string
         * @return this instance
         * @see JsonParserConfig#getMinStringCapacity
         */
        public Builder setMinStringCapacity(final int minStringCapacity) {
            this.minStringCapacity = minStringCapacity;

            return this;
        }

        /**
         * Set the maximum capacity for a string.
         *
         * @param maxStringCapacity the maximum capacity for a string
         * @return this instance
         * @see JsonParserConfig#getMaxStringCapacity
         */
        public Builder setMaxStringCapacity(final int maxStringCapacity) {
            this.maxStringCapacity = maxStringCapacity;

            return this;
        }

        /**
         * Build the JSON tree configuration.
         *
         * @return the JSON tree configuration
         */
        public JsonParserConfig build() {
            return new JsonParserConfig(bufferSize, minNestingCapacity,
                    maxNestingCapacity, minFieldNameCapacity,
                    maxFieldNameCapacity, minStringCapacity,
                    maxStringCapacity);
        }

    }

}
