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
package org.fasterjson.jargon.test.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.fasterjson.jargon.core.RandomCharAccessJsonParser;
import org.fasterjson.jargon.core.io.CharSequenceSource;
import org.fasterjson.jargon.databind.JsonNode;
import org.fasterjson.jargon.databind.JsonTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StringToJsonTreeTest {

    private static final String INPUT = "" +
        "{\n" +
        "  \"null\": null,\n" +
        "  \"false\": false,\n" +
        "  \"true\": true,\n" +
        "  \"double\": 1.23,\n" +
        "  \"long\": 123,\n" +
        "  \"string\": \"foo\",\n" +
        "  \"array\": [\n" +
        "    1,\n" +
        "    2,\n" +
        "    3\n" +
        "  ],\n" +
        "  \"object\": {\n" +
        "    \"foo\": 1,\n" +
        "    \"bar\": 2\n" +
        "  }\n" +
        "}";

    private JsonNode root;

    @BeforeEach
    void setUp() throws Exception {
        CharSequenceSource source = new CharSequenceSource();
        RandomCharAccessJsonParser parser = new RandomCharAccessJsonParser();

        source.reset(INPUT);
        parser.reset(source);

        JsonTree tree = new JsonTree();

        root = tree.reset(parser);
    }

    @Test
    void nullNode() {
        assertTrue(root.get("null").isNull());
    }

    @Test
    void falseNode() {
        JsonNode falseNode = root.get("false");

        assertTrue(falseNode.isBoolean());
        assertFalse(falseNode.booleanValue());
    }

    @Test
    void trueNode() {
        JsonNode trueNode = root.get("true");

        assertTrue(trueNode.isBoolean());
        assertTrue(trueNode.booleanValue());
    }

    @Test
    void doubleNode() {
        assertEquals(1.23, root.get("double").doubleValue());
    }

    @Test
    void longNode() {
        assertEquals(123, root.get("long").longValue());
    }

    @Test
    void stringNode() {
        assertTrue("foo".contentEquals(root.get("string").textValue()));
    }

    @Test
    void arrayNode() {
        JsonNode arrayNode = root.get("array");

        assertTrue(arrayNode.isArray());
        assertEquals(3, arrayNode.size());
        assertEquals(1, arrayNode.get(0).longValue());
        assertEquals(2, arrayNode.get(1).longValue());
        assertEquals(3, arrayNode.get(2).longValue());
    }

    @Test
    void objectNode() {
        JsonNode objectNode = root.get("object");

        assertTrue(objectNode.isObject());
        assertEquals(2, objectNode.size());
        assertEquals(1, objectNode.get("foo").longValue());
        assertEquals(2, objectNode.get("bar").longValue());
    }

}
