import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Comparison, comparisonMockup } from '../comparison';

@Component({
  selector: 'app-comparison',
  templateUrl: './comparison.component.html',
  styleUrls: ['./comparison.component.css']
})

export class ComparisonComponent implements OnChanges {
  @Input() comparison: Comparison | null = comparisonMockup;
  readonly attributeKeys = ['comparisons', '']
  path: number[] = [];
  depth: number = 0;

  ngOnChanges(): void {
    this.comparison = comparisonMockup;
  }
}
