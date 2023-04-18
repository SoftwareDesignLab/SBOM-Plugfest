/** Author: Tina DiLorenzo */

/** @TODO */
// 1. CREATE A CONSTRUCTOR TAKING IN A JSON OBJECT
// 2. CONVERT JSON PROPERTIES TO SETS/MAPS

import mockup from './diffreport3.json';
interface SBOM {
  name: string;
}

interface SBOMConflict {
  conflictTypes?: Set<string>;
  conflicts?: any[];
}

interface ComponentConflict {
  componentA?: attributes | null;
  componentB?: attributes | null;
  conflictTypes?: string[];
  conflicts?: any[];
}

interface attributes {
  uuid?: string | null;
  name?: string | null;
  publisher?: string | null;
  unpackaged?: boolean;
  cpes?: attributes[] | string[] | [] | null;
  purls?: attributes[] | attributes | [];
  swids?: string[] | [];
  uniqueID?: string | null;
  uniqueIDType?: string | null;
  children?: string[] | [];
  version?: string | null;
  vulnerabilities?: string[] | [];
  licenses?: string[] | [] | null;
  conflicts?: any[] | [];
  componentName?: string | null;
  appearances?: Number[] | []
  componentVersion?: Number[] | [] | string;
  packageManager?: string | null;
}

interface DiffReport {
  sbomConflict?: SBOMConflict;
  componentConflicts?: ComponentConflict[];
}

interface ComponentVersion {
  componentName?: string | null;
  componentVersion: string | null;
  cpes?: UniqueIdOccurrence[] | [];
  purls?: UniqueIdOccurrence[] | [];
  swids?: UniqueIdOccurrence[] | [];
  appearances?: number[] | []; // number meaning SBOM ID
}

interface UniqueIdOccurrence {
  appearances?: number[];
  uniqueIdType?: string;
}

export interface Comparison {
  targetSbom?: SBOM;
  diffReports: DiffReport[];
  comparisons: {[key: string]:  ComponentVersion[]};
}

//#region mockup Comparison

export const finalMockup: Comparison = mockup;

//#endregion
