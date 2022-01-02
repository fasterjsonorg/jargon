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

/**
 * A random character access source. A source of this kind provides efficient
 * access to any character in the input at once.
 */
public interface RandomCharAccessSource {

    /**
     * Get the character at the specified index.
     *
     * @param index the index
     * @return the character at the specified index
     */
    char charAt(int index);

    /**
     * Get the length.
     *
     * @return the length
     */
    int length();

}
