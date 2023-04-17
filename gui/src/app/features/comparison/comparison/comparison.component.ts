import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Comparison, comparisonMockup } from '../comparison';

@Component({
  selector: 'app-comparison',
  templateUrl: './comparison.component.html',
  styleUrls: ['./comparison.component.css']
})

export class ComparisonComponent {
  @Input() comparison: Comparison | null = null;
  path: number[] = [];
  depth: number = 0;
}
