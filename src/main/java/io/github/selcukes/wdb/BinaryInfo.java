package io.github.selcukes.wdb;

public class BinaryInfo {
    private String binaryProperty;
    private String binaryPath;

    public BinaryInfo(String binaryProperty, String binaryPath) {
        this.binaryProperty = binaryProperty;
        this.binaryPath = binaryPath;
    }
    public String getBinaryProperty()
    {
        return this.binaryProperty;
    }
    public String getBinaryPath()
    {
        return binaryPath;
    }
}
