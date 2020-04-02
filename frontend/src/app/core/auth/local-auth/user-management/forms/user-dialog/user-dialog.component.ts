import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { LocalUserManagementUser } from '../../user-management.component';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-user-dialog',
  templateUrl: './user-dialog.component.html',
  styleUrls: ['./user-dialog.component.css']
})
export class UserDialogComponent {
  @Input() title: string;
  @Input() userForm: FormGroup;
  @Input() resetPasswordIsShown: boolean = true;
  @Output() saveEmitter: EventEmitter<LocalUserManagementUser> = new EventEmitter<LocalUserManagementUser>();

  @ViewChild('canvasElement', {static: false}) canvas;
}
