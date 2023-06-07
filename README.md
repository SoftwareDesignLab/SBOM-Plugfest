# PlugFest Tooling
> A collection of tools to compare the usage and quality of different SBOM generators
>
> ## v3.2.0 -- 5/9/23
> ### API
> - Fixed another bug preventing non-ASCII characters from being processed
> ### Comparison
> - Allow marking of components as appearing in target SBOM
> ### Metrics
> - Fix bug causing formatting issues with the data verification test
> ### GUI
> - Added individual loading spinners for each uploaded SBOM

## Differ
- Compares two SBOMs supporting CycloneDX XML and SPDX Tag-Value
- Allows comparison between CycloneDX and SPDX formats
- Displays differences in trivial SBOM attributes like publisher and timestamp
- Displays differences in found component data between SBOMs 
  - Version number
  - Publisher
  - CPEs
  - PURLs
  - SWIDs
- Summarizes the report in a Unix-diff-like print

## Comparison
- Generate detailed DiffReports from a target SBOM and a list of SBOMs. 

## Quality Attributes
- **Completeness Test**
  - Checks if the [Minimum Elements for an SBOM](https://www.ntia.doc.gov/files/ntia/publications/sbom_minimum_elements_report.pdf) 
    are present as recommend by the NTIA.
    - [x] Supplier Name
    - [x] Component Name
    - [x] Version of the Component
    - [ ] Other Unique Identifiers
      - [x] CPE
      - [x] PURL
      - [ ] SWID
    - [ ] Dependency Relationship
    - [x] Author of SBOM Data
    - [x] Timestamp
  - Check if the CPE and PURL Identifiers are formatted correctly using regex.
- **Uniqueness Test**
  - Checks if a component's unique identifiers are accurate to the component data.
    - [x] Has Hash Data: Does the component have hash data?
    - [x] Valid Hash Data: Are the component's hashes a supported type?
    - [ ] Accurate UIDs
      - [x] CPE
        - Does the CPE's data match the component's data? 
          - Confirms that the CPE's name, version, and publisher match the component's stored data
      - [x] PURL
        - Does the PURL's data match the component's data?
          - Confirms that the PURL's name and version match the component's stored data
          - Implementation of PURL's namespace is needed.
      - [ ] SWID
        - Does the SWID's data match the component's data?
- **Registered Processor Tests**
  - IsRegisteredTest:
    - Uses PURLs to search for information about the package using package manager APIs
    - Confirms that name and version match resource and package is registered with package manager 
- **CycloneDX Metrics Test**
  - Checks if [CycloneDX SBOM specific metrics](https://cyclonedx.org/docs/1.4/json/) are included.
    - Has Bom-Ref: Does a component have a unique bom-ref to reference inside the BOM?
    - Has Bom Version: Does the SBOM have a version number declared?
## Translator
- Parse SBOMS from files and deserialize from formats:
  - CycloneDX
    > .xml and .json
  - SPDX
    > .spdx
## System Requirements
- Java 17
  > Check: `java -version`

## Quick Start
### Launch Bundled Application
```
cd gui
npm install
npm start
```
### Launch Separately
```
# Start back-end in root directory
# Or run api/src/main/java/org/nvip/plugfest/tooling/APIApplication in your IDE of choice, this works with debug mode!
./gradlew bootJar
java -jar .\api\build\libs\api-3.1.0.jar

# Then start front-end
cd gui
npm install
npm run dev
```

## Contributors
**Principal Investigator:** [Mehdi Mirakhorli](mailto:mxmvse@rit.edu)

**Senior Project Manager:** [Chris Enoch](mailto:ctevse@rit.edu)

**Senior Developer Team Lead:** [Derek Garcia](mailto:dlg1206@rit.edu)

**Developer Team Leads**
- [Tina DiLorenzo](mailto:tnd3015@rit.edu)
- [Matt London](mailto:mrl2534@rit.edu)
- [Dylan Mulligan](mailto:dtm5568@rit.edu)

**Developer Team**
- [Tyler Drake](mailto:txd3634@rit.edu)
- [Ian Dunn](mailto:itd3516@rit.edu)
- [Asa Horn](mailto:aoh9470@rit.edu)
- [Justin Jantzi](mailto:jwj7297@rit.edu)
- [Henry Orsagh](mailto:hco4630@rit.edu)
- [Juan Francisco Patino](mailto:jfp6815@rit.edu)
- [Max Stein](mailto:mhs8558@rit.edu)
