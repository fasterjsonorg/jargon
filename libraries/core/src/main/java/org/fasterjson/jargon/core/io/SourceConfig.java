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
package org.fasterjson.jargon.core.io;

/**
 * A source configuration.
 */
public class SourceConfig {

    /**
     * The default buffer size.
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024;

    /**
     * The defaults.
     */
    public static final SourceConfig DEFAULTS = SourceConfig.newBuilder().build();

    private final int bufferSize;

    private SourceConfig(final int bufferSize) {
        this.bufferSize = bufferSize;
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
     * A source configuration builder.
     */
    public static class Builder {

        private int bufferSize;

        private Builder() {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }

        /**
         * Set the buffer size.
         *
         * @param bufferSize the buffer size
         * @return this instance
         * @see SourceConfig#getBufferSize
         */
        public Builder setBufferSize(final int bufferSize) {
            this.bufferSize = bufferSize;

            return this;
        }

        /**
         * Build the source configuration.
         *
         * @return the source configuration
         */
        public SourceConfig build() {
            return new SourceConfig(bufferSize);
        }

    }

}
