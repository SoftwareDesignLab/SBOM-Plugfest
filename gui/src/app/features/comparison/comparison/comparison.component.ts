/** @Author Tina DiLorenzo, Justin Jantzi */
import { Component, Input, OnChanges, SimpleChanges } from "@angular/core";
import { Comparison, ComponentVersion, attributes } from "../comparison";
import { DataHandlerService } from "@services/data-handler.service";
import { SBOMComponent } from "@models/sbom";

@Component({
  selector: "app-comparison",
  templateUrl: "./comparison.component.html",
  styleUrls: ["./comparison.component.css"],
})
export class ComparisonComponent implements OnChanges {
  @Input() comparison: Comparison | null = null;
  @Input() comparedSBOMS: string[] = [];

  display: { [key: string]: readonly ComponentVersion[] } = {};
  targetSBOM: {
    [key: string]: string[];
  } = {};
  keys: string[] = [];
  path: any[] = [];
  pathTitles: string[] = [];
  filtered: boolean = false;
  attributes: { [key: string]: attributes[] | undefined } = {
    purls: [],
    cpes: [],
    swids: [],
  };

  constructor(private dataHandler: DataHandlerService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (this.comparison) {
      this.display = { ...this.comparison?.comparisons };
      this.path = [this.comparison.comparisons];
      this.pathTitles = ["Components"];
      this.keys = Object.keys(this.display);
      this.getTargetSBOMValues();
    }
  }

  getAliasFromIndex(index: any) {
    let path = this.dataHandler.lastSentFilePaths[index];
    return this.dataHandler.getSBOMAlias(path);
  }

  getTargetSBOMValues() {
    if (!this.comparison?.targetSbom) {
      return;
    }
    const targetSBOM = this.comparison.targetSbom;

    targetSBOM.allComponents?.forEach((component) => {
      if (component.name) {
        if (!this.targetSBOM[component.name]) {
          this.targetSBOM[component.name] = [];
        }
        if (component.version) {
          this.targetSBOM[component.name].push(component.version);
        }
      }
    });
  }

  increaseDepth(newLocation: any, pathTitles: string) {
    this.path.push(newLocation);
    this.pathTitles.push(pathTitles);
    if (this.path.length === 3) {
      Object.keys(this.attributes).forEach((attr) => {
        this.attributes[attr] = Object.values(
          this.path[2][attr] as attributes[]
        );
      });
    }
  }

  decreaseDepth(index: number) {
    if (index < this.path.length - 1) {
      this.pathTitles = this.pathTitles.slice(0, index + 1);
      this.path = this.path.slice(0, index + 1);
    }
  }

  filterConflicts() {
    if (!this.comparison) {
      return;
    }
    // Filter out nonConflicts
    if (!this.filtered) {
      const filtered = Object.keys(this.comparison.comparisons).filter(
        (key) => {
          let isUnique = false;
          this.comparison?.comparisons[key].forEach((version) => {
            // @TODO HOTFIX, REPLACE WHEN WE CAN ACTUALLY HAVE MORE THAN 2 SBOMS
            // Version is unique
            if (version?.appearances?.length < 2) {
              isUnique = true;
            } else {
              // @TODO HOTFIX, replace with attributeslist typescript is being annoying.
              const attributes = [
                ...Object.values(version.purls),
                ...Object.values(version.swids),
                ...Object.values(version.cpes),
              ];
              for (let attr of attributes) {
                if (attr.appearances) {
                  if (attr?.appearances?.length < 2) {
                    isUnique = true;
                    break;
                  }
                }
              }
            }
          });
          if (!isUnique) {
            delete this.display[key];
          }
          return isUnique;
        }
      );
      this.display = this.display;
      this.keys = filtered;
    } else {
      this.display = { ...this.comparison?.comparisons };
      this.keys = Object.keys(this.comparison?.comparisons);
      console.log(JSON.stringify(this.keys));
    }
    this.filtered = !this.filtered;
    this.pathTitles = ["Components"];
    this.path = [this.display];
  }
}
