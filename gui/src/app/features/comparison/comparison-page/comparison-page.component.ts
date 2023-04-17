import { Component } from '@angular/core';
import { Comparison, comparisonMockup } from '../comparison';

@Component({
  selector: 'app-comparison-page',
  templateUrl: './comparison-page.component.html',
  styleUrls: ['./comparison-page.component.css'],
})
export class ComparisonPageComponent {
  comparison: Comparison | null = null;
  // @TODO: apply api calls here with SBOM A & SBOM B
  constructor() {}

  compare() {
    this.comparison = comparisonMockup;
  }
}
