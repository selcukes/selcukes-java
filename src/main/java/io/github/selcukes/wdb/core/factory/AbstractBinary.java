package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.core.Environment;
import io.github.selcukes.wdb.core.MirrorUrls;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.exception.DriverPoolException;
import io.github.selcukes.wdb.util.HttpUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.InputStream;
import java.util.Optional;

import static io.github.selcukes.wdb.util.OptionalUtil.orElse;
import static io.github.selcukes.wdb.util.OptionalUtil.unwrap;
import static org.jsoup.Jsoup.parse;

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
    public Environment getBinaryEnvironment() {
        return targetArch.map(arch -> Environment.create(arch.getValue())).orElseGet(Environment::create);
    }

    @Override
    public String getBinaryVersion() {
        return orElse(release, getLatestRelease());
    }


    abstract protected String getLatestRelease();

    protected String getProxy() {

        return unwrap(proxyUrl);
    }
}
