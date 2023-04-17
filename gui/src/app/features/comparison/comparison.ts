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
  componentName?: string;
  version?: string;
  CPES?: Set<UniqueIdOccurrence>;
  PURLs?: Set<UniqueIdOccurrence>;
  SWID?: Set<UniqueIdOccurrence>;
  appearances?: Set<number>; // number meaning SBOM ID
}

interface UniqueIdOccurrence {
  uniqueIdentifier?: string;
  appearances?: Set<number>;
  uniqueIdType?: string;
}

export interface Comparison {
  targetSbom?: SBOM;
  diffReports: DiffReport[];
  comparisons: {[key: string]:  attributes[]};
}

//#region mockup Comparison

export const finalMockup: Comparison = mockup;

//#endregion
