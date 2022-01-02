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

import org.fasterjson.jargon.core.io.ByteArraySource;
import org.fasterjson.jargon.core.io.RandomByteAccessSource;

/**
 * A random byte access JSON parser. A JSON parser of this kind can read from
 * sources that provide efficient access to any byte in the input at once.
 *
 * @see ByteArraySource
 */
public class RandomByteAccessJsonParser extends AbstractJsonParser {

    private static final RandomByteAccessSource EMPTY_SOURCE = new ByteArraySource();

    private RandomByteAccessSource source;

    private int index;
    private int length;

    /**
     * Construct a new instance using the default configuration.
     */
    public RandomByteAccessJsonParser() {
        this(JsonParserConfig.DEFAULTS);
    }

    /**
     * Construct a new instance using a custom configuration.
     *
     * @param config the configuration
     */
    public RandomByteAccessJsonParser(final JsonParserConfig config) {
        super(config);

        reset(EMPTY_SOURCE);
    }

    /**
     * Reset this instance.
     *
     * @param source the source
     */
    public void reset(final RandomByteAccessSource source) {
        super.reset();

        this.source = source;

        this.index = 0;
        this.length = source.length();
    }

    @Override
    int nextAsciiChar() {
        if (index == length)
            return -1;

        return source.byteAt(index++);
    }

}
