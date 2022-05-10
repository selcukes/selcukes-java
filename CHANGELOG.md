# Changelog

----

## [Unreleased] (In Git)

### Added

### Changed

### Removed

### Fixed

## [2.0.1]

### Added

* [Core] Added Touch Actions Wrappers ([#107](https://github.com/selcukes/selcukes-java/pull/107))

### Fixed

* [Core] Fixed Event Capturing issue for mobile drivers ([#108](https://github.com/selcukes/selcukes-java/pull/108))
* [Core] Fixed Initiating new Local browser session issue

## [2.0.0]

### Added

* [Core] Use Selcukes.yaml file for all configs ([#105](https://github.com/selcukes/selcukes-java/pull/105))
* [Core] Add flexibility to update properties in runtime for different
  tests ([#95](https://github.com/selcukes/selcukes-java/pull/95))
* [Selcukes Core] Parallel execution support fixes ([#99](https://github.com/selcukes/selcukes-java/pull/99))
* [Selcukes DataBind] `DataMapper.write` method will create a new data file if not
  exist([#102](https://github.com/selcukes/selcukes-java/pull/102))
* [WebDriver Binaries] Added Cache binary management using `version.properties` in WebDrivers
  folder ([#104](https://github.com/selcukes/selcukes-java/pull/104))

### Changed

* [Selcukes Extent Report] Extent Adapter will attach logs without additional configuration in Hooks
  class ([#95](https://github.com/selcukes/selcukes-java/pull/95))
* [WebDriver Binaries] By default, WebDriverBinary setup method will look for Browser version using commands. This can
  be disabled using `disableAutoCheck` method. ([#97](https://github.com/selcukes/selcukes-java/pull/97))
* Updated dependency selenium.version to v4.1.4 ([#98](https://github.com/selcukes/selcukes-java/pull/98))
* Updated dependency cucumber.version to v7.3.4 ([#103](https://github.com/selcukes/selcukes-java/pull/103))

### Removed

* [Selcukes TestNG] Removed selcukes-test properties file support instead use `selcukes.yaml`
  file ([#105](https://github.com/selcukes/selcukes-java/pull/105))

### Fixed

* [Selcukes DataBind] Fixed Reading data files from root folder
  issue ([#102](https://github.com/selcukes/selcukes-java/pull/102))
* [Core] Allow user to start selenium server manually

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