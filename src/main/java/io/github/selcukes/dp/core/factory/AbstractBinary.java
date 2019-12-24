package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.enums.TargetArch;

import java.util.Optional;

import static io.github.selcukes.dp.util.OptionalUtil.orElse;
import static io.github.selcukes.dp.util.OptionalUtil.unwrap;

abstract class AbstractBinary implements BinaryFactory {
    private Optional<String> release;
    private Optional<TargetArch> targetArch;
    private Optional<String> proxyUrl;

    public AbstractBinary(String release, TargetArch targetArch, String proxyUrl) {

        this.release = Optional.ofNullable(release);
        this.targetArch = Optional.ofNullable(targetArch);
        this.release = Optional.ofNullable(proxyUrl);
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

    String getProxy() {

        return unwrap(proxyUrl);
    }
}
