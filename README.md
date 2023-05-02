# PlugFest Tooling
> A collection of tools to compare the usage and quality of different SBOM generators
>
> ## v3.1.0 -- 5/2/23
> ### API
> - Fixed bug that prevented non-ASCII characters from being processed
> ### Comparison
> - Fix bug that showed duplicate UIDs in the comparison report
> ### Metrics
> - Added support for non-ASCII characters when pulling from package manager databases
> - Remove all empty tests to prevent duplicated component lists
> ### GUI
> - Display which SBOM an identifier or quality came from

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

## Metrics
- Appropriate Amount Test
  - Checks to ensure that each attribute within an SBOM does not exceed the maximum line length in Java
- Completeness Test
  - Checks to make sure components have a name, publisher, version
  - Checks if attributes are formatted correctly and checks CPE and PURL formatting 
- Timeliness Test
  - Uses PURLs to search for information about the package using package manager APIs
  - Confirms that name and publisher match resource
  - Also checks to see if the assigned version number exists in resource

## System Requirements
- Java 17
  > Check: `java -version`

## Quick Start
1. `./gradlew jar`
2. `java -jar app.jar [OPTIONS]`

## Contributors
- [Derek Garcia](mailto:dlg1206@rit.edu)
- [Matt London](mailto:mrl2534@rit.edu)
- [Tina DiLorenzo](mailto:tnd3015@rit.edu)
- [Tyler Drake](mailto:txd3634@rit.edu)
- [Asa Horn](mailto:aoh9470@rit.edu)
- [Justin Jantzi](mailto:jwj7297@rit.edu)
- [Juan Francisco Patino](mailto:jfp6815@rit.edu)
- [Max Stein](mailto:mhs8558@rit.edu)
- [Ian Dunn](mailto:itd3516@rit.edu)
- [Henry Keena](mailto:htk4363@rit.edu)
- [Henry Lu](mailto:hyl2415@rit.edu)
- [Chris Enoch](mailto:cte6149@rit.edu)
- [Ping Liu](mailto:htk4363@rit.edu)