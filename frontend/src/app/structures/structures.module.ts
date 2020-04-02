import { NgModule } from '@angular/core';
import { StructureCardComponent } from './structure-card/structure-card.component';
import { StructureDashboardComponent } from './structures-dashboard/structure-dashboard.component';
import { AppRoutingModule } from '../app-routing.module';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatMenuModule } from '@angular/material/menu';
import { SharedModule } from '../shared/shared.module';
import {
  MatCardModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatSelectModule,
  MatTooltipModule
} from '@angular/material';
import { StructureFormComponent } from './structure-form/structure-form.component';

@NgModule({
  declarations: [
    StructureCardComponent,
    StructureDashboardComponent,
    StructureFormComponent,
  ],
  imports: [
    SharedModule,
    AppRoutingModule,
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
  ],
  entryComponents: [
    StructureFormComponent,
    ]
})
export class StructuresModule {}
