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
import org.fasterjson.jargon.core.io.CharArraySource;
import org.fasterjson.jargon.core.io.CharSequenceSource;
import org.fasterjson.jargon.core.io.CharSource;
import org.fasterjson.jargon.core.io.ReaderSource;

/**
 * A JSON parser that reads from character sources.
 *
 * @see CharArraySource
 * @see CharSequenceSource
 * @see ReaderSource
 */
public class CharJsonParser extends AbstractJsonParser {

    private static final CharSource EMPTY_SOURCE = new CharSource() {

        @Override
        public int read(final char[] buffer, final int offset) {
            return -1;
        }

    };

    private final char[] buffer;

    private int length;

    private int index;

    private CharSource source;

    /**
     * Construct a new instance using the default configuration.
     */
    public CharJsonParser() {
        this(JsonParserConfig.DEFAULTS);
    }

    /**
     * Construct a new instance using a custom configuration.
     *
     * @param config the configuration
     */
    public CharJsonParser(final JsonParserConfig config) {
        super(config);

        buffer = new char[config.getBufferSize()];

        reset(EMPTY_SOURCE);
    }

    /**
     * Reset this instance.
     *
     * @param source the source
     */
    public void reset(final CharSource source) {
        super.reset();

        this.length = 0;

        this.index = 0;

        this.source = source;
    }

    @Override
    void parseFalse() throws IOException {
        ensure(4);

        matchAsciiChar('a');
        matchAsciiChar('l');
        matchAsciiChar('s');
        matchAsciiChar('e');
    }

    @Override
    void parseNull() throws IOException {
        ensure(3);

        matchAsciiChar('u');
        matchAsciiChar('l');
        matchAsciiChar('l');
    }

    @Override
    void parseTrue() throws IOException {
        ensure(3);

        matchAsciiChar('r');
        matchAsciiChar('u');
        matchAsciiChar('e');
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
            length = source.read(buffer, 0);
            if (length == -1)
                return -1;

            if (length == 0)
                continue;

            break;
        }

        index = 1;

        return buffer[0];
    }

    private void matchAsciiChar(final char expectedCh) throws IOException {
        char actualCh = buffer[index++];

        if (actualCh != expectedCh)
            unexpectedAsciiChar(actualCh, expectedCh);
    }

    private void ensure(final int count) throws IOException {
        if (length - index < count)
            fill();

        if (length < count)
            unexpectedEof();
    }

    private void fill() throws IOException {
        int remaining = length - index;
        if (remaining > 0)
            System.arraycopy(buffer, index, buffer, 0, remaining);

        index = 0;

        while (true) {
            length = source.read(buffer, remaining);
            if (length == 0)
                continue;

            break;
        }

        length += remaining;
    }

}
