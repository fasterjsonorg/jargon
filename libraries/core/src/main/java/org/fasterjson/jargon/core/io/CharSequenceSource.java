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

import org.fasterjson.jargon.core.RandomCharAccessJsonParser;

/**
 * A {@link CharSequence} source.
 *
 * @see RandomCharAccessJsonParser
 */
public class CharSequenceSource implements RandomCharAccessSource {

    private static final String EMPTY_INPUT = "";

    private CharSequence input;

    private int offset = 0;

    private int length = 0;

    /**
     * Construct a new instance.
     */
    public CharSequenceSource() {
        reset(EMPTY_INPUT);
    }

    /**
     * Reset this source.
     *
     * @param input the input
     */
    public void reset(final CharSequence input) {
        this.input = input;

        this.offset = 0;

        this.length = input.length();
    }

    /**
     * Reset this source.
     *
     * @param input the input
     * @param offset the offset
     * @param length the length
     */
    public void reset(final CharSequence input, final int offset, final int length) {
        this.input = input;

        this.offset = offset;

        this.length = length;
    }

    @Override
    public char charAt(final int index) {
        return input.charAt(offset + index);
    }

    @Override
    public int length() {
        return length;
    }

}
