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

package io.github.selcukes.commons.exec;

import io.github.selcukes.commons.exception.CommandException;
import io.github.selcukes.commons.helper.Preconditions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple thread based utility that consumes {@link InputStream} and provides the stream contents
 * as a list of strings.
 * This utility is <b>NOT</b> designed to be re-used. So everytime a stream is to be consumed, a new
 * object of this utility is expected to be created. Attempting to re-use an existing instance of {@link StreamGuzzler}
 * for consuming stream will trigger errors.
 */
public class StreamGuzzler implements Runnable {
    private final List<String> content = new LinkedList<>();
    private InputStream stream;

    public StreamGuzzler(final InputStream stream) {
        this.stream = stream;
    }

    public List<String> getContent() {
        return content;
    }

    @Override
    public void run() {
        Preconditions.checkState(stream != null, "Cannot guzzle an empty/null stream.");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.add(line);
            }
        } catch (IOException exception) {
            throw new CommandException(exception);
        } finally {
            this.stream = null;
        }
    }
}

