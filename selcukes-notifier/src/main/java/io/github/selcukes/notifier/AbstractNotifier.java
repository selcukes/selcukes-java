/*
 *
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
 *
 */

package io.github.selcukes.notifier;

public abstract class AbstractNotifier implements Notifier {
    private String title;
    private String status;
    private String message;
    private String error;
    private String path;

    @Override
    public Notifier scenarioName(String title) {
        this.title = title;
        return this;
    }

    @Override
    public Notifier scenarioStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public Notifier stepDetails(String stepDetails) {
        this.message = stepDetails;
        return this;
    }

    @Override
    public Notifier errorMessage(String error) {
        this.error = error;
        return this;
    }

    @Override
    public Notifier path(String path) {
        this.path = path;
        return this;
    }

    @Override
    public void pushNotification() {
        pushNotification(title, status, message, error, path);
    }

    public abstract Notifier pushNotification(
            String scenarioTitle, String scenarioStatus, String message, String error, String screenshotPath
    );

}
