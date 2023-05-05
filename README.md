# PlugFest Tooling
> A collection of tools to compare the usage and quality of different SBOM generators
>
> ## v3.1.1 -- 5/5/23
> ### README
> - Updated README.md with a more accurate and recent description of features.

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

## Quality Assurance
- Actionable Test
  - Tests fields to ensure data contained is usable.
- Completeness Test
  - Checks to make sure components have a name, publisher, version
  - Checks if attributes are formatted correctly and checks CPE and PURL formatting 
- Data Verification Test
  - Uses PURLs to search for information about the package using package manager APIs
  - Confirms that name and publisher match resource
  - Also checks to see if the assigned version number exists in resource
  
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
### Backend
1. `./gradlew bootJar`
2. `java -jar .\api\build\libs\api-3.1.0.jar`
### Frontend
1. `cd gui`
2. `npm install`
3. `npm start`

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
