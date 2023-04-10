# PlugFest Tooling
> A collection of tools to compare the usage and quality of different SBOM generators
>
> ## v2.1.0 -- 4/7/23
> ### Differ
> - None
>
> ### Metrics
> - Documented `TimelinessTest`
> - `TimelinessTest` now checks to see if version numbers and publishers match the resource (package manager database)
>
> ### Miscellaneous
> - Added `changelog.md` to track weekly changes to the project
> - Added `README.md` to hold usage instructions and project breakdown
> - Removed quality metrics from the SBOM class
> - Added `PURL` class to allow for easier PURL parsing

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

## QA Pipeline
- Uses open source SPDX Java library to check that the SPDX SBOMs match the schema
- Uses open source CycloneDX Java library to check that the CycloneDX SBOMs match the schema

## System Requirements
- Java 17
  > Check: `java -version`

## Quick Start
1. `./gradlew jar`
2. `java -jar app.jar [OPTIONS]`

## Contributors
- [Matt London](mailto:mrl2534@rit.edu)
- [Tyler Drake](mailto:txd3634@rit.edu)
- [Henry Keena](mailto:htk4363@rit.edu)
- [Henry Lu](mailto:hyl2415@rit.edu)
- [Chris Enoch](mailto:cte6149@rit.edu)
- [Ping Liu](mailto:htk4363@rit.edu)