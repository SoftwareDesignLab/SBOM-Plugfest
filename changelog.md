# Plugfest Changelog
## v2.0.0
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