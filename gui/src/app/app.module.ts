import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { MatExpansionModule } from '@angular/material/expansion'
import { MatMenuModule } from '@angular/material/menu';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SharedModule } from './shared/shared.module';
import { IconComponent } from './shared/components/icon/icon.component';
import { ButtonComponent } from './shared/components/button/button.component';
import { AccordionComponent } from './shared/components/accordion/accordion.component';
import { DropdownComponent } from './shared/components/dropdown/dropdown.component';
import { SidePanelComponent } from './features/comparison/side-panel/side-panel.component';
import { ComparisonComponent } from './features/comparison/comparison/comparison.component';
import { TextEmitterDirective } from './shared/directive/textEmitter.directive';

@NgModule({
  declarations: [
    AppComponent,
    IconComponent,
    ButtonComponent,
    AccordionComponent,
    IconComponent,
    DropdownComponent,
    SidePanelComponent,
    ComparisonComponent,
    TextEmitterDirective
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatExpansionModule,
    MatMenuModule,
    SharedModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
