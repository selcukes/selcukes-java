# Changelog

----

## [Unreleased] (In Git)

### Added

### Changed

### Fixed

### Removed

## [2.2.10] (07-05-2023)

### Added

* [DataBind] Added support for writing `DataTable` to excel ([#249](https://github.com/selcukes/selcukes-java/pull/249))
* [DataBind] Add support for writing POJOs to Java Properties files and field-level interpolation ([#253](https://github.com/selcukes/selcukes-java/pull/253))
* [DataBind] Added support for reading the folder path from the System property.

### Changed

* Update selenium.version to v4.9.0 ([#248](https://github.com/selcukes/selcukes-java/pull/248))
* Update dependency com.fasterxml.jackson:jackson-bom to v2.15.0  ([#250](https://github.com/selcukes/selcukes-java/pull/250))
* Update dependency org.junit:junit-bom to v5.9.3 ([#251](https://github.com/selcukes/selcukes-java/pull/251))
* Update dependency io.cucumber:cucumber-bom to v7.12.0 ([#252](https://github.com/selcukes/selcukes-java/pull/252))

## [2.2.9] (17-04-2023)

### Added

* [DataBind] Added `DecimalNumber` ([#244](https://github.com/selcukes/selcukes-java/pull/244))

* [Core] Add support for executing tests on multiple devices simultaneously using custom capabilities ([#245](https://github.com/selcukes/selcukes-java/pull/245))

### Changed

* [Commons] Renamed `Response` class to `WebResponse`
* [Commons] Refactored DataBaseDriver to use DataBaseConfig builder to initiate
  connections([#246](https://github.com/selcukes/selcukes-java/pull/246))

### Fixed

* [Core] Fixed issue with desktop switch window ([#247](https://github.com/selcukes/selcukes-java/pull/247))

## [2.2.8] (04-04-2023)

### Changed

* [Internal Changes] Conducted a thorough code cleanup and refactoring to improve the code quality and readability. No
  changes were made to the functionality or features of the software

## [2.2.7] (30-03-2023)

### Added

* [DataBind] Added `Try.class` utility to simplify error
  handling ([#236](https://github.com/selcukes/selcukes-java/pull/236))

### Changed

* Update selenium.version to v4.8.3 ([#239](https://github.com/selcukes/selcukes-java/pull/239))
* Update dependency org.apache.commons:commons-compress to
  v1.23.0 ([#233](https://github.com/selcukes/selcukes-java/pull/233))
* Update dependency io.cucumber:cucumber-bom to v7.11.2 ([#235](https://github.com/selcukes/selcukes-java/pull/235))
* [Extent Reports] Cleanup ExtentService ([#234](https://github.com/selcukes/selcukes-java/pull/234))

### Fixed

* [ExcelRunner] ConcurrentModificationException when calling MultiExcelData.getTestDataAsMap() in
  ExcelReader2 ([#241](https://github.com/selcukes/selcukes-java/pull/241))

### Removed

* [ExcelRunner] Removed `ExcelTestRunner2.java` and added support for running ExcelTestRunner in multi-file data mode
  when the "
  suiteFile" parameter contains a file name.([#241](https://github.com/selcukes/selcukes-java/pull/241))

## [2.2.6] (21-03-2023)

### Added

* [DataBind] Added a new class called DataTable that provides a set of common table operations for working with data in
  various formats including Excel, CSV, web tables, and
  databases ([#230](https://github.com/selcukes/selcukes-java/pull/230))

### Changed

* [DataBind] Moved Streams, Lists, Maps, and DataTable into a new "collections"
  package ([#230](https://github.com/selcukes/selcukes-java/pull/230))
* [DataBind] Updated parse Excel and CSV to return a DataTable instead of a List of Map<String,
  String> ([#230](https://github.com/selcukes/selcukes-java/pull/230))

### Fixed

* [Core] Fixed issue in launching Chrome browser of 111
  series ([#230](https://github.com/selcukes/selcukes-java/pull/230))

### Removed

* [DataBind] Removed LinkedProperties class and instead use Properties
  class([#230](https://github.com/selcukes/selcukes-java/pull/230))

## [2.2.5] (07-03-2023)

### Added

* Added PageElement class to read element name for EventCapture of Page Actions

### Changed

* Performance enhancement and Core Refactoring
* Added support for default value in Maps of list of keys and list of values by accepting an additional argument
* Update dependency net.masterthought:cucumber-reporting to
  v5.7.5 ([#228](https://github.com/selcukes/selcukes-java/pull/228))

### Fixed

* [Excel Runner] Fixed reading Excel files from jar ([#227](https://github.com/selcukes/selcukes-java/pull/227))

### Removed

* [Core] Remove Selenium Grid dependency in favor of SeleniumSever
  Class ([#229](https://github.com/selcukes/selcukes-java/pull/229))

## [2.2.4] (26-02-2023)

### Added

* [Excel Runner] Singleton and SingletonContext class for Singleton
  Instances ([#226](https://github.com/selcukes/selcukes-java/pull/226))

### Fixed

* [Excel Runner] Hot Fixes ([#225](https://github.com/selcukes/selcukes-java/pull/225))

## [2.2.3] (24-02-2023)

### Added

* [Excel Runner] Support different testdata excel files ([#223](https://github.com/selcukes/selcukes-java/pull/223))
* [Excel Runner] Parse Excel as Map of Maps ([#219](https://github.com/selcukes/selcukes-java/pull/219))

### Changed

* Core Refactoring ([#217](https://github.com/selcukes/selcukes-java/pull/217))
  , ([#221](https://github.com/selcukes/selcukes-java/pull/221))

### Fixed

* [Data Bind] Fixed CSV Parser issue with double quotes

### Removed

[Core] Removed `webdriver-binaries` dependency from core module. `webdriver-binaries` will no longer maintained instead
using inbuilt Selenium Manager ([#219](https://github.com/selcukes/selcukes-java/pull/219))

## [2.2.1] (08-02-2023)

### Added

* Added CSV Data Support ([#216](https://github.com/selcukes/selcukes-java/pull/216))

### Changed

* Update dependency org.projectlombok:lombok to v1.18.26 ([#215](https://github.com/selcukes/selcukes-java/pull/215))
* Update dependency io.cucumber:cucumber-bom to v7.11.([#214](https://github.com/selcukes/selcukes-java/pull/214))
* Update dependency com.fasterxml.jackson:jackson-bom to
  v2.14.2 ([#213](https://github.com/selcukes/selcukes-java/pull/213))
* Update selenium.version to v4.8.0 ([#212](https://github.com/selcukes/selcukes-java/pull/212))
* Update dependency io.cucumber:cucumber-bom to v7.11.0 ([#211](https://github.com/selcukes/selcukes-java/pull/211))
* Update dependency org.junit:junit-bom to v5.9.2 ([#210](https://github.com/selcukes/selcukes-java/pull/210))
* Update dependency org.testng:testng to v7.7.1 ([#209](https://github.com/selcukes/selcukes-java/pull/209))

### Fixed

* Fixed StringHelper toFieldName issue

## [2.2.0] (03-12-2022)

### Changed

* Update dependency selenium.version to v4.7.0 ([#204](https://github.com/selcukes/selcukes-java/pull/204))
* Update dependency io.appium:java-client to v8.3.0 ([#204](https://github.com/selcukes/selcukes-java/pull/204))
* Update dependency com.fasterxml.jackson:jackson-bom to
  v2.14.1 ([#202](https://github.com/selcukes/selcukes-java/pull/202))
* Update dependency net.masterthought:cucumber-reporting to
  v5.7.4 ([#199](https://github.com/selcukes/selcukes-java/pull/199))
* Update dependency io.cucumber:cucumber-bom to v7.9.0 ([#198](https://github.com/selcukes/selcukes-java/pull/198))
* Update dependency org.apache.commons:commons-compress to
  v1.22 ([#197](https://github.com/selcukes/selcukes-java/pull/197))

### Fixed

* [Commons]  Reading file content issue ([#192](https://github.com/selcukes/selcukes-java/pull/192))
* [Core] Error collections issue ([#204](https://github.com/selcukes/selcukes-java/pull/204))

## [2.1.7] (22-09-2022)

### Added

* [DataBind] Added support to get data file name from System
  Properties([#189](https://github.com/selcukes/selcukes-java/pull/189))

### Changed

* Update dependency io.cucumber:cucumber-bom to v7.8.0([#185](https://github.com/selcukes/selcukes-java/pull/185))
* Update dependency selenium.version to v4.4.0([#180](https://github.com/selcukes/selcukes-java/pull/180))
* Update dependency org.apache.maven.plugins:maven-javadoc-plugin to
  v3.4.1([#181](https://github.com/selcukes/selcukes-java/pull/181))
* Update dependency io.appium:java-client to v8.2.0([#182](https://github.com/selcukes/selcukes-java/pull/182))
* Update dependency org.apache.logging.log4j:log4j-slf4j-impl to
  v2.19.0([#186](https://github.com/selcukes/selcukes-java/pull/186))
* Update dependency org.apache.poi:poi-ooxml to v5.2.3([#187](https://github.com/selcukes/selcukes-java/pull/187))
* Update dependency org.junit:junit-bom to v5.9.1([#188](https://github.com/selcukes/selcukes-java/pull/188))

### Removed

* [Commons] Removed DateHelper Class in favor of Clocks([#189](https://github.com/selcukes/selcukes-java/pull/189))

## [2.1.6] (29-07-2022)

### Added

* [BOM] Added Selcukes Bill of Materials (BOM) ([#167](https://github.com/selcukes/selcukes-java/pull/167))
* [Commons] Added SPI `io.github.selcukes.commons.listener.TestLifecycleListener`

### Changed

* Update dependency cucumber.version to v7.5.0([#174](https://github.com/selcukes/selcukes-java/pull/174))
* Update dependency org.junit:junit-bom to v5.9.0([#172](https://github.com/selcukes/selcukes-java/pull/172))
* [Screenshot] Capture full page screenshot using CDP
  command ([#176](https://github.com/selcukes/selcukes-java/pull/176))

### Removed

* [Core] Removed Listeners `TestLifecyclePerMethod`, `TestLifecyclePerClass`, `TestNGReportListener` instead
  use `@Lifecycle`

### Fixed

* [Reports] Add ReportDriver in TestLifecycle ([#169](https://github.com/selcukes/selcukes-java/pull/169))
* [All] Remove TestNG Bindings from Core and Report modules ([#168](https://github.com/selcukes/selcukes-java/pull/168))

## [2.1.5] (11-07-2022)

### Fixed

* [DataBind] Fixed Data Rows overlapping issue.

* [JUnit] Fixed Cucumber junit-platform-suite runner ([#162](https://github.com/selcukes/selcukes-java/pull/162))

## [2.1.4] (10-07-2022)

### Added

* [DataBind] Added support to Parse Properties to
  entityClass([#159](https://github.com/selcukes/selcukes-java/pull/159))

* [DataBind] Added Data Substitutor support for Excel and
  PropertiesMapper([#160](https://github.com/selcukes/selcukes-java/pull/160))

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
