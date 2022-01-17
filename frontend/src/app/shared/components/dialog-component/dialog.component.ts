import { Component, EventEmitter, HostListener, Inject, Input, OnInit, Output, ViewEncapsulation } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DeleteDialogComponent } from '../delete-dialog/delete-dialog.component';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-dialog-component',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DialogComponent<T> implements OnInit{
  @Input() title: string;
  @Input() saveAndCloseLabel;
  @Input() formGroup: FormGroup = new FormGroup({});
  @Input() hasFormGroupError = false;
  @Input() isSaveButtonDisabled = false;
  @Input() formHasToBeEdited = false;
  @Input() needsCancelButton: boolean = true;
  @Output() okEmitter = new EventEmitter<T>();

  NO_ENTER_TAGS: string[] = ['TEXTAREA', 'MAT-SELECT', 'BUTTON'];

  constructor(private dialogRef: MatDialogRef<DialogComponent<T>>,
              @Inject(MAT_DIALOG_DATA) private formData: any,
              private translate: TranslateService,
  ) {
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

  handleEnter(event): void {
    if (!this.NO_ENTER_TAGS.includes(event.target.tagName)
        && event.target.type !== 'checkbox'
        && !this.saveDisabled()) {
      this.sendOk();
    }
  }

  saveDisabled(): boolean {
    return (this.isSaveButtonDisabled || this.formGroup.invalid || this.formHasToBeEdited && this.formGroup.pristine);
  }

  ngOnInit(): void {
    this.translate.get('dialog-component.save').subscribe((text: string) => {
      if(!this.saveAndCloseLabel){
        this.saveAndCloseLabel = text;
      }
    });
  }
}
