import { Component, EventEmitter, Inject, Input, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DeleteDialogComponent } from '../delete-dialog/delete-dialog.component';

@Component({
  selector: 'app-dialog-component',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.scss']
})
export class DialogComponent<T> {
  @Input() title: string;

  @Input() saveAndCloseLabel = 'Speichern';
  @Input() formGroup: FormGroup = new FormGroup({});
  @Input() hasFormGroupError = false;
  @Input() isSaveButtonDisabled = false;
  @Input() formHasToBeEdited = false;
  @Output() okEmitter = new EventEmitter<T>();

  constructor(private dialogRef: MatDialogRef<DialogComponent<T>>,
              @Inject(MAT_DIALOG_DATA) private formData: any) {
  }

  closeDialog(): void {
    if (this.dialogRef.componentInstance instanceof DeleteDialogComponent) {
      this.dialogRef.close();
    } else {
      this.dialogRef.close(undefined);
    }
  }

  sendOk(): void {
    this.okEmitter.emit(this.formGroup.getRawValue());
  }
}
