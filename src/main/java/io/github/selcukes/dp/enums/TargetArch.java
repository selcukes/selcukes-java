package io.github.selcukes.dp.enums;

/**
 * The enum Target arch.
 */
public enum TargetArch {
    /**
     * X 86 target arch.
     */
    X86(32),
    /**
     * X 64 target arch.
     */
    X64(64);

    /**
     * The Value.
     */
    int value;

    TargetArch(int value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }
}