/** @Author Tina DiLorenzo, Justin Jantzi */

import { Component, EventEmitter, Inject, Input, Output } from "@angular/core";
import { Comparison } from "../comparison";
import { SBOM } from "@models/sbom";

import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from "@angular/material/dialog";
import { DataHandlerService } from "@services/data-handler.service";

@Component({
  selector: "app-comparison-page",
  templateUrl: "./comparison-page.component.html",
  styleUrls: ["./comparison-page.component.css"],
})
export class ComparisonPageComponent {
  collapsed: boolean = false;

  sbomInfoOpened: string | null = null;

  sboms: string[] = ["a", "b"];
  targetSbom!: string;

  constructor(
    public dialog: MatDialog,
    private dataHandler: DataHandlerService
  ) {}

  /** @TODO create an api call where you would send the target sbom and compare */
  // it against all sboms rather than doing singular api calls for each one  */
  selectTargetSbom($event: any) {
    this.targetSbom = $event;
  }

  // Display diff report
  compare() {
     // Get all the checkboxes in the DOM
     const checkboxes = document.querySelectorAll('input[type="checkbox"]');

     // Loop through each checkbox and check if it's selected and not disabled
     const selectedCheckboxes = [];
     for (let i = 0; i < checkboxes.length; i++) {
       const checkbox = checkboxes[i] as HTMLInputElement;
       if (checkbox.checked && !checkbox.disabled) {
         selectedCheckboxes.push(checkbox.value);
       }
     }

    this.dataHandler.Compare(this.targetSbom, selectedCheckboxes);
  }

  openDialog(sbom: SBOM): void {
    const dialogRef = this.dialog.open(ComparisonDialogComponent, {
      data: sbom,
    });
  }

  GetValidSBOMs() {
    return this.dataHandler.GetValidSBOMs();
  }

  GetDropdownSBOMs() {
    let keys = this.GetValidSBOMs();
    let data: { [id: string]: Object | null } = {};

    for(let i = 0; i < keys.length; i++) {
      let key = keys[i];
      let value = this.getSBOMAlias(key);

      data[key] = value;
    }

    return data;
  }

  getSBOMAlias(path: string) {
    return this.dataHandler.getSBOMAlias(path);
  }

  GetComparison() {
    return this.dataHandler.comparison;
  }

  getSBOMInfo(path: string) {
    this.sbomInfoOpened = path;
  }
}

@Component({
  selector: "app-comparison-dialog",
  template: `<app-dialog
    icon="delete"
    (clicked)="this.dialogRef.close(true)"
    buttonText="delete"
  >
    <span title>SBOM details</span>
    <div body>
      Identifier: {{ data.name }} Timestamp: {{ data.timestamp }} Publisher:
      {{ data.publisher }}
      <app-button (click)="this.dialogRef.close(false)">Close</app-button>
    </div>
  </app-dialog>`,
  styles: [],
})
export class ComparisonDialogComponent {
  @Output() removeSbom: EventEmitter<SBOM> = new EventEmitter<SBOM>();
  constructor(
    public dialogRef: MatDialogRef<ComparisonDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: SBOM
  ) {}
}