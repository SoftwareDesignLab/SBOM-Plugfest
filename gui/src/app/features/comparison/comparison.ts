/** @Author Tina DiLorenzo, Justin Jantzi */

export interface Conflict {
  type: string;
  target: string | null;
  other: string | null;
}

interface DiffReport {
  sbomConflicts: Conflict[];
  componentConflicts: ComponentConflicts;
  similarity: number;
  differences: number;
}

export interface ComponentConflicts {
  [id: string]: {
    [id: string]: Conflict[]
  }
};

export interface Comparison {
  target: string;
  diffReport: {
    [path: string]: DiffReport
  };
}
