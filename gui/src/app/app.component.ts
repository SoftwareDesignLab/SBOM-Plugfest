import { Component } from '@angular/core';
import { DataHandlerService } from '@services/data-handler.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  constructor(private dataHandeler: DataHandlerService) {}

  HasTwoValidSBOMS() {
    return this.dataHandeler.GetValidSBOMs().length >= 2;
  }
}
