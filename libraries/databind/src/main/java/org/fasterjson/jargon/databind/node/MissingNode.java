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
package org.fasterjson.jargon.databind.node;

import org.fasterjson.jargon.databind.JsonNode;
import org.fasterjson.jargon.databind.JsonNodeType;

/**
 * The missing node.
 */
public class MissingNode implements JsonNode {

    /**
     * The singleton instance of the missing node.
     */
    public static final MissingNode INSTANCE = new MissingNode();

    private MissingNode() {
    }

    @Override
    public boolean isMissingNode() {
        return true;
    }

    @Override
    public JsonNodeType getNodeType() {
        return JsonNodeType.MISSING;
    }

    @Override
    public boolean isContainerNode() {
        return false;
    }

    @Override
    public boolean isValueNode() {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public boolean isLong() {
        return false;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public boolean isTextual() {
        return false;
    }

    @Override
    public JsonNode get(final int index) {
        return null;
    }

    @Override
    public JsonNode get(final String fieldName) {
        return null;
    }

    @Override
    public JsonNode path(final int index) {
        return INSTANCE;
    }

    @Override
    public JsonNode path(final String fieldName) {
        return INSTANCE;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean booleanValue() {
        return false;
    }

    @Override
    public double doubleValue() {
        return 0.0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public CharSequence textValue() {
        return null;
    }

}
