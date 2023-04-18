import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { MatExpansionModule } from '@angular/material/expansion'
import { MatMenuModule } from '@angular/material/menu';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SharedModule } from './shared/shared.module';
import { IconComponent } from './shared/components/icon/icon.component';
import { ButtonComponent } from './shared/components/button/button.component';
import { AccordionComponent } from './shared/components/accordion/accordion.component';
import { DropdownComponent } from '@components/dropdown/dropdown.component';
import { SidePanelComponent } from './shared/components/side-panel/side-panel.component';
import { MetricsSidePanelComponent } from './features/metrics/metrics-side-panel/metrics-side-panel.component';
import { MetricsBodyComponent } from './features/metrics/metrics-body/metrics-body.component';

@NgModule({
  declarations: [
    AppComponent,
    IconComponent,
    ButtonComponent,
    AccordionComponent,
    IconComponent,
    DropdownComponent,
    SidePanelComponent,
    MetricsSidePanelComponent,
    MetricsBodyComponent
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
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
