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

import static java.nio.charset.StandardCharsets.*;

import java.io.ByteArrayInputStream;
import org.fasterjson.jargon.core.io.InputStreamSource;
import org.junit.jupiter.api.BeforeEach;

class ByteStreamJsonParserTest extends JsonParserTest<ByteStreamJsonParser> {

    private InputStreamSource source;

    @BeforeEach
    void setUp() {
        source = new InputStreamSource();

        parser = new ByteStreamJsonParser(CONFIG);
    }

    @Override
    void reset(final String input) {
        source.reset(new ByteArrayInputStream(input.getBytes(UTF_8)));
        parser.reset(source);
    }

}
