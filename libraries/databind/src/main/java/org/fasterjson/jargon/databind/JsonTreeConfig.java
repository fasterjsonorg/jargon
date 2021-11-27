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

import org.fasterjson.jargon.core.JsonParser;

/**
 * A JSON tree configuration.
 */
public class JsonTreeConfig {

    /**
     * The default minimum capacity for JSON nodes.
     */
    public static final int DEFAULT_MIN_NODE_CAPACITY = 4;

    /**
     * The default maximum capacity for JSON nodes.
     */
    public static final int DEFAULT_MAX_NODE_CAPACITY = Integer.MAX_VALUE;

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
    public static final int DEFAULT_MIN_FIELD_NAME_CAPACITY = 4;

    /**
     * The default maximum capacity for a field name.
     */
    public static final int DEFAULT_MAX_FIELD_NAME_CAPACITY = Integer.MAX_VALUE;

    /**
     * The default minimum capacity for a string value.
     */
    public static final int DEFAULT_MIN_TEXT_VALUE_CAPACITY = 4;

    /**
     * The default maximum capacity for a string value.
     */
    public static final int DEFAULT_MAX_TEXT_VALUE_CAPACITY = Integer.MAX_VALUE;

    /**
     * The defaults.
     */
    public static final JsonTreeConfig DEFAULTS = JsonTreeConfig.newBuilder().build();

    private final int minNodeCapacity;
    private final int maxNodeCapacity;

    private final int minNestingCapacity;
    private final int maxNestingCapacity;

    private final int minFieldNameCapacity;
    private final int maxFieldNameCapacity;

    private final int minTextValueCapacity;
    private final int maxTextValueCapacity;

