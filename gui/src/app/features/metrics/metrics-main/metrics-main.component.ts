import { Component } from '@angular/core';
import { DataHandlerService } from '@services/data-handler.service';

@Component({
  selector: 'app-metrics-main',
  templateUrl: './metrics-main.component.html',
  styleUrls: ['./metrics-main.component.css']
})
export class MetricsMainComponent {
  constructor(private dataHandler: DataHandlerService) {

  }
  isLoadingMetrics() {
    return this.dataHandler.IsLoadingMetrics()
  }

}
