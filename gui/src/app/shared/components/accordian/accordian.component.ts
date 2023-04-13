import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-accordian',
  templateUrl: './accordian.component.html',
  styleUrls: ['../components.css']
})
export class AccordianComponent {
  @Input() title: string = '';
  @Input() extra: string[] = [];
  @Input() color: string = '';
}
