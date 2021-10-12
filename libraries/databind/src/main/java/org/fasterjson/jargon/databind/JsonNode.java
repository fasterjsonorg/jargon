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

/**
 * A JSON node.
 */
public interface JsonNode {

    /**
     * Returns true if this is a missing node.
     *
     * @return true if this is a missing node, otherwise false
     */
    boolean isMissingNode();

    /**
     * Get the node type.
     *
     * @return the node type
     */
    JsonNodeType getNodeType();

    /**
     * Returns true if this is an array node or an object node.
     *
     * @return true if this is an array node or an object node, otherwise
     *     false
     */
    boolean isContainerNode();

    /**
     * Returns true if this is a value node.
     *
     * @return true if this is a value node, otherwise false
     */
    boolean isValueNode();

    /**
     * Returns true if this is an array node.
     *
     * @return true if this is an array node, otherwise false
     */
    boolean isArray();

    /**
     * Returns true if this is a boolean node.
     *
     * @return true if this is a boolean node, otherwise false
     */
    boolean isBoolean();

    /**
     * Returns true if this is a number node with a double value.
     *
     * @return true if this is a number node with a double value, otherwise
     *     false
     */
    boolean isDouble();

    /**
     * Returns true if this is a number node with a long value.
     *
     * @return true if this is a number node with a long value, otherwise false
     */
    boolean isLong();

    /**
     * Returns true if this is a null value.
     *
     * @return true if this is a null node, otherwise false
     */
    boolean isNull();

    /**
     * Returns true if this is an object node.
     *
     * @return true if this is an object node, otherwise false
     */
    boolean isObject();

    /**
     * Returns true if this is a string node.
     *
     * @return true if this is a string node, otherwise false
     */
    boolean isTextual();

    /**
     * Get the node at the specified index. This method returns {@code null}
     * if this is not an array node or the array does not have an element with
     * the specified index.
     *
     * @param index the index
     * @return the node at the specified index or {@code null} if such does not
     *     exist
     * @see #path(int)
     */
    JsonNode get(int index);

    /**
     * Get the node with the specified field name. This method returns
     * {@code null} if this is not an object node or the object does not have
     * an element with the specified field name.
     *
     * @param fieldName the field name
     * @return the node with the specified field name or {@code null} if such
     *     does not exist
     * @see #path(String)
     */
    JsonNode get(String fieldName);

    /**
     * Get the node at the specified index. This method returns a missing node
     * if this is not an array node or the array does not have an element with
     * the specified index.
     *
     * @param index the index
     * @return the node at the specified index or a missing node if such does
     *     not exist
     * @see #get(int)
     * @see #isMissingNode()
     */
    JsonNode path(int index);

    /**
     * Get the node with the specified field name. This method returns a
     * missing node if this is not an object node or the object does not have
     * an element with the specified field name.
     *
     * @param fieldName the field name
     * @return the node with the specified field name or a missing node if such
     *     does not exist
     * @see #get(String)
     */
    JsonNode path(String fieldName);

    /**
     * Get the number of child nodes. For an array node, returns the number of
     * elements in the array. For an object node, returns the number of fields
     * in the object. For other node types, returns zero.
     *
     * @return the number of child nodes
     */
    int size();

    /**
     * Get the boolean value. For a boolean node, returns the corresponding
     * value. For other node types, returns false.
     *
     * @return the boolean value
     */
    boolean booleanValue();

    /**
     * Get the double value. For a number node, returns the corresponding
     * double value. For other node types, returns 0.0.
     *
     * @return the double value
     */
    double doubleValue();

    /**
     * Get the long value. For a number node, returns the corresponding long
     * value. For other node types, returns 0.
     *
     * @return the long value
     */
    long longValue();

    /**
     * Get the string value. For a string node, returns the corresponding
     * value. For other node types, returns {@code null}.
     *
     * @return the string value
     */
    CharSequence textValue();

}
