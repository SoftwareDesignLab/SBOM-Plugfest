/** @Author Tina DiLorenzo */
/** @TODO Replace ? when finalized */

export interface SBOM {
  name: string;
  originFormat?: string;
  specVersion?: string;
  sbomVersion?: string;
  serialNumber?: string;
  supplier?: string;
  timestamp?: string;
  publisher?: string;
  allComponents?: SBOMComponent[];

  // @TODO: implement fully later
  dependencyTree?: any;
  Signature?: any;
}

export interface SBOMComponent {
  uuid?: string | null;
  uniqueID?: string | null;
  name?: string | null;
  version?: string | null;
  unpackaged?: string | null;
  publisher?: string | null;
  licenses?: string[] | null;
  cpes?: string[] | [] | null;
  swids?: string[] | [] | null;
  purls?: string[] | [] | null;
}