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

/**
 * The DevicePool class represents a collection of devices grouped by their
 * device type.
 * <p>
 * Each device is represented as an {@code Object}, which can be a
 * {@code WebDriver} instance or any other type of device object. The pool
 * maintains a mapping between the device objects and their device types, and
 * allows devices to be added, removed, and retrieved based on their device type
 * and index.
 * </p>
 * <p>
 * The DevicePool class is thread-safe, allowing multiple threads to access and
 * modify the pool concurrently.
 * </p>
 */
public class DevicePool {
    /**
     * A map that associates each device type with a list of devices of that
     * type. The list of devices is stored as a {@code CopyOnWriteArrayList},
     * which is thread-safe and allows concurrent reads and writes without the
     * need for explicit synchronization.
     */
    private final Map<DeviceType, List<Object>> devicesByType;
    /**
     * A map that associates each device object with its corresponding device
     * type. The map is stored as a {@code ConcurrentHashMap}, which is
     * thread-safe and allows concurrent reads and writes without the need for
     * explicit synchronization.
     */
    private final Map<Object, DeviceType> devicesByObject;

    /**
     * Creates a new instance of the {@code DevicePool} class. Initializes the
     * {@code devicesByType} and {@code devicesByObject} maps with thread-safe
     * implementations.
     */
    public DevicePool() {
        devicesByType = new ConcurrentHashMap<>();
        devicesByObject = new ConcurrentHashMap<>();
    }

    /**
     * Adds a device to the device pool for the specified device type.
     * <p>
     * The device is represented as an {@code Object}, which can be a
     * {@code WebDriver} instance or any other type of device object. The device
     * is added to the end of the list of devices for the specified device type.
     *
     * @param deviceType the device type of the device to be added
     * @param device     the device object to be added
     */
    public synchronized void addDevice(DeviceType deviceType, Object device) {
        devicesByType.computeIfAbsent(deviceType, k -> new CopyOnWriteArrayList<>()).add(device);
        devicesByObject.put(device, deviceType);
    }

    /**
     * Returns a list of devices of the given type in the pool.
     *
     * @param  deviceType the type of devices being retrieved
     * @return            a list of devices of the given type in the pool, or an
     *                    empty list if there are no devices of that type
     */
    public synchronized List<Object> getDevices(DeviceType deviceType) {
        return devicesByType.getOrDefault(deviceType, Collections.emptyList());
    }

    /**
     * Returns the device object at the specified index in the list of devices
     * of the given type. If the index is out of range, an
     * {@code IndexOutOfBoundsException} is thrown.
     *
     * @param  deviceType                the type of device being retrieved
     * @param  index                     the index of the device in the list of
     *                                   devices
     * @return                           the device object at the specified
     *                                   index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public synchronized Object getDevice(DeviceType deviceType, int index) {
        var deviceList = getDevices(deviceType);
        if (index >= 0 && index < deviceList.size()) {
            return deviceList.get(index);
        } else {
            throw new IndexOutOfBoundsException(
                String.format("Invalid device index %d for device type %s", index, deviceType));
        }
    }

    /**
     * Returns a map of all device types to their corresponding lists of devices
     * in the pool.
     *
     * @return a map of all device types to their corresponding lists of devices
     *         in the pool
     */
    public synchronized Map<DeviceType, List<Object>> getAllDevices() {
        return Collections.unmodifiableMap(devicesByType);
    }

    /**
     * Removes a device from the pool.
     *
     * @param device the device being removed
     */
    public synchronized void removeDevice(Object device) {
        var deviceType = devicesByObject.get(device);
        if (deviceType != null) {
            devicesByType.get(deviceType).remove(device);
            devicesByObject.remove(device);
        }
    }

    /**
     * Checks if the pool has a device of the given type and with the given
     * reference.
     *
     * @param  deviceType the type of the device being checked
     * @param  device     the device being checked
     * @return            true if the pool has a device of the given type and
     *                    with the given reference, false otherwise
     */
    public boolean hasDevice(final DeviceType deviceType, final Object device) {
        return getDevices(deviceType).contains(device);
    }

}
