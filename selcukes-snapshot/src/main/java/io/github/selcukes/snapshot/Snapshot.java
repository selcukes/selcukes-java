/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.snapshot;

public interface Snapshot {
    /**
     * Shoot the page.
     *
     * @return A string.
     */
    String shootPage();

    /**
     * Shoot the visible page and return the image as a base64 encoded string.
     *
     * @return The string "shootVisiblePage" is being returned.
     */
    String shootVisiblePage();

    /**
     * Shoot the page as a byte array.
     *
     * @return A byte array of the page.
     */
    byte[] shootPageAsBytes();

    /**
     * "Shoot the visible page and return the result as a byte array."
     *
     * The function is a bit more complicated than that, but not much. It's a wrapper around the following function:
     *
     * // Java
     * byte[] shootVisiblePageAsBytes(int maxWidth, int maxHeight, int quality);
     *
     * @return A byte array of the visible page.
     */
    byte[] shootVisiblePageAsBytes();

    /**
     * This function returns a new Snapshot object that is identical to the current one, except that it has the address bar
     * enabled.
     *
     * @return A new instance of the class.
     */
    Snapshot withAddressBar();

    /**
     * It returns a snapshot of the current state of the application.
     *
     * @param text The text to be displayed in the notification.
     * @return A new instance of the class.
     */
    Snapshot withText(String text);
}
