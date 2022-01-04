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

import java.io.IOException;
import java.util.Arrays;
import org.fasterjson.jargon.core.JsonParser;
import org.fasterjson.jargon.core.JsonToken;
import org.fasterjson.jargon.databind.node.MissingNode;

/**
 * <p>A JSON tree.</p>
 *
 * <p>This class implements the {@linkplain JsonNode JSON node} interface using
 * reusable data structures.</p>
 *
 * <p>Of the JSON node interface operations, {@link JsonNode#get(String)} and
 * {@link JsonNode#path(String)} run in O(n) time. All other operations run in
 * linear time.</p>
 *
 * <p>{@link JsonTreeConfig} specifies the minimum and maximum capacity for a
 * JSON tree's internal data structures. When needed, a JSON tree grows their
 * capacity from the minimum up to the maximum. An attempt to exceed the
 * maximum results in a {@link JsonMappingException}.</p>
 */
public class JsonTree {

    private enum ContainerType {
        ARRAY,
        OBJECT,
    }

    private final int maxNodeCapacity;

    private final int maxNestingCapacity;

    private final int minFieldNameCapacity;
    private final int maxFieldNameCapacity;

    private final int minTextValueCapacity;
    private final int maxTextValueCapacity;

    private Node[] nodes;

    private int[] nextSiblingIndexes;

    private int depth;

    private int[] containerStartIndexes;

    private Node currentContainerNode;

    private ContainerType currentContainerType;

    private JsonToken previousToken;

    /**
     * Construct a new instance using the default configuration.
     */
    public JsonTree() {
        this(JsonTreeConfig.DEFAULTS);
    }

    /**
     * Construct a new instance using a custom configuration.
     *
     * @param config the configuration
     */
    public JsonTree(final JsonTreeConfig config) {
        maxNodeCapacity = config.getMaxNodeCapacity();

        maxNestingCapacity = config.getMaxNestingCapacity();

        minFieldNameCapacity = config.getMinFieldNameCapacity();
        maxFieldNameCapacity = config.getMaxFieldNameCapacity();

        minTextValueCapacity = config.getMinTextValueCapacity();
        maxTextValueCapacity = config.getMaxTextValueCapacity();

        nodes = new Node[config.getMinNodeCapacity()];

        for (int i = 0; i < nodes.length; i++)
            nodes[i] = new Node(i);

        nextSiblingIndexes = new int[config.getMinNodeCapacity()];

        containerStartIndexes = new int[config.getMinNestingCapacity()];

        currentContainerNode = null;

        currentContainerType = null;

        previousToken = null;

        nodes[0].type = Type.MISSING;
    }

    /**
     * <p>Reset this instance.</p>
     *
     * <p><strong>Note.</strong> This method invalidates all JSON nodes
     * obtained through prior invocations of this method or the
     * {@link #getRoot()} method.</p>
     *
     * @param parser a JSON parser
     * @return the root JSON node
     * @throws IOException if an I/O error occurs
     */
    public JsonNode reset(final JsonParser parser) throws IOException {
        int index = 0;

        depth = 0;

        currentContainerNode = null;

        currentContainerType = null;

        previousToken = null;

        for (int i = 0; i < nextSiblingIndexes.length; i++)
            nextSiblingIndexes[i] = i + 1;

        while (true) {
            JsonToken currentToken = parser.nextToken();
            if (currentToken == null)
                break;

            CharSequence currentName = parser.currentName();

            switch (currentToken) {
            case END_ARRAY:
                handleEndStruct(index, ContainerType.ARRAY);
                break;
            case END_OBJECT:
                handleEndStruct(index, ContainerType.OBJECT);
                break;
            case FIELD_NAME:
                handleFieldName(previousToken);
                break;
            case START_ARRAY:
                handleValue();
                set(index, currentName, Type.ARRAY);
                handleStartStruct(index++, ContainerType.ARRAY);
                break;
            case START_OBJECT:
                handleValue();
                set(index, currentName, Type.OBJECT);
                handleStartStruct(index++, ContainerType.OBJECT);
                break;
            case VALUE_FALSE:
                handleValue();
                set(index++, currentName, Type.BOOLEAN_FALSE);
                break;
            case VALUE_NULL:
                handleValue();
                set(index++, currentName, Type.NULL);
                break;
            case VALUE_NUMBER_FLOAT:
                handleValue();
                set(index++, currentName, parser.getDoubleValue());
                break;
            case VALUE_NUMBER_INT:
                handleValue();
                set(index++, currentName, parser.getLongValue());
                break;
            case VALUE_STRING:
                handleValue();
                set(index++, currentName, parser.getText());
                break;
            case VALUE_TRUE:
                handleValue();
                set(index++, currentName, Type.BOOLEAN_TRUE);
                break;
            }

            previousToken = currentToken;
        }

        if (currentContainerType != null)
            missingEndStruct(currentContainerType);

        set(index, Type.MISSING);

        return nodes[0];
    }

