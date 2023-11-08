# PlugFest-in-a-Box Tool
### v1.0.0-beta
[changelog](changelog.md)
> PlugFest-in-a-Box is a powerful tool to reveal key areas of difference between several Software Bills of Materials 
> (SBOMs) and applying thorough metrics to identify any and all quality issues.


## Supported SBOM Formats
- CycloneDX 1.4 JSON
- CycloneDX 1.4 XML
- SPDX 2.3 Tag-Value


## Quick Start
> See [System Requirements](doc/README.md) for more details
1. `cd gui`
2. `npm -ci`
3. `npm run ...`
    - web-start: Runs an angular instance of the application for development purposes
    - web-build: Generates an index.html and build folder for the angular application for web deployment
    - electron-start: Launches an electron instance of the application
    - windows-build: Generates a standalone application (.exe) file and directory for deployment with the backend built and included with


## Comparison
> Allows comparison across schemas and file formats

### SBOM Conflicts
- **Supplier**: Supplier of the code are not the same (publisher)
- **Author**: SBOMs have different authors
- **Timestamp**: SBOMs have different timestamps
- **Origin Format**: SBOMs have different origin formats
- **Schema Version**: SBOMs have different schema versions (CycloneDX 1.4, SPDX 2.3, etc)
- **SBOM Version**: SBOMs have different versions
- **Serial Number**: SBOMs have different serial numbers

### Component Conflicts
- **Missing**: Component only found in one SBOM
- **Version**: Component found in both SBOMs, but has different versions
- **License**: Component found in both SBOMs, but has different licenses
- **Publisher**: Component found in both SBOMs, but has different publisher
- **CPE**: Component found in both SBOMs, but has different CPE
- **PURL**: Component found in both SBOMs, but has different PURL
- **Hash**: Component found in both SBOMs, but has different Hashes


## Metrics
> A series of metrics to access the quality of the SBOM.

### Completeness
> Accesses how complete the content of the SBOM is.
- **Minimum Elements Test**: Checks for the [Minimum Elements for an SBOM](https://www.ntia.doc.gov/files/ntia/publications/sbom_minimum_elements_report.pdf) 
are present as recommend by the NTIA.
  - _Supplier Name_: The name of an entity that creates, defines, and identifies components. 
  - _Component Name_: Designation assigned to a unit of software defined by the original supplier. 
  - _Version of the Component_: Identifier used by the supplier to specify a change in software from a previously identified version. 
  - _Other Unique Identifiers_: Other identifiers that are used to identify a component, or serve as a look-up key for relevant databases.
    > Plugfest uses CPE and PURL
  - _Author of SBOM Data_: The name of the entity that creates the SBOM data for this
    component. 
  - _Timestamp_: Record of the date and time of the SBOM data assembly
- **Valid PURL Test**: Test to see if the PURL is correctly formatted
- **Valid CPE Test**: Test to see if the CPE is correctly formatted

### Uniqueness
> Accesses the quality of the unique identifiers and ensure they match the stored SBOM data.
- **Has Hash Data Test**: Test to see if hashes are stored
- **Valid Hash Data Test**: Test to see the stored hashes match the reported hash algorithm
- **Accurate PURL Test**: Test to see if the data stored in the PURL matches what is reported in the SBOM
- **Accurate CPE Test**: Test to see if the data stored in the CPE matches what is reported in the SBOM

### Registered
> Accesses if the component is stored in a default repository
- **Is Registered Test**: Uses PURLs to verify if the component exists in the [default PURL repository](https://github.com/package-url/purl-spec/blob/master/PURL-TYPES.rst)
  
### Licensing
> Accesses if the SBOM has valid license data
- **Has License Data Test**: Test to see if Licenses are stored
- **Valid SPDX License Test**: Test to see if the License is stored in the [SPDX License List](https://spdx.org/licenses/) and if they are depreciated

### SPDX
> Accesses for features that are required specifically for [SPDX SBOMs](https://spdx.github.io/spdx-spec/v2.3/).
- **Has Data License SPDX Test**: Test to see if the SBOM's DataLicense field contain the CC0-1.0 license
- **Has SPDX ID Test**: Test to see if each component has a valid SPDXID
- **Has Document Namespace Test**: Test to see if the SBOM contains a valid document namespace
- **Has Download Location Test**: Test to see if each component has a download location
- **Has Creation Info Test**: Test to see if the SBOM contains creation information
- **Has Verification Code Test**: Test to see if each component has a package verification code (FilesAnalyzed is true) or is it omitted (FilesAnalyzed if false)
- **Has Extracted Licenses Test**: Test to see if there are any extracted licenses not on the SPDX license list in the SBOM
- **Extracted License Minimum Element Test**: Test to see if the extracted licenses contain the required fields LicenseName, LicenseID, and LicenseCrossReference

### CycloneDX
> Accesses for features that are required specifically for [CycloneDX SBOMs](https://cyclonedx.org/specification/overview/).
- **Has Bom-Ref Test**: Test to see if a component has a unique bom-ref to reference inside the SBOM
- **Has Bom Version Test**: Test to see if the SBOM has a version number declared


## Contributors
**Principal Investigator:** [Mehdi Mirakhorli](mailto:mxmvse@rit.edu)

**Senior Project Manager:** [Chris Enoch](mailto:ctevse@rit.edu)

**Senior Developer Team Lead:** [Derek Garcia](mailto:dlg1206@rit.edu)

**Developer Team Leads**
- [Tina DiLorenzo](mailto:tnd3015@rit.edu)
- [Tyler Drake](mailto:txd3634@rit.edu)
- [Matt London](mailto:mrl2534@rit.edu)
- [Dylan Mulligan](mailto:dtm5568@rit.edu)

**Developer Team**
- [Michael Alfonzetti](mailto:michael.alfonzetti93@gmail.com)
- [Ian Dunn](mailto:itd3516@rit.edu)
- [Asa Horn](mailto:aoh9470@rit.edu)
- [Justin Jantzi](mailto:jwj7297@rit.edu)
- [Ping Liu](mailto:pxlvse@rit.edu)
- [Matthew Morrison](mailto:msm8275@rit.edu)
- [Ethan Numan](mailto:ehn4602@rit.edu)
- [Henry Orsagh](mailto:hco4630@rit.edu)
- [Juan Francisco Patino](mailto:jfp6815@rit.edu)
- [Max Stein](mailto:mhs8558@rit.edu)
