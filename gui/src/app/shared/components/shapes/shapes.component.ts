import { Component, Input, OnInit } from '@angular/core';
import shape from '../../enum/shape';

@Component({
  selector: 'app-shapes',
  templateUrl: './shapes.component.html',
  styleUrls: ['./shapes.component.css']
})
export class ShapesComponent {
  @Input() shape!: shape;
  @Input() color: string = 'var(--accent)';
  @Input() size: number = 100;
}
