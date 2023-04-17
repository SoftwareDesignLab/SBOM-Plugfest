import { Component, Input, OnInit, EventEmitter, Output } from '@angular/core';
import { Comparison, comparisonMockup } from '../comparison';
import { SBOM } from '@models/sbom';

@Component({
  selector: 'app-side-panel',
  templateUrl: './side-panel.component.html',
  styleUrls: ['./side-panel.component.css'],
})
export class SidePanelComponent {
  collapsed: boolean = false;

  sboms: SBOM[] = [];
  selectionSBOMS: SBOM[] = [];  // to display in options
  targetSbom: SBOM | null = null; // number is for original position
  comparisonSBOM: SBOM | null = null;

  compareSboms: EventEmitter<SBOM[]> = new EventEmitter();
  
  constructor() {}

  uploadSbom($event: any) {
    // @TODO: FILE UPLOAD HERE
    // apiCall with event.target.files
    this.sboms = [{ name: 'SBOM A' }, { name: 'SBOM B' }, { name: 'SBOM C' }];
    this.selectionSBOMS = this.sboms;
  }


  selectTargetSbom($event: any) {
    this.targetSbom = $event;
    this.selectionSBOMS = this.sboms.filter(
      (s) => s.name !== this.targetSbom?.name
    );
    this.compare();
  }

  selectComparison($event: any){
    this.comparisonSBOM = $event.target?.value || null
    this.compare();
  }

  compare() {
    if (this.targetSbom && this.comparisonSBOM) {
      const comparisonSBOMS = [this.targetSbom, this.comparisonSBOM];
      this.compareSboms.emit(comparisonSBOMS);
    }
  }
}
