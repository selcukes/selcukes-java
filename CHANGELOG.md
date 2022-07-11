# Changelog

----

## [Unreleased] (In Git)

### Added

### Changed

### Removed

### Fixed

## [2.1.5] (11-07-2022)

### Fixed

* [DataBind] Fixed Data Rows overlapping issue.

* [JUnit] Fixed Cucumber junit-platform-suite runner ([#162](https://github.com/selcukes/selcukes-java/pull/162))

## [2.1.4] (10-07-2022)

### Added

* [DataBind] Added support to Parse Properties to entityClass([#159](https://github.com/selcukes/selcukes-java/pull/159))

* [DataBind] Added Data Substitutor support for Excel and PropertiesMapper([#160](https://github.com/selcukes/selcukes-java/pull/160))

### Changed

* [Excel Runner] Moved Excel Mapper to `DataBind` module([#153](https://github.com/selcukes/selcukes-java/pull/152))
* Update dependency org.testng:testng to v7.6.1 ([#154](https://github.com/selcukes/selcukes-java/pull/154))
* Update dependency net.masterthought:cucumber-reporting to
  v5.7.2 ([#157](https://github.com/selcukes/selcukes-java/pull/157))
* [Commons] Moved XML Mapper to `DataBind` module([#161](https://github.com/selcukes/selcukes-java/pull/161))

## [2.1.3] (30-06-2022)

### Added

* [Core] Added String Locator support ([#151](https://github.com/selcukes/selcukes-java/pull/151))
* [Excel Runner] Added support to parse Excel sheet to entity
  class ([#152](https://github.com/selcukes/selcukes-java/pull/152))

### Changed

* Update dependency cucumber.version to v7.4.1([#149](https://github.com/selcukes/selcukes-java/pull/149))
* Update dependency selenium.version to v4.3.0([#150](https://github.com/selcukes/selcukes-java/pull/150))

## [2.1.2]

### Added

* [DataBind] Added streamLoader support to load data files using Thread Resource
  Stream ([#139](https://github.com/selcukes/selcukes-java/pull/139))
* [Core] Added support to handle Exceptions on the test
  failures ([#139](https://github.com/selcukes/selcukes-java/pull/139))
* [Core]  Added `Pages` class to quick start testing using `Pages.webPage()`, `Pages.winPage()`
  , `Pages.mobilePage()` ([#139](https://github.com/selcukes/selcukes-java/pull/139))
* [Core] Added `ApiPage` class to perform API testing
  using `Pages.apiPage()` ([#141](https://github.com/selcukes/selcukes-java/pull/141))
* [Core] Added TestLifecycle testng listeners for better instance
  management ([#145](https://github.com/selcukes/selcukes-java/pull/145))

### Changed

* Update dependency cucumber.version to v7.4.0([#147](https://github.com/selcukes/selcukes-java/pull/147))

### Removed

* [WebDriver Binaries] Removed `org.jsoup` ([#142](https://github.com/selcukes/selcukes-java/pull/142)) in favour of XML
  Mapper ([#144](https://github.com/selcukes/selcukes-java/pull/144))

### Fixed

* [Core] Selcukes test properties thread safety

## [2.1.1]

### Fixed

* [Core] Fixed issue initiate local browser
* [Reports] Fixed attaching screenshot for testng

## [2.1.0]

### Added

* [Reports] Native mobile video and screenshot support  ([#133](https://github.com/selcukes/selcukes-java/pull/133))

### Changed

* Update dependency selenium.version to v4.2.2([#138](https://github.com/selcukes/selcukes-java/pull/138))
* Update dependency io.appium:java-client to v8.1.1 ([#137](https://github.com/selcukes/selcukes-java/pull/137))

### Fixed

* [Core] Add Support to build Multipart Http Request  ([#135](https://github.com/selcukes/selcukes-java/pull/135))
* [Reports]Enable on demand Video Recording option for Mobile
  Tests ([#131](https://github.com/selcukes/selcukes-java/pull/131))
* [Reports]Desktop Automation-Local Appium Server is not started when mobile configured as
  Cloud ([#130](https://github.com/selcukes/selcukes-java/pull/130))

## [2.0.3]

### Added

* [Core] Added BrowserStack Support ([#129](https://github.com/selcukes/selcukes-java/pull/129))

### Changed

* Update dependency selenium.version to v4.2.1([#128](https://github.com/selcukes/selcukes-java/pull/128))
* Update dependency net.masterthought:cucumber-reporting to
  v5.7.1 ([#123](https://github.com/selcukes/selcukes-java/pull/123))
* Update dependency io.appium:java-client to v8.1.0 ([#126](https://github.com/selcukes/selcukes-java/pull/126))

### Fixed

* [Core] Replaced deprecated DesiredCapabilities with Options classes
  video ([#127](https://github.com/selcukes/selcukes-java/pull/127))

## [2.0.2]

### Added

* [Video Recorder] Added support to generate MONTE recorder as MP4
  video ([#109](https://github.com/selcukes/selcukes-java/pull/109))
* [Selcukes Reports] Real-time reporting for cucumber tests using Elastic Search and
  Kibana ([#119](https://github.com/selcukes/selcukes-java/pull/119))
* [Core] Added Page Validations and Wait Wrappers ([#113](https://github.com/selcukes/selcukes-java/pull/113))
* [Core] Added Alert and Frame Actions to Page ([#122](https://github.com/selcukes/selcukes-java/pull/122))

### Changed

* Update dependency com.fasterxml.jackson.dataformat to
  v2.13.3 ([#114](https://github.com/selcukes/selcukes-java/pull/114))
* Update dependency org.jsoup:jsoup to v1.15.1 ([#115](https://github.com/selcukes/selcukes-java/pull/115))
* Update dependency org.testng:testng to v7.6.0 ([#116](https://github.com/selcukes/selcukes-java/pull/116))

### Removed

* [Core] Removed Apache HttpClient instead using Java
  HttpClient ([#121](https://github.com/selcukes/selcukes-java/pull/121))

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
