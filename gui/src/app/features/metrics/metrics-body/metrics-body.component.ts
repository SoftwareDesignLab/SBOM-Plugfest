import { Component, Input } from '@angular/core';
import { qualityReport } from '../qualityReport';

@Component({
  selector: 'app-metrics-body',
  templateUrl: './metrics-body.component.html',
  styleUrls: ['./metrics-body.component.css']
})
export class MetricsBodyComponent {
  @Input() report!: qualityReport;
}
