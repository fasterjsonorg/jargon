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

import org.fasterjson.jargon.core.JsonProcessingException;

/**
 * Indicates an I/O error in a high-level data binding operation.
 */
public class JsonMappingException extends JsonProcessingException {

    /**
     * Construct a new instance.
     *
     * @param message the message
     */
    public JsonMappingException(final String message) {
        super(message);
    }

}
