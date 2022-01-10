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
import org.fasterjson.jargon.core.io.ByteArraySource;
import org.fasterjson.jargon.core.io.InputStreamSource;
import org.fasterjson.jargon.core.io.ByteSource;

/**
 * A JSON parser that reads from byte sources.
 *
 * @see ByteArraySource
 * @see InputStreamSource
 */
public class ByteJsonParser extends AbstractJsonParser {

    private static final ByteSource EMPTY_SOURCE = new ByteSource() {

        @Override
        public int read(final byte[] buffer) {
            return -1;
        }

    };

    private final byte[] buffer;

    private int length;

    private int index;

    private ByteSource source;

    /**
     * Construct a new instance using the default configuration.
     */
    public ByteJsonParser() {
        this(JsonParserConfig.DEFAULTS);
    }

    /**
     * Construct a new instance using a custom configuration.
     *
     * @param config the configuration
     */
    public ByteJsonParser(final JsonParserConfig config) {
        super(config);

        buffer = new byte[config.getBufferSize()];

        reset(EMPTY_SOURCE);
    }

    /**
     * Reset this instance.
     *
     * @param source the source
     */
    public void reset(final ByteSource source) {
        super.reset();

        this.length = 0;

        this.index = 0;

        this.source = source;
    }

    @Override
    int nextAsciiChar() throws IOException {
        if (index < length)
            return buffer[index++];

        return nextAsciiCharSlowPath();
    }

    private int nextAsciiCharSlowPath() throws IOException {
        if (length == -1)
            return -1;

        while (true) {
            length = source.read(buffer);
            if (length == -1)
                return -1;

            if (length == 0)
                continue;

            break;
        }

        index = 1;

        return buffer[0];
    }

}