    private JsonTreeConfig(final int minNodeCapacity, final int maxNodeCapacity,
            final int minNestingCapacity, final int maxNestingCapacity,
            final int minFieldNameCapacity, final int maxFieldNameCapacity,
            final int minTextValueCapacity, final int maxTextValueCapacity) {
        this.minNodeCapacity = minNodeCapacity;
        this.maxNodeCapacity = maxNodeCapacity;

        this.minNestingCapacity = minNestingCapacity;
        this.maxNestingCapacity = maxNestingCapacity;

        this.minFieldNameCapacity = minFieldNameCapacity;
        this.maxFieldNameCapacity = maxFieldNameCapacity;

        this.minTextValueCapacity = minTextValueCapacity;
        this.maxTextValueCapacity = maxTextValueCapacity;
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
     * Get the minimum capacity for JSON nodes. The JSON tree preallocates
     * storage for handling a number of JSON nodes less than or equal to
     * this value.
     *
     * @return the minimum capacity for JSON nodes
     */
    public int getMinNodeCapacity() {
        return minNodeCapacity;
    }

    /**
     * Get the maximum capacity for JSON nodes.
     * {@link JsonTree#reset(JsonParser)} throws a {@link JsonMappingException}
     * if the number of JSON nodes exceeds this value.
     *
     * @return the maximum capacity for JSON nodes
     */
    public int getMaxNodeCapacity() {
        return maxNodeCapacity;
    }

    /**
     * Get the minimum capacity for nesting depth. The JSON tree preallocates
     * storage for handling a nesting depth less than or equal to this value.
     *
     * @return the minimum capacity for nesting depth
     */
    public int getMinNestingCapacity() {
        return minNestingCapacity;
    }

    /**
     * Get the maximum capacity for nesting depth.
     * {@link JsonTree#reset(JsonParser)} throws a {@link JsonMappingException}
     * if the nesting depth exceeds this value.
     *
     * @return the maximum capacity for nesting depth
     */
    public int getMaxNestingCapacity() {
        return maxNestingCapacity;
    }

    /**
     * Get the minimum capacity for a field name. For each JSON node, the JSON
     * tree preallocates storage for handling a field name with length less
     * than or equal to this value.
     *
     * @return the minimum capacity for a field name
     */
    public int getMinFieldNameCapacity() {
        return minFieldNameCapacity;
    }

    /**
     * Get the maximum capacity for a field name.
     * {@link JsonTree#reset(JsonParser)} throws a {@link JsonMappingException}
     * if it encounters a field name longer than this value.
     *
     * @return the maximum capacity for a field name
     */
    public int getMaxFieldNameCapacity() {
        return maxFieldNameCapacity;
    }

    /**
     * Get the minimum capacity for a string value. For each JSON node, the
     * JSON tree preallocates storage for handling a string value with length
     * less than or equal to this value.
     *
     * @return the minimum capacity for a string value
     * @see JsonNode#textValue()
     */
    public int getMinTextValueCapacity() {
        return minTextValueCapacity;
    }

    /**
     * Get the maximum capacity for a string value.
     * {@link JsonTree#reset(JsonParser)} throws a {@link JsonMappingException}
     * if it encounters a string value longer than this value.
     *
     * @return the maximum capacity for a string value
     * @see JsonNode#textValue()
     */
    public int getMaxTextValueCapacity() {
        return maxTextValueCapacity;
    }

    /**
     * A JSON tree configuration builder.
     */
    public static class Builder {

        private int minNodeCapacity;
        private int maxNodeCapacity;

        private int minNestingCapacity;
        private int maxNestingCapacity;

        private int minFieldNameCapacity;
        private int maxFieldNameCapacity;

        private int minTextValueCapacity;
        private int maxTextValueCapacity;

        private Builder() {
            minNodeCapacity = DEFAULT_MIN_NODE_CAPACITY;
            maxNodeCapacity = DEFAULT_MAX_NODE_CAPACITY;

            minNestingCapacity = DEFAULT_MIN_NESTING_CAPACITY;
            maxNestingCapacity = DEFAULT_MAX_NESTING_CAPACITY;

            minFieldNameCapacity = DEFAULT_MIN_FIELD_NAME_CAPACITY;
            maxFieldNameCapacity = DEFAULT_MAX_FIELD_NAME_CAPACITY;

            minTextValueCapacity = DEFAULT_MIN_TEXT_VALUE_CAPACITY;
            maxTextValueCapacity = DEFAULT_MAX_TEXT_VALUE_CAPACITY;
        }

        /**
         * Set the minimum capacity for JSON nodes.
         *
         * @param minNodeCapacity the minimum capacity for JSON nodes
         * @return this instance
         * @see JsonTreeConfig#getMinNodeCapacity
         */
        public Builder setMinNodeCapacity(final int minNodeCapacity) {
            this.minNodeCapacity = minNodeCapacity;

            return this;
        }

        /**
         * Set the maximum capacity for JSON nodes.
         *
         * @param maxNodeCapacity the maximum capacity for JSON nodes
         * @return this instance
         * @see JsonTreeConfig#getMaxNodeCapacity
         */
        public Builder setMaxNodeCapacity(final int maxNodeCapacity) {
            this.maxNodeCapacity = maxNodeCapacity;

            return this;
        }

        /**
         * Set the minimum capacity for nesting depth.
         *
         * @param minNestingCapacity the minimum capacity for nesting depth
         * @return this instance
         * @see JsonTreeConfig#getMinNestingCapacity
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
         * @see JsonTreeConfig#getMaxNestingCapacity
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
         * @see JsonTreeConfig#getMinFieldNameCapacity
         */
        public Builder setMinFieldNameCapacity(final int minFieldNameCapacity) {
            this.minFieldNameCapacity = minFieldNameCapacity;

            return this;
        }

        /**
         * Set the maximum capacity for field name.
         *
         * @param maxFieldNameCapacity the maximum capacity for a field name
         * @return this instance
         * @see JsonTreeConfig#getMaxFieldNameCapacity
         */
        public Builder setMaxFieldNameCapacity(final int maxFieldNameCapacity) {
            this.maxFieldNameCapacity = maxFieldNameCapacity;

            return this;
        }

        /**
         * Set the minimum capacity for a string value.
         *
         * @param minTextValueCapacity the minimum capacity for a string value
         * @return this instance
         * @see JsonTreeConfig#getMinTextValueCapacity
         */
        public Builder setMinTextValueCapacity(final int minTextValueCapacity) {
            this.minTextValueCapacity = minTextValueCapacity;

            return this;
        }

        /**
         * Set the maximum capacity for a string value.
         *
         * @param maxTextValueCapacity the maximum capacity for a string value
         * @return this instance
         * @see JsonTreeConfig#getMaxTextValueCapacity
         */
        public Builder setMaxTextValueCapacity(final int maxTextValueCapacity) {
            this.maxTextValueCapacity = maxTextValueCapacity;

            return this;
        }

        /**
         * Build the JSON tree configuration.
         *
         * @return the JSON tree configuration
         */
        public JsonTreeConfig build() {
            return new JsonTreeConfig(minNodeCapacity, maxNodeCapacity,
                    minNestingCapacity, maxNestingCapacity,
                    minFieldNameCapacity, maxFieldNameCapacity,
                    minTextValueCapacity, maxTextValueCapacity);
        }

    }

}
