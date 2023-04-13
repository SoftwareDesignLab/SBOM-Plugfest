import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-dropdown',
  templateUrl: './dropdown.component.html',
  styleUrls: ['../components.css']
})
export class DropdownComponent {
  @Input() options: string[] = [];
}
