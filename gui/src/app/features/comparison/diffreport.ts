export const mockupDiffReport: DiffReport = {
  targetSbom: 'SBOM A',
  comparisonSbom: 'SBOM B',
  components: [
    {
      info: { name: 'Component A', occurrences: 2, matching: 1 },
      versions: [
        {
          info: { name: 'v1.1.2', occurrences: 2, matching: 1 },
          attributes: [
            {
              info: { name: 'CPE', occurrences: 2, matching: 1 },
              values: [
                {
                  name: 'CPE NAME',
                  occurrences: 2,
                  matching: 1,
                },
              ],
            },
          ],
        },
      ],
    },
  ],
};

export interface DiffReport {
  targetSbom: any;
  comparisonSbom: any;
  components: component[];
}

interface component {
  info: info;
  versions: version[];
}

interface version {
  info: info;
  attributes: attribute[];
}

interface attribute {
  info: info;
  values: info[];
}

interface info {
  name: string;
  occurrences: number;
  matching: number;
}
