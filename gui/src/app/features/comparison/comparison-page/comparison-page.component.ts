/** @Author Tina DiLorenzo */

import { Component, EventEmitter, Inject, Input, Output } from '@angular/core';
import { Comparison, finalMockup } from '../comparison';
import { SBOM } from '@models/sbom';

import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';

@Component({
  selector: 'app-comparison-page',
  templateUrl: './comparison-page.component.html',
  styleUrls: ['./comparison-page.component.css'],
})
export class ComparisonPageComponent {
  collapsed: boolean = false;

  sboms: SBOM[] = [];
  targetSbom: SBOM | null = null; // number is for original position
  comparison: Comparison | null = null;

  constructor(public dialog: MatDialog) {}

  /** @TODO create an api call where you would send the target sbom and compare */
  // it against all sboms rather than doing singular api calls for each one  */
  selectTargetSbom($event: any) {
    this.targetSbom = $event;
  }

  /** @TODO replace with inserting the associated diff report */
  selectComparison($event: any) {
    //this.targetSbom = $event.target?.value
  }

  // Display diff report
  compare() {
    this.comparison = finalMockup;
  }

  openDialog(sbom: SBOM): void {
    const dialogRef = this.dialog.open(ComparisonDialogComponent, {
      data: sbom,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.sboms = this.sboms.filter((s) => s !== sbom);
      }
    });
  }
}

@Component({
  selector: 'app-comparison-dialog',
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
