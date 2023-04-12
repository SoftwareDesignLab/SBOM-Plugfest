// @author Tina DiLorenzo 
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-example-shared.ts',
  template: `<span> example`,
  styleUrls: ['../shared-styles.scss']
})
export class AppExample implements OnInit {

  /*An example of the simple format for a 1 file shared component*/
  constructor() { }

  ngOnInit() {
  }

}
