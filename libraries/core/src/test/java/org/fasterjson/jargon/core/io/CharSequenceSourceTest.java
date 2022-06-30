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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CharSequenceSourceTest {

    private static final String INPUT = "FOO";

    private static final char[] BUFFER = { 0, 0, 'F', 'O', 'O', 0 };

    private CharSequenceSource source;

    private char[] buffer;

    @BeforeEach
    void setUp() {
        source = new CharSequenceSource();

        buffer = new char[6];
    }

    @Test
    void resetWithString() {
        source.reset(INPUT);

        int count = source.read(buffer, 2);

        assertEquals(3, count);
        assertArrayEquals(BUFFER, buffer);
    }

    @Test
    void resetWithStringBuilder() {
        source.reset(new StringBuilder(INPUT));

        int count = source.read(buffer, 2);

        assertEquals(3, count);
        assertArrayEquals(BUFFER, buffer);
    }

}