    private void increaseNestingCapacity() throws JsonMappingException {
        int currentNestingCapacity = containerStartIndexes.length;

        int newNestingCapacity = Math.min(2 * currentNestingCapacity, maxNestingCapacity);

        if (newNestingCapacity == currentNestingCapacity)
            mappingError("Maximum nesting capacity exceeded");

        containerStartIndexes = Arrays.copyOf(containerStartIndexes, newNestingCapacity);
    }

    private void increaseNodeCapacity() throws JsonMappingException {
        int currentNodeCapacity = nodes.length;

        int newNodeCapacity = Math.min(2 * currentNodeCapacity, maxNodeCapacity);

        if (newNodeCapacity == currentNodeCapacity)
            mappingError("Maximum node capacity exceeded");

        nodes = Arrays.copyOf(nodes, newNodeCapacity);

        for (int i = currentNodeCapacity; i < newNodeCapacity; i++)
            nodes[i] = new Node(i);

        nextSiblingIndexes = Arrays.copyOf(nextSiblingIndexes, newNodeCapacity);

        for (int i = currentNodeCapacity; i < newNodeCapacity; i++)
            nextSiblingIndexes[i] = i + 1;
    }

    private void handleStartStruct(final int index, final ContainerType containerType) throws JsonMappingException {
        if (depth == containerStartIndexes.length)
            increaseNestingCapacity();

        containerStartIndexes[depth++] = index;

        currentContainerNode = nodes[index];

        currentContainerNode.size = 0;

        currentContainerType = containerType;
    }

    private void handleEndStruct(final int index, final ContainerType containerType) throws JsonMappingException {
        if (currentContainerType != containerType)
            unexpectedEndStruct(containerType);

        if (containerType == ContainerType.OBJECT && previousToken == JsonToken.FIELD_NAME)
            missingValue();

        int containerStartIndex = containerStartIndexes[--depth];

        if (depth == 0) {
            currentContainerNode = null;

            currentContainerType = null;
        }
        else {
            currentContainerNode = nodes[containerStartIndexes[depth - 1]];

            currentContainerType = currentContainerNode.type.getContainerType();
        }

        nextSiblingIndexes[containerStartIndex] = index;
    }

    private void handleFieldName(final JsonToken previousToken) throws JsonMappingException {
        if (currentContainerType != ContainerType.OBJECT)
            unexpectedFieldName();

        if (previousToken == JsonToken.FIELD_NAME)
            unexpectedFieldName();
    }

    private void handleValue() throws JsonMappingException {
        if (currentContainerNode == null)
            return;

        if (currentContainerType == ContainerType.OBJECT && previousToken != JsonToken.FIELD_NAME)
            missingFieldName();

        currentContainerNode.size++;
    }

    private Node get(final int index) throws JsonMappingException {
        if (index == nodes.length)
            increaseNodeCapacity();

        return nodes[index];
    }

    private void set(final int index, final Type type) throws JsonMappingException {
        Node node = get(index);

        node.type = type;
    }

    private void set(final int index, final CharSequence currentName,
            final Type type) throws JsonMappingException {
        Node node = get(index);

        node.type = type;

        node.setFieldName(currentName);
    }

    private void set(final int index, final CharSequence currentName,
            final double doubleValue) throws JsonMappingException {
        Node node = get(index);

        node.type = Type.NUMBER_DOUBLE;

        node.setFieldName(currentName);

        node.doubleValue = doubleValue;
        node.longValue = (long)doubleValue;
    }

    private void set(final int index, final CharSequence currentName,
            final long longValue) throws JsonMappingException {
        Node node = get(index);

        node.type = Type.NUMBER_LONG;

        node.setFieldName(currentName);

        node.doubleValue = (double)longValue;
        node.longValue = longValue;
    }

    private void set(final int index, final CharSequence currentName,
            final CharSequence text) throws JsonMappingException {
        Node node = get(index);

        node.type = Type.STRING;

        node.setFieldName(currentName);
        node.setTextValue(text);
    }

    /**
     * Get the root JSON node.
     *
     * @return the root JSON node
     */
    public JsonNode getRoot() {
        return nodes[0];
    }

    private enum Type {
        ARRAY(JsonNodeType.ARRAY, ContainerType.ARRAY, false),
        BOOLEAN_TRUE(JsonNodeType.BOOLEAN, null, true),
        BOOLEAN_FALSE(JsonNodeType.BOOLEAN, null, true),
        MISSING(JsonNodeType.MISSING, null, false),
        NULL(JsonNodeType.NULL, null, true),
        NUMBER_DOUBLE(JsonNodeType.NUMBER, null, true),
        NUMBER_LONG(JsonNodeType.NUMBER, null, true),
        OBJECT(JsonNodeType.OBJECT, ContainerType.OBJECT, false),
        STRING(JsonNodeType.STRING, null, true);

