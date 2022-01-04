/*
 * Copyright 2021 Jargon authors
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
package org.fasterjson.jargon.databind;

import static org.junit.jupiter.api.Assertions.*;
import static org.fasterjson.jargon.core.JsonToken.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class JsonTreeTest {

    private static final JsonTreeConfig CONFIG = JsonTreeConfig.newBuilder()
        .setMinNodeCapacity(16)
        .setMaxNodeCapacity(64)
        .setMinNestingCapacity(4)
        .setMaxNestingCapacity(8)
        .setMinFieldNameCapacity(4)
        .setMaxFieldNameCapacity(8)
        .setMinTextValueCapacity(8)
        .setMaxTextValueCapacity(16)
        .build();

    private TestJsonParser parser;

    private JsonTree tree;

    private JsonNode root;

    @BeforeEach
    void setUp() {
        parser = new TestJsonParser();

        tree = new JsonTree(CONFIG);

        root = tree.getRoot();
    }

    @Test
    void missingNode() throws Exception {
        root = tree.reset(parser);

        assertMissingNode(root);
    }

    @Test
    void nullNode() throws Exception {
        parser.push(VALUE_NULL);

        root = tree.reset(parser);

        assertNullNode(root);
    }

    @Test
    void falseNode() throws Exception {
        parser.push(VALUE_FALSE);

        root = tree.reset(parser);

        assertBooleanNode(root, false);
    }

    @Test
    void trueNode() throws Exception {
        parser.push(VALUE_TRUE);

        root = tree.reset(parser);

        assertBooleanNode(root, true);
    }

    @Test
    void doubleNode() throws Exception {
        parser.push(VALUE_NUMBER_FLOAT, 1.23);

        root = tree.reset(parser);

        assertDoubleNode(root, 1.23, 1);
    }

    @Test
    void longNode() throws Exception {
        parser.push(VALUE_NUMBER_INT, 123);

        root = tree.reset(parser);

        assertLongNode(root, 123.0, 123);
    }

    @Test
    void stringNode() throws Exception {
        parser.push(VALUE_STRING, "foo");

        root = tree.reset(parser);

        assertStringNode(root, "foo");
    }

    @Test
    void emptyArrayNode() throws Exception {
        parser.push(START_ARRAY);
        parser.push(END_ARRAY);

        root = tree.reset(parser);

        assertArrayNode(root);
        assertEquals(0, root.size());

        assertNull(root.get(0));
        assertMissingNode(root.path(0));
    }

    @Test
    void arrayNodeWithOneElement() throws Exception {
        parser.push(START_ARRAY);
        parser.push(VALUE_NULL);
        parser.push(END_ARRAY);

        root = tree.reset(parser);

        assertArrayNode(root);
        assertEquals(1, root.size());

        assertNullNode(root.get(0));
        assertNullNode(root.path(0));

        assertNull(root.get(1));
        assertMissingNode(root.path(1));
    }

    @Test
    void arrayNodeWithTwoElements() throws Exception {
        parser.push(START_ARRAY);
        parser.push(VALUE_FALSE);
        parser.push(VALUE_TRUE);
        parser.push(END_ARRAY);

        root = tree.reset(parser);

        assertArrayNode(root);
        assertEquals(2, root.size());

        assertBooleanNode(root.get(0), false);
        assertBooleanNode(root.path(0), false);

        assertBooleanNode(root.get(1), true);
        assertBooleanNode(root.path(1), true);

        assertNull(root.get(2));
        assertMissingNode(root.path(2));

    }

    @Test
    void arrayNodeWithArrayElement() throws Exception {
        parser.push(START_ARRAY);
        parser.push(START_ARRAY);
        parser.push(VALUE_FALSE);
        parser.push(END_ARRAY);
        parser.push(VALUE_TRUE);
        parser.push(END_ARRAY);

        root = tree.reset(parser);

        assertArrayNode(root);
        assertEquals(2, root.size());

        assertArrayNode(root.get(0));
        assertArrayNode(root.path(0));
        assertEquals(1, root.path(0).size());

        assertBooleanNode(root.get(0).get(0), false);
        assertBooleanNode(root.path(0).path(0), false);

        assertNull(root.get(0).get(1));
        assertMissingNode(root.path(0).path(1));

        assertBooleanNode(root.get(1), true);
        assertBooleanNode(root.path(1), true);

        assertNull(root.get(2));
        assertMissingNode(root.path(2));
    }

    @Test
    void emptyObjectNode() throws Exception {
        parser.push(START_OBJECT);
        parser.push(END_OBJECT);

        root = tree.reset(parser);

        assertObjectNode(root);
        assertEquals(0, root.size());

        assertNull(root.get("foo"));
        assertMissingNode(root.path("foo"));
    }

    @Test
    void objectNodeWithOneField() throws Exception {
        parser.push(START_OBJECT);
        parser.push(FIELD_NAME, "foo");
        parser.push(VALUE_NULL);
        parser.push(END_OBJECT);

        root = tree.reset(parser);

        assertObjectNode(root);
        assertEquals(1, root.size());

        assertNullNode(root.get("foo"));
        assertNullNode(root.path("foo"));

        assertNull(root.get("bar"));
        assertMissingNode(root.path("bar"));
    }

    @Test
    void objectNodeWithTwoFields() throws Exception {
        parser.push(START_OBJECT);
        parser.push(FIELD_NAME, "foo");
        parser.push(VALUE_FALSE);
        parser.push(FIELD_NAME, "bar");
        parser.push(VALUE_TRUE);
        parser.push(END_OBJECT);

        root = tree.reset(parser);

        assertObjectNode(root);
        assertEquals(2, root.size());

        assertBooleanNode(root.get("foo"), false);
        assertBooleanNode(root.path("foo"), false);

        assertBooleanNode(root.get("bar"), true);
        assertBooleanNode(root.path("bar"), true);

        assertNull(root.get("baz"));
        assertMissingNode(root.path("baz"));
    }

    @Test
    void missingEndArrayForEmptyArray() throws Exception {
        parser.push(START_ARRAY);

        assertMappingError("Missing end of array" , () -> tree.reset(parser));
    }

    @Test
    void missingEndArrayForNonEmptyArray() throws Exception {
        parser.push(START_ARRAY);
        parser.push(VALUE_NULL);

        assertMappingError("Missing end of array" , () -> tree.reset(parser));
    }

    @Test
    void loneEndArray() throws Exception {
        parser.push(END_ARRAY);

        assertMappingError("Unexpected end of array", () -> tree.reset(parser));
    }

    @Test
    void mismatchingEndArray() throws Exception {
        parser.push(START_OBJECT);
        parser.push(END_ARRAY);

        assertMappingError("Unexpected end of array", () -> tree.reset(parser));
    }

    @Test
    void extraEndArray() throws Exception {
        parser.push(START_ARRAY);
        parser.push(END_ARRAY);
        parser.push(END_ARRAY);

        assertMappingError("Unexpected end of array", () -> tree.reset(parser));
    }

    @Test
    void missingEndObjectForEmptyObject() throws Exception {
        parser.push(START_OBJECT);

        assertMappingError("Missing end of object" , () -> tree.reset(parser));
    }

    @Test
    void missingEndObjectForNonEmptyObject() throws Exception {
        parser.push(START_OBJECT);
        parser.push(FIELD_NAME, "foo");
        parser.push(VALUE_NULL);

        assertMappingError("Missing end of object" , () -> tree.reset(parser));
    }

    @Test
    void loneEndObject() throws Exception {
        parser.push(END_OBJECT);

        assertMappingError("Unexpected end of object", () -> tree.reset(parser));
    }

    @Test
    void mismatchingEndObject() throws Exception {
        parser.push(START_ARRAY);
        parser.push(END_OBJECT);

        assertMappingError("Unexpected end of object", () -> tree.reset(parser));
    }

    @Test
    void extraEndObject() throws Exception {
        parser.push(START_OBJECT);
        parser.push(END_OBJECT);
        parser.push(END_OBJECT);

        assertMappingError("Unexpected end of object", () -> tree.reset(parser));
    }

    @Test
    void unexpectedFieldNameAsRoot() throws Exception {
        parser.push(FIELD_NAME, "foo");

        assertMappingError("Unexpected field name", () -> tree.reset(parser));
    }

    @Test
    void unexpectedFieldNameInArray() throws Exception {
        parser.push(START_ARRAY);
        parser.push(FIELD_NAME, "foo");
        parser.push(END_ARRAY);

        assertMappingError("Unexpected field name", () -> tree.reset(parser));
    }

    @Test
    void fieldNameWithoutValue() throws Exception {
        parser.push(START_OBJECT);
        parser.push(FIELD_NAME, "foo");
        parser.push(END_OBJECT);

        assertMappingError("Missing value", () -> tree.reset(parser));
    }

    @Test
    void valueWithoutFieldName() throws Exception {
        parser.push(START_OBJECT);
        parser.push(VALUE_TRUE);
        parser.push(END_OBJECT);

        assertMappingError("Missing field name", () -> tree.reset(parser));
    }

    @Test
    void maxNodeCapacity() throws Exception {
        parser.push(START_ARRAY);

        for (int i = 0; i < 62; i++)
            parser.push(VALUE_NULL);

        parser.push(END_ARRAY);

        root = tree.reset(parser);

        assertTrue(root.isArray());
        assertEquals(62, root.size());

        for (int i = 0; i < 62; i++)
            assertTrue(root.get(i).isNull());
    }

    @Test
    void exceedMaxNodeCapacity() throws Exception {
        parser.push(START_ARRAY);

        for (int i = 0; i < 64; i++)
            parser.push(VALUE_NULL);

        assertMappingError("Maximum node capacity exceeded", () -> tree.reset(parser));
    }

    @Test
    void maxNestingCapacity() throws Exception {
        for (int i = 0; i < 8; i++)
            parser.push(START_ARRAY);

        for (int i = 0; i < 8; i++)
            parser.push(END_ARRAY);

        tree.reset(parser);
    }

    @Test
    void exceedMaxNestingCapacity() throws Exception {
        for (int i = 0; i < 9; i++)
            parser.push(START_ARRAY);

        assertMappingError("Maximum nesting capacity exceeded", () -> tree.reset(parser));
    }

    @Test
    void maxFieldNameCapacity() throws Exception {
        parser.push(START_OBJECT);
        parser.push(FIELD_NAME, "aaaaaaaa");
        parser.push(VALUE_NULL);
        parser.push(END_OBJECT);

        tree.reset(parser);
    }

    @Test
    void exceedMaxFieldNameCapacity() throws Exception {
        parser.push(START_OBJECT);
        parser.push(FIELD_NAME, "aaaaaaaaa");
        parser.push(VALUE_NULL);
        parser.push(END_OBJECT);

        assertMappingError("Too long field name", () -> tree.reset(parser));
    }

    @Test
    void maxTextValueCapacity() throws Exception {
        parser.push(VALUE_STRING, "aaaaaaaaaaaaaaaa");

        tree.reset(parser);
    }

    @Test
    void exceedMaxTextValueCapacity() throws Exception {
        parser.push(VALUE_STRING, "aaaaaaaaaaaaaaaaa");

        assertMappingError("Too long string value", () -> tree.reset(parser));
    }

    private static void assertMissingNode(final JsonNode node) {
        assertTrue(node.isMissingNode());
        assertEquals(JsonNodeType.MISSING, node.getNodeType());
        assertFalse(node.isContainerNode());
        assertFalse(node.isValueNode());
        assertFalse(node.isArray());
        assertFalse(node.isBoolean());
        assertFalse(node.isDouble());
        assertFalse(node.isLong());
        assertFalse(node.isNull());
        assertFalse(node.isObject());
        assertFalse(node.isTextual());
        assertEquals(0, node.size());
        assertEquals(false, node.booleanValue());
        assertEquals(0.0, node.doubleValue());
        assertEquals(0, node.longValue());
        assertNull(node.textValue());
    }

    private static void assertArrayNode(final JsonNode node) {
        assertContainerNode(node);

        assertEquals(JsonNodeType.ARRAY, node.getNodeType());
        assertTrue(node.isArray());
        assertFalse(node.isObject());
    }

    private static void assertObjectNode(final JsonNode node) {
        assertContainerNode(node);

        assertEquals(JsonNodeType.OBJECT, node.getNodeType());
        assertFalse(node.isArray());
        assertTrue(node.isObject());
    }

    private static void assertContainerNode(final JsonNode node) {
        assertFalse(node.isMissingNode());
        assertTrue(node.isContainerNode());
        assertFalse(node.isValueNode());
        assertFalse(node.isBoolean());
        assertFalse(node.isDouble());
        assertFalse(node.isLong());
        assertFalse(node.isNull());
        assertFalse(node.isTextual());
        assertEquals(false, node.booleanValue());
        assertEquals(0.0, node.doubleValue());
        assertEquals(0, node.longValue());
        assertNull(node.textValue());
    }

    private static void assertBooleanNode(final JsonNode node, final boolean booleanValue) {
        assertValueNode(node);

        assertEquals(JsonNodeType.BOOLEAN, node.getNodeType());
        assertTrue(node.isBoolean());
        assertFalse(node.isDouble());
        assertFalse(node.isLong());
        assertFalse(node.isNull());
        assertFalse(node.isTextual());
        assertEquals(booleanValue, node.booleanValue());
        assertEquals(0.0, node.doubleValue());
        assertEquals(0, node.longValue());
        assertNull(node.textValue());
    }

    private static void assertNullNode(final JsonNode node) {
        assertValueNode(node);

        assertEquals(JsonNodeType.NULL, node.getNodeType());
        assertFalse(node.isBoolean());
        assertFalse(node.isDouble());
        assertFalse(node.isLong());
        assertTrue(node.isNull());
        assertFalse(node.isTextual());
        assertEquals(false, node.booleanValue());
        assertEquals(0.0, node.doubleValue());
        assertEquals(0, node.longValue());
        assertNull(node.textValue());
    }

    private static void assertDoubleNode(final JsonNode node, final double doubleValue,
            final long longValue) {
        assertNumberNode(node, doubleValue, longValue);

        assertTrue(node.isDouble());
        assertFalse(node.isLong());
    }

    private static void assertLongNode(final JsonNode node, final double doubleValue,
            final long longValue) {
        assertNumberNode(node, doubleValue, longValue);

        assertFalse(node.isDouble());
        assertTrue(node.isLong());
    }

    private static void assertNumberNode(final JsonNode node, final double doubleValue,
            final long longValue) {
        assertValueNode(node);

        assertEquals(JsonNodeType.NUMBER, node.getNodeType());
        assertFalse(node.isBoolean());
        assertFalse(node.isNull());
        assertFalse(node.isTextual());
        assertEquals(false, node.booleanValue());
        assertEquals(doubleValue, node.doubleValue());
        assertEquals(longValue, node.longValue());
        assertNull(node.textValue());
    }

    private static void assertStringNode(final JsonNode node, final String textValue) {
        assertValueNode(node);

        assertEquals(JsonNodeType.STRING, node.getNodeType());
        assertFalse(node.isBoolean());
        assertFalse(node.isDouble());
        assertFalse(node.isLong());
        assertFalse(node.isNull());
        assertTrue(node.isTextual());
        assertEquals(false, node.booleanValue());
        assertEquals(0.0, node.doubleValue());
        assertEquals(0, node.longValue());
        assertTrue(textValue.contentEquals(node.textValue()));
    }

    private static void assertValueNode(final JsonNode node) {
        assertFalse(node.isMissingNode());
        assertFalse(node.isContainerNode());
        assertTrue(node.isValueNode());
        assertFalse(node.isArray());
        assertFalse(node.isObject());
        assertEquals(0, node.size());
    }

    private static void assertMappingError(final String message, final Executable executable) {
        Exception exception = assertThrows(JsonMappingException.class, executable);

        assertEquals(message, exception.getMessage());
    }

}
