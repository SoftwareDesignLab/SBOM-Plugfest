/** @Author Max Stein, Justin Jantzi */

import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-dropdown',
  templateUrl: './dropdown.component.html',
  styleUrls: ['../components.css']
})
export class DropdownComponent {
  @Input() options: { [id: string]: Object | null } = {};
  @Input() key: string = '';
  @Output() selectionChange = new EventEmitter<any>();

  selectedItem($event: any) {
    this.selectionChange.emit($event.source.value);
  }
}
