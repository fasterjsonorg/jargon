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
import org.fasterjson.jargon.core.io.ByteStreamSource;
import org.fasterjson.jargon.core.io.InputStreamSource;

/**
 * A byte stream JSON parser. A JSON parser of this kind can read from sources
 * that provide efficient access to the next byte of the input.
 *
 * @see InputStreamSource
 */
public class ByteStreamJsonParser extends AbstractJsonParser {

    private static final InputStreamSource EMPTY_SOURCE = new InputStreamSource();

    private ByteStreamSource source;

    /**
     * Construct a new instance using the default configuration.
     */
    public ByteStreamJsonParser() {
        this(JsonParserConfig.DEFAULTS);
    }

    /**
     * Construct a new instance using a custom configuration.
     *
     * @param config the configuration
     */
    public ByteStreamJsonParser(final JsonParserConfig config) {
        super(config);

        reset(EMPTY_SOURCE);
    }

    /**
     * Reset this instance.
     *
     * @param source the source
     */
    public void reset(final ByteStreamSource source) {
        super.reset();

        this.source = source;
    }

    @Override
    int nextAsciiChar() throws IOException {
        return source.nextByte();
    }

}
