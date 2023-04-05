# PlugFest Tooling
> A collection of tools to compare the usage and quality of different SBOM generators
>
> **v1.0.0 - alpha** (4/7/23)
> - Differ: Displays the differences between two SBOMs across formats
> - Metrics: Determines the completeness of an SBOM
> - QA Pipeline: Ensure that the SBOM follows its associated schema

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
* Matt London
* Tyler Drake
* Henry Keena
* Henry Lu
* Chris Enoch
* Ping Liu