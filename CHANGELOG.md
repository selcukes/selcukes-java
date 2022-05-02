# Changelog

----

## [Unreleased] (In Git)

### Added

* [All] Add flexibility to update properties in runtime for different
  tests ([#95](https://github.com/selcukes/selcukes-java/pull/95))
* [Selcukes Core] Parallel execution support fixes ([#99](https://github.com/selcukes/selcukes-java/pull/99))

### Changed

* [Selcukes Extent Report] Extent Adapter will attach logs without additional configuration in Hooks
  class ([#95](https://github.com/selcukes/selcukes-java/pull/95))
* [WebDriver Binaries] By default, WebDriverBinary setup method will look for Browser version using commands. This can
  be disabled using `disableAutoCheck` method. ([#97](https://github.com/selcukes/selcukes-java/pull/97))
* Updated dependency selenium.version to v4.1.4 ([#98](https://github.com/selcukes/selcukes-java/pull/98))

### Removed

### Fixed

## [1.6.0]  (22-04-2022)

### Added

* [Excel Runner] Support excel driven test runner ([#84](https://github.com/selcukes/selcukes-java/pull/84))
* [Selcukes Core] Added AndroidDriver support ([#92](https://github.com/selcukes/selcukes-java/pull/92))

### Changed

* Update dependency org.projectlombok:lombok to v1.18.24 ([#85](https://github.com/selcukes/selcukes-java/pull/85))
* Update dependency cucumber.version to
  v7.3.1 ([#86](https://github.com/selcukes/selcukes-java/pull/86)) ([#89](https://github.com/selcukes/selcukes-java/pull/89))

### Removed

* [WebDriver Binaries] Removed Selenium server binary support. Alternatively use selenium grid maven dependency to run
  grid programmatically

### Fixed

* [Selcukes Core] Fixed Appium Local service issue ([#92](https://github.com/selcukes/selcukes-java/pull/92))
* [Selcukes Core] Fixed launching windows application using
  WinAppDriver ([#92](https://github.com/selcukes/selcukes-java/pull/92))