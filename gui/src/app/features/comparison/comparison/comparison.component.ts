/** @Author Tina DiLorenzo */
import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Comparison } from '../comparison';

@Component({
  selector: 'app-comparison',
  templateUrl: './comparison.component.html',
  styleUrls: ['./comparison.component.css'],
})
export class ComparisonComponent implements OnChanges {
  @Input() comparison: Comparison | null = null;
  display: any = null;
  keys: string[] = [];
  path: any[] = [];
  pathTitles: string[] = [];
  readonly attributes = ['purls', 'cpes', 'swids'];

  ngOnChanges(changes: SimpleChanges): void {
    if (this.comparison) {
      this.display = this.comparison?.comparisons;
      this.keys = Object.keys(this.display);
      console.log(this.keys);
      this.path = [this.comparison.comparisons];
      this.pathTitles = ['Components'];
    }
  }

  increaseDepth(newLocation: any, pathTitles: string) {
    this.path.push(newLocation);
    this.pathTitles.push(pathTitles);
  }

  decreaseDepth(index: number) {
    if (index < this.path.length - 1) {
      this.pathTitles = this.pathTitles.slice(0, index + 1);
      this.path = this.path.slice(0, index + 1);
    }
  }

  /** @TODO REPLACE WITH PIPE */
  getKeys(obj: any) {
    return Object.keys(obj);
  }
}
