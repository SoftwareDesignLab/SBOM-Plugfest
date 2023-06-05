/** @Author Tina DiLorenzo, Justin Jantzi */
import { Component, Input, OnChanges, SimpleChanges } from "@angular/core";
import { Comparison } from "../comparison";
import { DataHandlerService } from "@services/data-handler.service";
import { SBOMComponent } from "@models/sbom";

@Component({
  selector: "app-comparison",
  templateUrl: "./comparison.component.html",
  styleUrls: ["./comparison.component.css"],
})
export class ComparisonComponent implements OnChanges {
  @Input() comparison: Comparison | null = null;
  path: any[] = [];
  pathTitles: any[] = [];
  icon =  'check_circle'

  constructor(private dataHandler: DataHandlerService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (this.comparison) {

    }
  }

  getAliasFromIndex(index: any) {
    let path = this.dataHandler.lastSentFilePaths[index];
    return this.dataHandler.getSBOMAlias(path);
  }

  getSBOMAlias(name: string) {
    return this.dataHandler.getSBOMAlias(name);
  }

  /**
   * Removes target from compared
   * @returns compared keys
   */
  getFilteredCompared() {
    if(this.comparison !== null) {
      return Object.keys(this.comparison.diffReport).filter((x) => x !== this.comparison?.target);
    }

    return [];
  }

  getMetadataConflicts(name: string) {
    if(!this.comparison)
      return [];

    return this.comparison.diffReport[name].sbomConflicts;
  }

  getComponentConflicts(name: string) {
    if(!this.comparison)
      return [];

    return this.comparison.diffReport[name].componentConflicts;
  }

  getComponentConflictAmount(name: string) {
    let i = 0 ;

    Object.entries(this.getComponentConflicts(name)).forEach(
      ([key, value]) => {
        Object.entries(value).forEach(([key, conflicts]) => {
          i += conflicts.length;
        })
      }
    );

    return i;
  }

  getPathIndex(index: number) {
    if(this.path.length < index) {
      return '';
    }

    return this.path[index];
  }
}

export enum ConflictTypes {
  Metadata,
  Components
}