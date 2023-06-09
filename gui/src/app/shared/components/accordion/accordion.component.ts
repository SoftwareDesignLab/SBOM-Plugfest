/** @Author Justin Jantzi */

import { Component, Input } from '@angular/core';
import shape from '../../enum/shape';

@Component({
  selector: 'app-accordion',
  templateUrl: './accordion.component.html',
  styleUrls: ['../components.css']
})
export class AccordionComponent {
  @Input() title: string = '';
  @Input() extra: string[] = [];
  @Input() color: string = '';
  @Input() shape: shape | null = null;
  @Input() expanded: boolean = false;
  @Input() toggle: boolean = false;
  @Input() backgroundColor: string ='';
}
