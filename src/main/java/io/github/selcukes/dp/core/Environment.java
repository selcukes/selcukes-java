package io.github.selcukes.dp.core;

import io.github.selcukes.dp.enums.OsType;
import io.github.selcukes.dp.util.OsUtil;

/**
 * The type Environment.
 */
public class Environment {
    private int archType;

    private Environment() {
    }

    private Environment(int archType) {
        this.archType = archType;
    }

    /**
     * Create environment.
     *
     * @return the environment
     */
    public static Environment create() {
        return new Environment();
    }

    /**
     * Create environment.
     *
     * @param archType the arch type
     * @return the environment
     */
    public static Environment create(int archType) {
        return new Environment(archType);
    }

    /**
     * Gets os type.
     *
     * @return the os type
     */
    public OsType getOsType() {
        return OsUtil.getOsType();
    }

    /**
     * Gets architecture.
     *
     * @return the architecture
     */
    public int getArchitecture() {
        return archType != 0 ? archType : OsUtil.getOsArch();
    }

    /**
     * Gets os name and arch.
     *
     * @return the os name and arch
     */
    public String getOsNameAndArch() {
        return (getOsType().name() + getArchitecture()).toLowerCase();
    }
}