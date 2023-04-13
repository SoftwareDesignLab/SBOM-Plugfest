import { Component, OnInit } from '@angular/core';
import {mockupDiffReport, DiffReport} from '../diffreport';

@Component({
  selector: 'app-comparison',
  templateUrl: './comparison.component.html',
  styleUrls: ['./comparison.component.css']
})

export class ComparisonComponent {
  //@Input() diffReport: DiffReport;
  diffReport: DiffReport = mockupDiffReport;
  path: number[] = [];
  depth: number = 0;
}
