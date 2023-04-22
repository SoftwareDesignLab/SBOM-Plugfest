import { Component } from '@angular/core';
import { DataHandlerService } from '@services/data-handler.service';

@Component({
  selector: 'app-metrics-side-panel',
  templateUrl: './metrics-side-panel.component.html',
  styleUrls: ['./metrics-side-panel.component.css']
})
export class MetricsSidePanelComponent {

  constructor(private handeler: DataHandlerService) {}

  GetSBOMs() {
    return this.handeler.GetValidSBOMs();
  }

  SetSelectedSBOM(bom: string) {
    this.handeler.selectedQualityReport = bom;
  }
}
