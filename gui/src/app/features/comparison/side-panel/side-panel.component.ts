import { Component } from '@angular/core';

@Component({
  selector: 'app-side-panel',
  templateUrl: './side-panel.component.html',
  styleUrls: ['./side-panel.component.css']
})
export class SidePanelComponent {
  collapsed: boolean = false;
  sboms: string[] = ['SBOM B', 'SBOM C'];
  selected: string = "SBOM A";
  
}
