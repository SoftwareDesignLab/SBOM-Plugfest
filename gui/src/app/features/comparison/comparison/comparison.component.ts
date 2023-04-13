import { Component, OnInit } from '@angular/core';
import {mockupDiffReport, DiffReport} from '../diffreport';

@Component({
  selector: 'app-comparison',
  templateUrl: './comparison.component.html',
  styleUrls: ['./comparison.component.css']
})
export class ComparisonComponent implements OnInit {

  //@Input() diffReport: DiffReport;
  diffReport: DiffReport = mockupDiffReport;
  path: string[] = ['components'];
  step: string = 'components';

  // I was just going to add each step thru the sbom to the path to navigate.
  // step would be the actual view atm
  // when u go back u just pop/slice items out of the array 

  constructor() { }

  ngOnInit() {
  }

}
