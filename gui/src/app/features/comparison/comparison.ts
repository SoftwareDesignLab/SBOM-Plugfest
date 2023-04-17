interface SBOM {
  name: string;
}

interface Component {
  name: string;
}

interface SBOMConflict {
  sbomA: SBOM;
  sbomB: SBOM;
  conflictTypes: Set<string>;
}

interface ComponentConflict {
  componentA: Component;
  componentB: Component;
  conflictTypes: Set<string>;
}

interface DiffReport {
  sbomA: SBOM;
  sbomB: SBOM;
  sbomConflict: SBOMConflict;
  componentConflicts: Set<ComponentConflict>;
}

interface ComponentVersion {
  componentName: string;
  version: string;
  CPES: Set<UniqueIdOccurrence>;
  PURLs: Set<UniqueIdOccurrence>;
  SWID: Set<UniqueIdOccurrence>;
  appearances: Set<number>; // number meaning SBOM ID
}

interface UniqueIdOccurrence {
  uniqueIdentifier: string;
  appearances: Set<number>;
  uniqueIdType: string;
}

export interface Comparison {
  targetSbom?: SBOM;
  diffReports: DiffReport[];
  comparisons: Map<string, Set<ComponentVersion>>;
}

//#region mockup Comparison
const sbomA = { name: 'SBOM a' };
const sbomB = { name: 'SBOM b' };

const componentA: Component = { name: 'Component A' };
const componentB: Component = { name: 'Component b' };

const componentConflict: ComponentConflict = {
  componentA: componentA,
  componentB: componentB,
  conflictTypes: new Set<string>([
    'COMPONENT_VERSION_MISMATCH',
    'COMPONENT_CPE_MISMATCH',
  ]),
};

const componentVersions = [
  {
    componentName: 'Component A',
    version: '1.1.2',
    CPES: new Set<UniqueIdOccurrence>(),
    PURLs: new Set<UniqueIdOccurrence>(),
    SWID: new Set<UniqueIdOccurrence>(),
    appearances: new Set<number>([1]),
  },
  {
    componentName: 'Component A',
    version: '1.1.3',
    CPES: new Set<UniqueIdOccurrence>(),
    PURLs: new Set<UniqueIdOccurrence>(),
    SWID: new Set<UniqueIdOccurrence>(),
    appearances: new Set<number>([2]),
  },
];

export const comparisonMockup: Comparison = {
  targetSbom: {
    name: 'SBOM a',
  },
  diffReports: [
    {
      sbomA: sbomA,
      sbomB: sbomB,
      sbomConflict: {
        sbomA: sbomA,
        sbomB: sbomB,
        conflictTypes: new Set<string>(['SUPPLIER_MISMATCH']),
      },
      componentConflicts: new Set<ComponentConflict>([componentConflict]),
    },
  ],
  comparisons: new Map([
    ['Component A', new Set<ComponentVersion>(componentVersions)],
  ]),
};

//#endregion
