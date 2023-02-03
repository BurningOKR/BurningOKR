import { NgModule } from '@angular/core';
import { OkrUnitCardComponent } from './okr-unit-card/okr-unit-card.component';
import { OkrUnitDashboardComponent } from './okr-unit-dashboard/okr-unit-dashboard.component';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatMenuModule } from '@angular/material/menu';
import { SharedModule } from '../shared/shared.module';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTooltipModule } from '@angular/material/tooltip';
import { OkrUnitFormComponent } from './okr-unit-form/okr-unit-form.component';
import { DemoModule } from '../demo/demo.module';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [
    OkrUnitCardComponent,
    OkrUnitDashboardComponent,
    OkrUnitFormComponent,
  ],
  imports: [
    SharedModule,
    BrowserModule,
    BrowserAnimationsModule,
    CommonModule,
    FormsModule,
    MatDatepickerModule,
    MatIconModule,
    MatCardModule,
    MatTooltipModule,
    MatSelectModule,
    MatFormFieldModule,
    MatNativeDateModule,
    MatAutocompleteModule,
    MatMenuModule,
    ReactiveFormsModule,
    MatInputModule,
    DemoModule,
    TranslateModule,
  ],
  entryComponents: [
    OkrUnitFormComponent,
  ],
})
export class OkrUnitModule {
}
