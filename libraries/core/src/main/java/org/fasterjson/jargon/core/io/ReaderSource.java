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
import org.fasterjson.jargon.core.CharJsonParser;

/**
 * A {@link Reader} source.
 *
 * @see CharJsonParser
 */
public class ReaderSource implements CharSource {

    private Reader input;

    /**
     * Construct a new instance.
     */
    public ReaderSource() {
        reset(null);
    }

    /**
     * Reset this source.
     *
     * @param input the input document
     */
    public void reset(final Reader input) {
        this.input = input;
    }

    @Override
    public int read(final char[] buffer) throws IOException {
        if (input == null)
            return -1;

        return input.read(buffer);
    }

}
