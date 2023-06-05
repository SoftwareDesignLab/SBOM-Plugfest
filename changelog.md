# Plugfest Changelog
All notable changes to Plugfest will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).
---
## [3.7.2] - 2023-06-04
> Completeness processor is not complete, see issue [#159](https://github.com/SoftwareDesignLab/plugfest-tooling/issues/159)
### Added
- New Completeness Processor. New tests include:
  - `ValidCPETest` - checks to see if valid CPE
  - `ValidPurlTest` - checks to see if valid CPE
- New CPE object for testing
### Changed
  - Refactored `EmptyOrNullTest` into `MinElementTest`
    - Missing Relationships check
  - Temporary removed `ValidSWIDTest` and `HasRelationshipsTest`
### Fixed

---
## [3.7.1] - 2023-06-02
### Added
- New License processor. New tests include:
  - `HasLicenseDataTest` - checks to see if has license data
  - `ValidSPDXLicenseTest` - validates SPDX Licensing information
    > Checks to see if the SPDX License Name/ID is valid and if it's depreciated
### Changed
### Fixed

---
## [3.7.0] - 2023-05-31
### Added
- New QA Refactor
  - Unified Metric testing process
    - `RegisteredProcessor` - Collection of Tests for checking if components are registered in their package manager
      - `IsRegisteredTest` - Using a component's purl(s), check if package is registered with their package manager
### Changed
- `DiffReport` is now more JSON friendly object
  - Consolidated `Comparer`, `Comparison`, and `ComponentVersion`

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
