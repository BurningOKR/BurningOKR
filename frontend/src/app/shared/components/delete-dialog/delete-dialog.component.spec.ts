import { TestBed } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA, Injectable, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DeleteDialogComponent } from './delete-dialog.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DeleteDialogData } from '../../model/ui/delete-dialog-data';
import { MaterialTestingModule } from '../../../testing/material-testing.module';

@Injectable()
class MockDeleteDialogData {
}

describe('DeleteDialogComponent', () => {
  let fixture: any;
  let component: {
    ngOnDestroy: () => void;
    dialogRef: { close?: any };
    confirmDelete: () => void;
    closeDialog: () => void;
    generateDeleteDialogData: (param1: string, param2: string, param3: string | undefined) => DeleteDialogData;
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        FormsModule,
        ReactiveFormsModule,
        MaterialTestingModule,
      ],
      declarations: [
        DeleteDialogComponent
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        {provide: MatDialogRef, useValue: {}},
        {provide: MAT_DIALOG_DATA, useValue: {}},
      ]
    })
      .overrideComponent(DeleteDialogComponent, {})
      .compileComponents();
    fixture = TestBed.createComponent(DeleteDialogComponent);
    component = fixture.debugElement.componentInstance;
  });

  afterEach(() => {
    fixture.destroy();
  });

  it('should run #constructor()', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should run #confirmDelete()', () => {
    component.dialogRef = component.dialogRef || {};
    component.dialogRef.close = jest.fn();
    component.confirmDelete();
    expect(component.dialogRef.close)
        .toHaveBeenCalled();
  });

  it('should run #closeDialog()', () => {
    component.dialogRef = component.dialogRef || {};
    component.dialogRef.close = jest.fn();
    component.closeDialog();
    expect(component.dialogRef.close)
        .toHaveBeenCalled();
  });
});
