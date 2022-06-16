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

package io.github.selcukes.core.wait;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.function.BiFunction;
import java.util.function.Function;

@RequiredArgsConstructor
public enum WaitCondition {
    VISIBLE(ExpectedConditions::visibilityOfElementLocated),
    INVISIBLE(ExpectedConditions::invisibilityOfElementLocated),
    CLICKABLE((Function<By, ExpectedCondition<?>>) ExpectedConditions::elementToBeClickable),
    PRESENT(ExpectedConditions::presenceOfElementLocated),
    ALL_VISIBLE(ExpectedConditions::visibilityOfAllElementsLocatedBy),
    ALL_PRESENT(ExpectedConditions::presenceOfAllElementsLocatedBy),
    TEXT_TO_BE((BiFunction<By, String, ExpectedCondition<?>>) ExpectedConditions::textToBe);
    private final BiFunction<?, ?, ExpectedCondition<?>> type;

    @SuppressWarnings("all")
    <T, V> WaitCondition(final Function<T, ExpectedCondition<?>> type) {
        this((T arg1, V arg2) -> type.apply(arg1));
    }

    @SuppressWarnings("unchecked")
    public <T, V, R> BiFunction<T, V, R> getType() {
        return (BiFunction<T, V, R>) type;
    }
}