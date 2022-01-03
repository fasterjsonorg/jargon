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

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class InputStreamSourceTest {

    private static final SourceConfig CONFIG = SourceConfig.newBuilder()
        .setBufferSize(8)
        .build();

    private static final byte[] INPUT = "abcdefghijklmnop".getBytes(UTF_8);

    @Test
    void readBytes() throws Exception {
        assertArrayEquals(INPUT, read(INPUT));
    }

    private static byte[] read(final byte[] input) throws IOException {
        InputStreamSource source = new InputStreamSource(CONFIG);

        source.reset(new ByteArrayInputStream(input));

        byte[] bytes = new byte[input.length];

        int index = 0;

        while (true) {
            int b = source.nextByte();
            if (b < 0)
                break;

            bytes[index++] = (byte)b;
        }

        return Arrays.copyOf(bytes, index);
    }

}
