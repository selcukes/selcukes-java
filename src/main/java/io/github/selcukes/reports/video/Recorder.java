/*
 *
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
 *
 */

package io.github.selcukes.reports.video;

import java.io.File;

public interface Recorder {
    /**
     * This method will start the recording of the execution.
     */
    void start();

    /**
     * This method will stop and save's the recording.
     */
    File stopAndSave(String filename);

    /**
     * This method will delete the recorded file,if the test is pass.
     */
    void stopAndDelete();
}
