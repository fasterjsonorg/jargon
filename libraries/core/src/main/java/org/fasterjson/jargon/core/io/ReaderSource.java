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

import java.io.IOException;
import java.io.Reader;
import org.fasterjson.jargon.core.CharStreamJsonParser;

/**
 * An {@link Reader} source.
 *
 * @see CharStreamJsonParser
 */
public class ReaderSource implements CharStreamSource {

    private final char[] buffer;

    private int length;

    private int index;

    private Reader input;

    /**
     * Construct a new instance using the default configuration.
     */
    public ReaderSource() {
        this(SourceConfig.DEFAULTS);
    }

    /**
     * Construct a new instance using a custom configuration.
     *
     * @param config the configuration
     */
    public ReaderSource(final SourceConfig config) {
        this.buffer = new char[config.getBufferSize()];

        this.length = -1;

        this.index = 0;

        this.input = null;
    }

    /**
     * Reset this instance.
     *
     * @param input the input
     */
    public void reset(final Reader input) {
        this.length = 0;

        this.index = 0;

        this.input = input;
    }

    @Override
    public int nextChar() throws IOException {
        if (index < length)
            return buffer[index++];

        return nextCharSlowPath();
    }

    private int nextCharSlowPath() throws IOException {
        if (length == -1)
            return -1;

        length = input.read(buffer);

        index = 0;

        return nextChar();
    }

}
