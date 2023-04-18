import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Comparison } from '../comparison';

@Component({
  selector: 'app-comparison',
  templateUrl: './comparison.component.html',
  styleUrls: ['./comparison.component.css']
})

export class ComparisonComponent implements OnChanges{
  @Input() comparison: Comparison | null = null;
  display: any = null;
  keys: string[] = [];
  path: number[] = [];
  depth: number = 0;

  ngOnChanges(changes: SimpleChanges): void {
    if (this.comparison) {
      this.display = this.comparison?.comparisons;
      this.keys = Object.keys(this.display);
      console.log(this.keys);
    }
  }
}
