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

import java.io.IOException;
import org.fasterjson.jargon.core.CharJsonParser;

/**
 * A character source.
 *
 * @see CharJsonParser
 */
public interface CharSource {

    /**
     * Read from the input.
     *
     * @param buffer the destination buffer
     * @param offset the destination offset
     * @return the number of characters read or {@code -1} if there are no more
     *     characters
     * @throws IOException if an I/O error occurs
     */
    int read(char[] buffer, int offset) throws IOException;

}
