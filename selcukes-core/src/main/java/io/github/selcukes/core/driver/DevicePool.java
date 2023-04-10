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

package io.github.selcukes.core.driver;

import io.github.selcukes.core.enums.DeviceType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DevicePool {
    private final Map<DeviceType, List<Object>> devicesByType;
    private final Map<Object, DeviceType> devicesByObject;

    public DevicePool() {
        devicesByType = new ConcurrentHashMap<>();
        devicesByObject = new ConcurrentHashMap<>();
    }

    public synchronized void addDevice(DeviceType deviceType, Object device) {
        devicesByType.computeIfAbsent(deviceType, k -> new CopyOnWriteArrayList<>()).add(device);
        devicesByObject.put(device, deviceType);
    }

    public synchronized List<Object> getDevices(DeviceType deviceType) {
        return devicesByType.getOrDefault(deviceType, Collections.emptyList());
    }

    public synchronized Object getDevice(DeviceType deviceType, int index) {
        var deviceList = getDevices(deviceType);
        if (index >= 0 && index < deviceList.size()) {
            return deviceList.get(index);
        } else {
            throw new IndexOutOfBoundsException(
                String.format("Invalid device index %d for device type %s", index, deviceType));
        }
    }

    public synchronized Map<DeviceType, List<Object>> getAllDevices() {
        return Collections.unmodifiableMap(devicesByType);
    }

    public synchronized void removeDevice(Object device) {
        var deviceType = devicesByObject.get(device);
        if (deviceType != null) {
            devicesByType.get(deviceType).remove(device);
            devicesByObject.remove(device);
        }
    }
}
