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

import static org.junit.jupiter.api.Assertions.*;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ReaderSourceTest {

    private static final SourceConfig CONFIG = SourceConfig.newBuilder()
        .setBufferSize(8)
        .build();

    private static final char[] INPUT = "abcdefghijklmnop".toCharArray();

    @Test
    void readChars() throws Exception {
        assertArrayEquals(INPUT, read(INPUT));
    }

    private static char[] read(final char[] input) throws IOException {
        ReaderSource source = new ReaderSource(CONFIG);

        source.reset(new CharArrayReader(input));

        char[] chars = new char[input.length];

        int index = 0;

        while (true) {
            int ch = source.nextChar();
            if (ch < 0)
                break;

            chars[index++] = (char)ch;
        }

        return Arrays.copyOf(chars, index);
    }

}
