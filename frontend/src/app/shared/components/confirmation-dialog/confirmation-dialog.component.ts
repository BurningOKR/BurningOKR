import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';

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
export class ConfirmationDialogComponent implements OnInit, OnDestroy {

  subscriptions: Subscription[] = [];
  confirmButtonText: string;
  cancelButtonText: string;

  constructor(
    private dialogRef: MatDialogRef<ConfirmationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public formData: ConfirmationDialogData,
    private translate: TranslateService
  ) {
    if (formData.confirmButtonText) {
      this.confirmButtonText = formData.confirmButtonText;
    }
  }

  ngOnInit(): void {
    this.subscriptions.push(this.translate.stream('confirmation-dialog.confirm-text').subscribe((text: string) => {
      this.confirmButtonText = text;
    }));
    this.subscriptions.push(this.translate.stream('confirmation-dialog.cancel-text').subscribe((text: string) => {
      this.cancelButtonText = text;
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  onNoClick(): void {
    this.dialogRef.close(false);
  }
}
