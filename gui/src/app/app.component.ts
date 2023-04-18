import { Component } from '@angular/core';
import { ComparisonService } from './features/comparison/report/comparison.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  /**
   *
   */
  constructor(private compare: ComparisonService) {
  }

  uploadFile($event: any) {
    console.log('val' + $event.target?.value)
    console.log('file' + $event.target?.files)
    this.compare.getComparisonReport($event.target.value)
  }
}
