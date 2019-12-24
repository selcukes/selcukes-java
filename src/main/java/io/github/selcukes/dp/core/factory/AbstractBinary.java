package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.enums.TargetArch;

import java.util.Optional;

import static io.github.selcukes.dp.util.OptionalUtil.orElse;

abstract class AbstractBinary implements BinaryFactory {
    private Optional<String> release;
    private Optional<TargetArch> targetArch;

    public AbstractBinary(Optional<String> release, Optional<TargetArch> targetArch) {

        this.release = release;
        this.targetArch = targetArch;
    }

    @Override
    public Environment getBinaryEnvironment() {
        return targetArch.map(arch -> Environment.create(arch.getValue())).orElseGet(Environment::create);
    }

    @Override
    public String getBinaryVersion() {
        return orElse(release, getLatestRelease());
    }

    abstract String getLatestRelease();
}
