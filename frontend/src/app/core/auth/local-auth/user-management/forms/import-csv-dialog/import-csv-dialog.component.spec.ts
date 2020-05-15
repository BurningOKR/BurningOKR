import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImportCsvDialogComponent } from './import-csv-dialog.component';
import { MatDialog, MatDialogRef } from '@angular/material';
import { Papa } from 'ngx-papaparse';
import { CsvUserParseService } from '../../services/csv-user-parse.service';
import { FormBuilder } from '@angular/forms';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';

describe('ImportCsvDialogComponent', () => {
  let component: ImportCsvDialogComponent;
  let fixture: ComponentFixture<ImportCsvDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ImportCsvDialogComponent],
      providers: [
        {provide: FormBuilder, useValue: {}},
        {provide: MatDialog, useValue: {}},
        {provide: CsvUserParseService, useValue: {}},
        {provide: Papa, useValue: {}},
        {provide: MatDialogRef, useValue: {}},
      ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
    })
      .compileComponents();
    fixture = TestBed.createComponent(ImportCsvDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
