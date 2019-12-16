package io.github.selcukes.dp.core;


public class BinaryInfo {
    private String binaryName;
    private String binaryVersion;
    private String binaryLocation;

    public BinaryInfo(String binaryName, String binaryVersion, String binaryLocation) {
        this.binaryName = binaryName;
        this.binaryVersion = binaryVersion;
        this.binaryLocation = binaryLocation;
    }


    public String getBinaryName() {
        return binaryName;
    }

    public void setBinaryName(String binaryName) {
        this.binaryName = binaryName;
    }

    public String getBinaryVersion() {
        return binaryVersion;
    }

    public void setBinaryVersion(String binaryVersion) {
        this.binaryVersion = binaryVersion;
    }

    public String getBinaryLocation() {
        return binaryLocation;
    }

    public void setBinaryLocation(String binaryLocation) {
        this.binaryLocation = binaryLocation;
    }


}