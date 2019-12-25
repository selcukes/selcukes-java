package io.github.selcukes.wdb.util;

public class VersionTokenizer {
    private final String versionString;
    private final int length;

    private int position;
    private int number;
    private String suffix;
    private boolean hasValue;

    public int getNumber() {
        return number;
    }

    public String getSuffix() {
        return suffix;
    }

    public boolean hasValue() {
        return hasValue;
    }

    public VersionTokenizer(String versionString) {
        if (versionString == null)
            throw new IllegalArgumentException("Version String is null");

        this.versionString = versionString;
        length = versionString.length();
    }

    public boolean moveNext() {
        number = 0;
        suffix = "";
        hasValue = false;

        // No more characters
        if (position >= length)
            return false;

        hasValue = true;

        while (position < length) {
            char c = versionString.charAt(position);
            if (c < '0' || c > '9') break;
            number = number * 10 + (c - '0');
            position++;
        }

        int suffixStart = position;

        while (position < length) {
            char c = versionString.charAt(position);
            if (c == '.') break;
            position++;
        }

        suffix = versionString.substring(suffixStart, position);

        if (position < length) position++;

        return true;
    }
}
