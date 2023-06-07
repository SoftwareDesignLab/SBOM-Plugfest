/** @Author Tina DiLorenzo, Justin Jantzi */

interface Conflict {
  type: string;
  target: string | null;
  other: string | null;
}

interface DiffReport {
  sbomConflicts: Conflict[];
  componentConflicts: {
    [id: string]: {
      [id: string]: Conflict[]
    }
  };
}

export interface Comparison {
  target: string;
  diffReport: {
    [path: string]: DiffReport
  };
}
