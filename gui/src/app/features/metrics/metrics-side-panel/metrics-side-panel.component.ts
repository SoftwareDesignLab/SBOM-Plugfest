import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ModalComponent } from '@components/modal/modal.component';
import { DataHandlerService } from '@services/data-handler.service';

@Component({
  selector: 'app-metrics-side-panel',
  templateUrl: './metrics-side-panel.component.html',
  styleUrls: ['./metrics-side-panel.component.css']
})
export class MetricsSidePanelComponent {
  selectedSBOM: any;
  sbomInfoOpened: string | null = null;

  constructor(private handler: DataHandlerService, public dialog: MatDialog) {}

  GetSBOMs() {
    return this.handler.GetValidSBOMs();
  }

  SetSelectedSBOM(bom: string) {
    this.handler.selectedQualityReport = bom;
    this.selectedSBOM = bom;
  }

  getSBOMAlias(path: string) {
    return this.handler.getSBOMAlias(path);
  }

  getSBOMInfo(path: string) {
    this.sbomInfoOpened = path;
  }
}
