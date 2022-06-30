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

import org.fasterjson.jargon.core.CharJsonParser;

/**
 * A {@link CharSequence} source.
 *
 * @see CharJsonParser
 */
public class CharSequenceSource implements CharSource {

    private static final String EMPTY_INPUT = "";

    private CharSequence input;

    private int inputOffset;

    private int inputLength;

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

        this.inputOffset = 0;

        this.inputLength = input.length();
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

        this.inputOffset = offset;

        this.inputLength = length;
    }

    @Override
    public int read(final char[] buffer, final int offset) {
        if (inputLength == 0)
            return -1;

        int count = Math.min(inputLength, buffer.length - offset);

        if (input instanceof String) {
            ((String)input).getChars(inputOffset, inputOffset + count, buffer, offset);
        }
        else {
            for (int i = 0; i < count; i++)
                buffer[offset + i] = input.charAt(inputOffset + i);
        }

        inputOffset += count;
        inputLength -= count;

        return count;
    }

}
