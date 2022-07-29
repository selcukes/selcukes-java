/*
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.selcukes.wdb.version;

public class VersionTokenizer {
    private final String versionString;
    private final int length;

    private int position;
    private int number;
    private String suffix;
    private boolean hasValue;

    public VersionTokenizer(final String versionString) {
        if (versionString == null) {
            throw new IllegalArgumentException("Version String is null");
        }

        this.versionString = versionString;
        length = versionString.length();
    }

    public int getNumber() {
        return number;
    }

    public String getSuffix() {
        return suffix;
    }

    public boolean hasValue() {
        return hasValue;
    }

    public boolean moveNext() {
        number = 0;
        suffix = "";
        hasValue = false;

        // No more characters
        if (position >= length) {
            return false;
        }

        hasValue = true;

        while (position < length) {
            char c = versionString.charAt(position);
            if (c < '0' || c > '9') {
                break;
            }
            number = number * 10 + (c - '0');
            position++;
        }

        int suffixStart = position;

        while (position < length) {
            char c = versionString.charAt(position);
            if (c == '.') {
                break;
            }
            position++;
        }

        suffix = versionString.substring(suffixStart, position);

        if (position < length) {
            position++;
        }

        return true;
    }
}
