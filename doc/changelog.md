# Plugfest Changelog
All notable changes to Plugfest will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

Beta
---
## [1.2.1] - 2013-11-29
### Fixed
  - If select all was pressed first on compare than it would always compare all sboms even if one was unchecked

## [1.2.0] - 2013-11-28
### Added
- `DiffReport` now tracks similarity and difference count for metadata and components
- Download button added to compare and metrics
- Header on metrics to display passed/total tests as well as which tests are currently displayed

### Changed
- `package.json` updated app name to plugfest

## [1.1.0] - 2023-11-08
### Added
- Plugfest can now be launched as either a web app or a standalone application

### Changed
- Added the following startup commands for frontend:
  - `web-start`: Runs an angular instance of the application for development purposes
  - `web-build`: Generates an index.html and build folder for the angular application for web deployment
  - `electron-start`: Launches an electron instance of the application
  - `windows-build`: Generates a standalone application (.exe) file and directory for deployment with the backend built and included with
  - `backend-build`: Builds the backend with no tests, used for windows build script
  - `copy-jar`: Copies the latest backend jar built from the backend to the build directory, used for windows build script


Alpha
---

## [3.9.4] - 2023-06-07
### Added
### Changed
- `TranslatorSPDX` Now filters out duplicate CPEs, PURLs, and SWIDs
- `TranslatorCDXJSON` tests to make sure if a JSON SBOM is CycloneDX.
  - If not, a more descriptive error message is thrown to prevent SPDX JSON SBOMs from causing errors.
  - Note: `TranslatorCDXXML` already handles this for SPDX XML SBOMs.

### Fixed
- Fixed SBOMS from JSON files not having hashes
- Fixed SBOMS from SPDX files not having hashes
---
## [3.9.3] - 2023-06-07

### Added
- New SPDX Metrics Processor. New tests include:
  - `HasDataLicenseSPDXTest` - checks to see if the SPDX SBOM DataLicense field in metadata contains the CC0-1.0 license
  - `HasSPDXIDTest` - checks to see if each component has a valid SPDXID ("SPDXRef:-[idstring]")
  - `HasDocumentNamespaceTest` - checks to see if the SPDX SBOM contains a valid document namespace
  - `HasDownloadLocationTest` - checks to see if each component contains a download location
  - `HasCreationInfoTest` - checks to see if the SPDX SBOM contains creation info 
     (Creator and Created fields in metadata)
  - `HasVerificationCodeTest` - checks to see if each component has a package verification code (FilesAnalyzed is true) 
     or is omitted (FilesAnalyzed if false)
  - `HasExtractedLicensesTest` - check if there are any extracted licenses not on the SPDX license list
  - `ExtractedLicenseMinElementTest` - check any extracted licenses for the required fields: LicenseName, LicenseID, 
     LicenseCrossReference 
- Method in Utils.java to ensure components with no licenses have no null components
- Added additional exceptions thrown in `TranslatorCDXXML` to increase verbosity of error messages.

### Changed
- `TranslatorSPDX` Now filters out duplicate CPEs, PURLs, and SWIDs
### Fixed
- Fixed broken SBOM file transfers by adding back the `@RequestBody` decorators to all `SBOMFile` API request parameters.
- Fixed issue with CDXJSON translator regarding translating null license objects to components without licenses
- Fixed errors caused by `TranslatorPlugFest.getTranslator()` method incorrectly determining filetype.
  - We now check for file extension only, since SPDX JSON and XML are not supported translators.
