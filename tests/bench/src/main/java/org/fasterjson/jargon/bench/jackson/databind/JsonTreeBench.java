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
package org.fasterjson.jargon.bench.jackson.databind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fasterjson.jargon.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;

public class JsonTreeBench extends Bench {

    private static final ObjectMapper MAPPER = new  ObjectMapper();

    @Benchmark
    public double sum() throws JsonProcessingException {
        JsonNode root = parse("" +
                "{\n" +
                "  \"null\": null,\n" +
                "  \"float\": 1.23,\n" +
                "  \"int\": 123,\n" +
                "  \"string\": \"foo\"\n" +
                "}");

        return root.get("float").doubleValue() + root.get("int").longValue();
    }

    private JsonNode parse(final String input) throws JsonProcessingException {
        return MAPPER.readTree(input);
    }

}
