package io.github.selcukes.dp.core;


/**
 * The type Binary info.
 */
public class BinaryInfo {
    private String binaryName;

    /**
     * Instantiates a new Binary info.
     *
     * @param binaryName     the binary name
     * @param binaryVersion  the binary version
     * @param binaryLocation the binary location
     */
    public BinaryInfo(String binaryName, String binaryVersion, String binaryLocation) {
        this.binaryName = binaryName;
        this.binaryVersion = binaryVersion;
        this.binaryLocation = binaryLocation;
    }

    private String binaryVersion;
    private String binaryLocation;

    /**
     * Gets binary name.
     *
     * @return the binary name
     */
    public String getBinaryName() {
        return binaryName;
    }

    /**
     * Sets binary name.
     *
     * @param binaryName the binary name
     */
    public void setBinaryName(String binaryName) {
        this.binaryName = binaryName;
    }

    /**
     * Gets binary version.
     *
     * @return the binary version
     */
    public String getBinaryVersion() {
        return binaryVersion;
    }

    /**
     * Sets binary version.
     *
     * @param binaryVersion the binary version
     */
    public void setBinaryVersion(String binaryVersion) {
        this.binaryVersion = binaryVersion;
    }

    /**
     * Gets binary location.
     *
     * @return the binary location
     */
    public String getBinaryLocation() {
        return binaryLocation;
    }

    /**
     * Sets binary location.
     *
     * @param binaryLocation the binary location
     */
    public void setBinaryLocation(String binaryLocation) {
        this.binaryLocation = binaryLocation;
    }



}