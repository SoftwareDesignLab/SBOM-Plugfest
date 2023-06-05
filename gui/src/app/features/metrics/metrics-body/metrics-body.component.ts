import { Component, Input, SimpleChanges, OnChanges } from '@angular/core';
import { qualityReport } from '../qualityReport';
import { DataHandlerService } from '@services/data-handler.service';

@Component({
  selector: 'app-metrics-body',
  templateUrl: './metrics-body.component.html',
  styleUrls: ['./metrics-body.component.css']
})
export class MetricsBodyComponent implements OnChanges {
  constructor(private handler: DataHandlerService) {}

  //TODO: convert to input
  GetQualityReport(): any {
    if(!this.handler.selectedQualityReport)
      return null;

    return this.handler.GetSBOMInfo(this.handler.selectedQualityReport).metrics;
  }

  getPassedPercent(testResult: any) {
    return (testResult.successfulTests / testResult.tests.length * 100).toFixed(2);
  }

  ngOnChanges(changes: SimpleChanges) {
   this.handler.RunAllMetrics();
  }
}