- Properly remove `SBOM` object from QA pipeline
---
## [3.9.2] - 2023-06-06
> CDX Metrics processor is not complete, see issue [#183](https://github.com/SoftwareDesignLab/plugfest-tooling/issues/183)
### Added
- New CDX Metrics Processor. New tests include:
  - `HasBomRefTest` - checks to see if a component has a unique bom-ref to reference inside the BOM
  - `HasBomVersionTest` - check if the SBOM has a version number declared

### Changed

### Fixed
- Fixed broken SBOM file transfers by adding back the `@RequestBody` decorators to all `SBOMFile` API request parameters.

---
## [3.9.1] - 2023-06-05
> Uniqueness processor is not complete, see issue [#160](https://github.com/SoftwareDesignLab/plugfest-tooling/issues/160)
### Added
- New Uniqueness Processor. New tests include:
  - `HasHashDataTest` - checks to see if component contains Hashes
  - `ValidHashDataTest` - checks to see if component Hashes are a supported type
  - `AccuratePURLTest` - checks to see if a component's PURL(s) matches stored component data
  - `AccurateCPETest` - checks to see if a component's CPE(s) matches stored component data

### Changed

### Fixed

---
## [3.9.0] - 2023-06-04
> Completeness processor is not complete, see issue [#159](https://github.com/SoftwareDesignLab/plugfest-tooling/issues/159)
### Added
- Several fields to `Component`:
  - `group` - The group of the component, found either in the PURL or the CycloneDX component itself.
  - `downloadLocation` - SPDX-only field
  - `filesAnalyzed` - SPDX-only field
  - `verificationCode` - SPDX-only field
  - `extractedLicenses` - SPDX-only field - this is a map from a license ID to its attributes, and contains all
    extracted licenses found in the component. These licenses are not contained in the normal `licenses` set.
- `SBOM.getName()` method to get the name of the project described by the SBOM.
- New Completeness Processor. New tests include:
  - `ValidCPETest` - checks to see if valid CPE
  - `ValidPurlTest` - checks to see if valid CPE
- New CPE object for testing

### Changed
- Updated `TranslatorSPDX` and `TranslatorSPDXTest` to reflect the added translation of the above fields.
- Refactored `EmptyOrNullTest` into `MinElementTest`
  - Missing Relationships check
- Temporary removed `ValidSWIDTest` and `HasRelationshipsTest`

### Fixed

---
## [3.8.3] - 2023-06-02
### Changed
- `TranslatorCDXXML.java`
  - Now creates the SBOM object and top component after components have been parsed
  - Top component defaulting changed to execute upon empty top component data collection
  - resolveMetadata now returns before assigning top component data if the top component collection is empty

---
## [3.8.2] - 2023-06-02
### Added

### Changed
- Updated the Quick Start guide in README.md.

### Fixed
- Fixed `UTF-8 Not Supported Error` caused by using the `@RequestBody` annotation with a class object.
- `Debug.log()` had an unchecked cast to `Exception`; this was fixed by checking the type.

---
## [3.8.1] - 2023-06-01
### Added
- Added test file cdxgen-8.4.6-source.json .
  - Added test data entry for the Authors of the metadata in the downloaded file above.

### Changed
- Stringify the `authors` object in `TranslatorCDXJSON.java` .
- Modified `TranslatorCDXJSONTest` to run the newly added test source file cdxgen-8.4.6-source.json .

### Fixed

---
## [3.8.0] - 2023-06-01
### Added
- `Debug` class from SVIP and replace (most) system calls with this to improve verbosity of our output.
- `TranslatorException` class to catch translator errors at the top-level.
  - This allows error handling at the API level with a simple failure message.

### Changed
- `TranslatorSPDX` now uses regex and string manipulation to parse through an SPDX tag-value file instead of 
  attempting to read it line-by-line.
- `TranslatorCDXJSONTest` now adds UIDs to each component only if they are not null.
  - `Component` construction was changed to only use the constructor with `name`, `version`, and `publisher` attributes.
- Most translator error logs now throw a `TranslatorException`

### Fixed
- Removed `PURL` class usage from `Component` and replaced it with a simple string. The `PURl` class should now only 
  be used in metric tests.

---
## [3.7.1] - 2023-05-31
### Added
- Tests in `TranslatorCDXJSONTest`, `TranslatorCDXXMLest`, & `TranslatorSPDXTest` to check for null UIDs in all 
  translated components and ensure correct processing of SBOMs with no metadata.
- `TranslatorSPDX` now has the capability to read in multiple external licenses. See 
  [the specification](https://spdx.github.io/spdx-spec/v2.3/other-licensing-information-detected/) for details.
- New License processor. New tests include:
  - `HasLicenseDataTest` - checks to see if has license data
  - `ValidSPDXLicenseTest` - validates SPDX Licensing information
    > Checks to see if the SPDX License Name/ID is valid and if it's depreciated

### Changed
- `TranslatorPlugfest` now checks for key tokens in file contents as well as the file extension to determine the 
  translator to use.
- `TranslatorCDXJSONTest` now adds UIDs to each component only if they are not null.
  - `Component` construction was changed to only use the constructor with `name`, `version`, and `publisher` attributes.

### Fixed

---
## [3.7.0] - 2023-05-31
### Added
- New QA Refactor
  - Unified Metric testing process
    - `UniquenessProcessor` - Collection of Tests for checking a component's UIDs (Hashes, PURLs, etc)
      - `HasHashDataTest` - Test if a component has any hash values and the number of hashes if present
      `ValidTestDataTest` - Test hashes if they match a valid hash schema
      - `AccuratePURLTest` - Test a component's PURL(s) if its data matches the component's stored data
    - `RegisteredProcessor` - Collection of Tests for checking if components are registered in their package manager
      - `IsRegisteredTest` - Using a component's purl(s), check if package is registered with their package manager
- Add `dev` launcher that launches just the front end.
- When internal failure occurs, Translators throw a top-level `TranslatorException` which contains information 
  regarding the specific error
- `/compare` tests are implemented that check for user input that will produce a `HttpStatus.BAD_REQUEST` response

### Changed
- `DiffReport` is now more JSON friendly object
  - Consolidated `Comparer`, `Comparison`, and `ComponentVersion`
- `Utils.java` now holds `SBOMFile` class

### Fixed

---
## [3.6.0] - 2023-05-30
### Added

### Changed
- Consolidate SBOM object and removed redundant classes

### Fixed

---
## [3.5.0] - 2023-05-26
### Added
- New QA Refactor
  - Unified Metric testing process
    - `CompletenessProcesser` - Collection of Tests for checking for SBOM content
      - `NullOrEmptyTest` - Test if field is `null` or empty string
### Changed
- Modified API Plugfest endpoints to use HTTP Body for data
- Removed Stateful `QAPipeline`
- Removed old QAPipeline and all previously existing Processors/Metrics

### Fixed

---
## [3.3.0] - 2023-05-22
> See issue [#92](https://github.com/SoftwareDesignLab/plugfest-tooling/issues/92)
### Added
- updated PURL object to have more robust fields and utility
### Changed

### Fixed

---
## [3.2.1] - 2023-05-18
> TODO: Add `dev` launcher that launches just the front end. See issue [#118](https://github.com/SoftwareDesignLab/plugfest-tooling/issues/118)
### Added

### Changed

### Fixed
- Frontend launches backend and kills process on exit
- removed full filepath from upload screen

---
## [3.2.0] - 2023-05-09
### Added
- Allow marking of components as appearing in target SBOM
- Added individual loading spinners for each uploaded SBOM in GUI
### Changed

### Fixed
- Fixed API bug preventing non-ASCII characters from being processed during request
- Fix bug causing formatting issues with the data verification test

---
## [3.1.0] - 2023-05-02
### Added
- Added support for non-ASCII characters when pulling from package manager databases
- Display which SBOM an identifier or quality came from in GUI
### Changed
- Remove all empty tests to prevent duplicated component lists
### Fixed
- Fix Comparison bug that showed duplicate UIDs in the comparison report

---
## [3.0.0] - 2023-04-21
### Added
- Translators support translating from a file's contents without needing the file on the system
- Opened an API endpoint for QualityReport generating
- Opened an API endpoint for ComparisonReport generating
- Report objects can now be Jacksonized and sent to frontend
### Changed
- Comparison now maintains the list of SBOMs in which a quality appeared
- Reformatted project into API and Core
- General documentation and cleanup
### Fixed
- Hotfix to allow translating SBOMs that have non-ASCII characters

---
## [2.2.0] - 2023-04-14
### Added
- Build a comparison report object to send to the frontend which will compare a list of SBOMs to some target SBOM
- Built a comparison function which generates the report from a target SBOM and list of SBOMs
- Built a new metrics object to send to frontend for displaying SBOM quality
- Added an `ActionableTest` to preliminarily check if an SBOM is actionable
- Created sidebar and component display for SBOM differences with mockup data.
### Changed

### Fixed

---
## [2.1.0] - 2023-04-07
### Added
- added Documented `TimelinessTest`
  - `TimelinessTest` now checks to see if version numbers and publishers match the resource (package manager database)
- Added `changelog.md` to track weekly changes to the project
- Added `README.md` to hold usage instructions and project breakdown
- Removed quality metrics from the SBOM class
- Added `PURL` class to allow for easier PURL parsing
### Changed

### Fixed

---
## [2.0.0] - 2023-03-23
### Added
- **Differ**
  - Add support for comparing across SBOM formats
- **Metrics**
  - Developed QA Pipeline structure from start to finish
    - Developed several Custom Metrics
      - `ContextualProcessor`
        - `CompletenessTest`
        - `AppropriateAmountTest`
        - `RelevancyTest`
    - Implement `Test` and `TestResults` to standardize test results returned from metrics
      - All metrics are now ran on one component at a time for a more informative per-component output
    - Integrated several OS Metrics
      - `SPDXProcessor`
      - `CDXProcessor`
- **SBOM**
  - Add supplier information to SBOMs to store the manufacturer of the software
- Created a new Translator package that can now be integrated into plugfest
  - Includes CDX Translator, SPDX Translator, SVIP Translator Driver, PlugFest Translator Driver
- Created Wireframe GUI for Plugfest
### Changed
- **Differ**
  - Rewrite the line-by-line system to digest SBOM data into an object for comparison
  - Differ now does not display differences for differently ordered components
  - Use the SVIP SBOM and SVIP Translators to find differences in SBOMs
- **SBOM**
  - Replace SBOM object with the SBOM object from SVIP
  - Represent SBOM information agnostically rather than in a specified format
- **Translators**
  - Translators have received a slight rework.
  - Integrated different google Multimap over Glassfish.
  - Now more accurately extracts data from SBOMs.
### Fixed
