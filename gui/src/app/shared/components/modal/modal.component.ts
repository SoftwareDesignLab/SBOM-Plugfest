import { Component, Input, EventEmitter, Output } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})

export class ModalComponent {
  @Input() opened: boolean = false;
  @Output() close = new EventEmitter<Boolean>();
  
  Close() {
    this.close.emit(true);
  }
}