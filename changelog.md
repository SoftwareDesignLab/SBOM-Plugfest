# Plugfest Changelog

## v3.0.0 -- 4/21/23
### Translator
- Now supports translating from a file's contents without needing the file on the system
- Hotfix to allow translating SBOMs that have non-ASCII characters
### Comparison
- Comparison now maintains the list of SBOMs in which a quality appeared
### API
- Opened an API endpoint for QualityReport generating
- Opened an API endpoint for ComparisonReport generating
### Misc
- Reformatted project into API and Core
- General documentation and cleanup
- Report objects can now be Jacksonized and sent to frontend

## v2.2.0 -- 4/14/23
### Comparison reports
- Build a comparison report object to send to the frontend which will compare a list of SBOMs to some target SBOM
- Built a comparison function which generates the report from a target SBOM and list of SBOMs
### Metrics
- Built a new metrics object to send to frontend for displaying SBOM quality
- Added an `ActionableTest` to preliminarily check if an SBOM is actionable
### Misc
- General documentation and code cleanup

## v2.1.0 -- 4/7/23
### Differ
- None

### Metrics
- Documented `TimelinessTest`
  - `TimelinessTest` now checks to see if version numbers and publishers match the resource (package manager database)

### Miscellaneous
- Added `changelog.md` to track weekly changes to the project
- Added `README.md` to hold usage instructions and project breakdown
- Removed quality metrics from the SBOM class
- Added `PURL` class to allow for easier PURL parsing

## v2.0.0 -- 3/31/23
### Differ
- Rewrite the line-by-line system to digest SBOM data into an object for comparison
- Add support for comparing across SBOM formats
- Differ now does not display differences for differently ordered components
- Use the SVIP SBOM and SVIP Translators to find differences in SBOMs
- Recorded demo of comparing two SBOMs

### Metrics
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

### Visualization
- Created Wireframe for Plugfest

### SBOM
- Replace SBOM object with the SBOM object from SVIP
- Represent SBOM information agnostically rather than in a specified format
- Add supplier information to SBOMs to store the manufacturer of the software
- Created a new Translator package that can now be integrated into plugfest 
  - Includes CDX Translator, SPDX Translator, SVIP Translator Driver, PlugFest Translator Driver
- Translators have received a slight rework.
  - Integrated different google Multimap over Glassfish.
  - Now more accurately extracts data from SBOMs.