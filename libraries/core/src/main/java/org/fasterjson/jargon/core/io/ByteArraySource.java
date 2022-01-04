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

import org.fasterjson.jargon.core.RandomByteAccessJsonParser;

/**
 * A byte array source.
 *
 * @see RandomByteAccessJsonParser
 */
public class ByteArraySource implements RandomByteAccessSource {

    private static final byte[] EMPTY_INPUT = {};

    private byte[] input;

    private int offset;

    private int length;

    /**
     * Construct a new instance.
     */
    public ByteArraySource() {
        reset(EMPTY_INPUT);
    }

    /**
     * Reset this source.
     *
     * @param input the input document
     */
    public void reset(final byte[] input) {
        this.input = input;

        this.offset = 0;

        this.length = input.length;
    }

    /**
     * Reset this source.
     *
     * @param input the input document
     * @param offset the offset
     * @param length the length
     */
    public void reset(final byte[] input, final int offset, final int length) {
        this.input = input;

        this.offset = offset;

        this.length = length;
    }

    @Override
    public byte byteAt(final int index) {
        return input[offset + index];
    }

    @Override
    public int length() {
        return length;
    }

}