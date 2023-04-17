import { Component, EventEmitter } from '@angular/core';
import { Comparison, comparisonMockup } from '../comparison';
import { SBOM } from '@models/sbom';

@Component({
  selector: 'app-comparison-page',
  templateUrl: './comparison-page.component.html',
  styleUrls: ['./comparison-page.component.css'],
})
export class ComparisonPageComponent {
  collapsed: boolean = false;

  sboms: SBOM[] = [];
  selectionSBOMS: SBOM[] = [];  // to display in options
  targetSbom: SBOM | null = null; // number is for original position
  comparisonSBOM: SBOM | null = null;
  comparison:Comparison| null = null;

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
      //this.comparison = [this.targetSbom, this.comparisonSBOM];
      this.comparison = comparisonMockup;
    }
  }
}

