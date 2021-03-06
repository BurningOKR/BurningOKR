import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImportCsvDialogComponent } from './import-csv-dialog.component';
import { MatDialog, MatDialogRef, MatTableModule } from '@angular/material';
import { Papa } from 'ngx-papaparse';
import { CsvUserParseService } from '../../services/csv-user-parse.service';
import { FormBuilder } from '@angular/forms';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { HAMMER_LOADER } from '@angular/platform-browser';
import { i18nMock } from '../../../../../../shared/mocks/i18n-mock';
import { I18n } from '@ngx-translate/i18n-polyfill';
import 'linq4js';

describe('ImportCsvDialogComponent', () => {
  const formBuilder: FormBuilder = new FormBuilder();
  let component: ImportCsvDialogComponent;
  let fixture: ComponentFixture<ImportCsvDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ImportCsvDialogComponent],
      providers: [
        {provide: FormBuilder, useValue: formBuilder},
        {provide: MatDialog, useValue: {}},
        {provide: CsvUserParseService, useValue: {}},
        {provide: Papa, useValue: {}},
        {provide: MatDialogRef, useValue: {}},
        {provide: HAMMER_LOADER, useValue: {}},
        {provide: I18n, useValue: i18nMock},
      ],
      imports: [MatTableModule],
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
