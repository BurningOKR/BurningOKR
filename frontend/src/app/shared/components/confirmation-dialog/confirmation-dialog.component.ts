import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { I18n } from '@ngx-translate/i18n-polyfill';

export interface ConfirmationDialogData {
  title: string;
  message: string;
  confirmButtonText?: string;
}

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent {
  confirmButtonText: string = this.i18n({
    id: 'submitButtonText',
    value: 'Bestätigen'
  });
  cancelButtonText: string = this.i18n({
    id: 'cancelButtonText',
    value: 'Abbrechen'
  });

  constructor(
    private dialogRef: MatDialogRef<ConfirmationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public formData: ConfirmationDialogData,
    private i18n: I18n
  ) {
    if (formData.confirmButtonText) {
      this.confirmButtonText = formData.confirmButtonText;
    }
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
