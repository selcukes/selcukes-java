package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.util.Platform;

import java.util.Optional;

import static io.github.selcukes.wdb.util.OptionalUtil.orElse;
import static io.github.selcukes.wdb.util.OptionalUtil.unwrap;

abstract class AbstractBinary implements BinaryFactory {
    private Optional<String> release;
    private Optional<TargetArch> targetArch;
    private Optional<String> proxyUrl;

    public AbstractBinary(String release, TargetArch targetArch, String proxyUrl) {

        this.release = Optional.ofNullable(release);
        this.targetArch = Optional.ofNullable(targetArch);
        this.proxyUrl = Optional.ofNullable(proxyUrl);
    }

    @Override
    public Platform getBinaryEnvironment() {
        Platform platform = Platform.getPlatform();
        targetArch.ifPresent(arch -> platform.setArchitecture(arch.getValue()));
        return platform;
    }

    @Override
    public String getBinaryVersion() {
        return orElse(release, getLatestRelease());
    }


    protected abstract String getLatestRelease();

    protected String getProxy() {
        return unwrap(proxyUrl);
    }
}