        private final JsonNodeType nodeType;
        private final ContainerType containerType;

        private final boolean isValueNode;

        private Type(final JsonNodeType nodeType, final ContainerType containerType, final boolean isValueNode) {
            this.nodeType = nodeType;
            this.containerType = containerType;

            this.isValueNode = isValueNode;
        }

        JsonNodeType getNodeType() {
            return nodeType;
        }

        ContainerType getContainerType() {
            return containerType;
        }

        boolean isValueNode() {
            return isValueNode;
        }
    }

    private class Node implements JsonNode {

        final int index;

        Type type;

        int size;

        final StringBuilder fieldName;

        double doubleValue;
        long longValue;

        final StringBuilder textValue;

        Node(final int index) {
            this.index = index;

            this.fieldName = new StringBuilder(minFieldNameCapacity);

            this.textValue = new StringBuilder(minTextValueCapacity);
        }

        void setFieldName(final CharSequence value) throws JsonMappingException {
            fieldName.setLength(0);

            if (value == null)
                return;

            if (value.length() > maxFieldNameCapacity)
                tooLongFieldName();

            fieldName.append(value);
        }

        void setTextValue(final CharSequence value) throws JsonMappingException {
            if (value.length() > maxTextValueCapacity)
                tooLongTextValue();

            textValue.setLength(0);
            textValue.append(value);
        }

        @Override
        public boolean isMissingNode() {
            return type == Type.MISSING;
        }

        @Override
        public JsonNodeType getNodeType() {
            return type.getNodeType();
        }

        @Override
        public boolean isContainerNode() {
            return type.getContainerType() != null;
        }

        @Override
        public boolean isValueNode() {
            return type.isValueNode();
        }

        @Override
        public boolean isArray() {
            return type == Type.ARRAY;
        }

        @Override
        public boolean isBoolean() {
            return type == Type.BOOLEAN_TRUE || type == Type.BOOLEAN_FALSE;
        }

        @Override
        public boolean isDouble() {
            return type == Type.NUMBER_DOUBLE;
        }

        @Override
        public boolean isLong() {
            return type == Type.NUMBER_LONG;
        }

        @Override
        public boolean isNull() {
            return type == Type.NULL;
        }

        @Override
        public boolean isObject() {
            return type == Type.OBJECT;
        }

        @Override
        public boolean isTextual() {
            return type == Type.STRING;
        }

        @Override
        public JsonNode get(final int index) {
            if (type != Type.ARRAY)
                return null;

            if (size == 0)
                return null;

            if (index < 0 || index >= size)
                return null;

            int count = index;

            int currentIndex = this.index + 1;

            while (count > 0) {
                currentIndex = nextSiblingIndexes[currentIndex];

                count--;
            }

            return nodes[currentIndex];
        }

        @Override
        public JsonNode get(final String fieldName) {
            if (type != Type.OBJECT)
                return null;

            if (size == 0)
                return null;

            int count = size;

            int currentIndex = this.index + 1;

            while (count > 0) {
                Node currentNode = nodes[currentIndex];

                if (fieldName.contentEquals(currentNode.fieldName))
                    return currentNode;

                currentIndex = nextSiblingIndexes[currentIndex];

                count--;
            }

            return null;
        }

        @Override
        public JsonNode path(final int index) {
            JsonNode node = get(index);

            return node != null ? node : MissingNode.INSTANCE;
        }

        @Override
        public JsonNode path(final String fieldName) {
            JsonNode node = get(fieldName);

            return node != null ? node : MissingNode.INSTANCE;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean booleanValue() {
            return type == Type.BOOLEAN_TRUE;
        }

        @Override
        public double doubleValue() {
            return doubleValue;
        }

        @Override
        public long longValue() {
            return longValue;
        }

        @Override
        public CharSequence textValue() {
            return type == Type.STRING ? textValue : null;
        }

    }

    private static void missingEndStruct(final ContainerType containerType)
            throws JsonMappingException {
        String struct = containerType == ContainerType.ARRAY ? "array" : "object";

        mappingError("Missing end of " + struct);
    }

    private static void missingFieldName() throws JsonMappingException {
        mappingError("Missing field name");
    }

    private static void missingValue() throws JsonMappingException {
        mappingError("Missing value");
    }

    private static void tooLongFieldName() throws JsonMappingException {
        mappingError("Too long field name");
    }

    private static void tooLongTextValue() throws JsonMappingException {
        mappingError("Too long string value");
    }

    private static void unexpectedEndStruct(final ContainerType containerType)
            throws JsonMappingException {
        String struct = containerType == ContainerType.ARRAY ? "array" : "object";

        mappingError("Unexpected end of " + struct);
    }

    private static void unexpectedFieldName() throws JsonMappingException {
        mappingError("Unexpected field name");
    }

    private static void mappingError(final String message) throws JsonMappingException {
        throw new JsonMappingException(message);
    }

}
