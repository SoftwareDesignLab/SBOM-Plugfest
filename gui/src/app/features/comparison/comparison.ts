/** @Author Tina DiLorenzo, Justin Jantzi */

interface Conflict {
  type: string;
  target: string;
  other: string;
}

interface DiffReport {
  sbomConflicts: Conflict[];
  componentConflicts: {
    [id: string]: {
      [id: string]: Conflict
    }
  };
}

export interface Comparison {
  targetSBOM: string;
  diffReport: {
    [path: string]: DiffReport
  };
}
