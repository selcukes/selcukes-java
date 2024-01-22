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

package io.github.selcukes.notifier.mail;

import io.github.selcukes.commons.helper.Singleton;

/**
 * An interface for managing the sending of emails.
 * <p>
 * Implementations of this interface provide methods to send emails with
 * specific subject, body, and optional attachments.
 * </p>
 * <p>
 * Implementing classes should adhere to the contract specified by this
 * interface.
 * </p>
 */
public interface EmailNotifier {

    /**
     * Returns a singleton instance of the default implementation of
     * {@code EmailNotifier}.
     * <p>
     * The default implementation is provided by the {@code EmailNotifierImpl}
     * class.
     * </p>
     *
     * @return A singleton instance of {@code EmailNotifier}.
     */
    static EmailNotifier emailNotifier() {
        return Singleton.instanceOf(EmailNotifierImpl.class);
    }

    /**
     * Sends an email with the specified subject, body, and optional
     * attachments.
     *
     * @param subject     The subject of the email.
     * @param body        The body content of the email.
     * @param attachments Array of file paths to be attached to the email.
     */
    void sendMail(String subject, String body, String... attachments);

}
