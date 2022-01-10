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
package org.fasterjson.jargon.bench.jargon.core;

import java.io.IOException;
import org.fasterjson.jargon.bench.Bench;
import org.fasterjson.jargon.core.JsonToken;
import org.fasterjson.jargon.core.CharJsonParser;
import org.fasterjson.jargon.core.io.CharSequenceSource;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class CharJsonParserBench extends Bench {

    private CharSequenceSource source;

    private CharJsonParser parser;

    @Setup(Level.Iteration)
    public void prepare() {
        source = new CharSequenceSource();

        parser = new CharJsonParser();
    }

    @Benchmark
    public JsonToken valueNull() throws IOException {
        return parse("null");
    }

    @Benchmark
    public JsonToken valueNumberFloat() throws IOException {
        return parse("1.23");
    }

    @Benchmark
    public JsonToken valueNumberInt() throws IOException {
        return parse("123");
    }

    @Benchmark
    public JsonToken valueString() throws IOException {
        return parse("\"foo\"");
    }

    @Benchmark
    public JsonToken emptyObject() throws IOException {
        return parse("{}");
    }

    @Benchmark
    public JsonToken nonEmptyObject() throws IOException {
        return parse("" +
                "{\n" +
                "  \"null\": null,\n" +
                "  \"float\": 1.23,\n" +
                "  \"int\": 123,\n" +
                "  \"string\": \"foo\"\n" +
                "}");
    }

    private JsonToken parse(final String input) throws IOException {
        source.reset(input);
        parser.reset(source);

        while (parser.nextToken() != null);

        return parser.currentToken();
    }

}
