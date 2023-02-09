import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { ImportCsvDialogComponent } from './import-csv-dialog.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { Papa } from 'ngx-papaparse';
import { CsvUserParseService } from '../../services/csv-user-parse.service';
import { FormBuilder } from '@angular/forms';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { HAMMER_LOADER } from '@angular/platform-browser';
import { MaterialTestingModule } from '../../../../../../testing/material-testing.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('ImportCsvDialogComponent', () => {
  const formBuilder: FormBuilder = new FormBuilder();
  let component: ImportCsvDialogComponent;
  let fixture: ComponentFixture<ImportCsvDialogComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ImportCsvDialogComponent],
      providers: [
        { provide: FormBuilder, useValue: formBuilder },
        { provide: MatDialog, useValue: {} },
        { provide: CsvUserParseService, useValue: {} },
        { provide: Papa, useValue: {} },
        { provide: MatDialogRef, useValue: {} },
        { provide: HAMMER_LOADER, useValue: {} },
      ],
      imports: [MatTableModule, MaterialTestingModule, NoopAnimationsModule],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportCsvDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
