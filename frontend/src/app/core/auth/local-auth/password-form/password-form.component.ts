import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-password-form',
  templateUrl: './password-form.component.html',
  styleUrls: ['./password-form.component.css'],
})
export class PasswordFormComponent {
  @Input() newPasswordForm: FormGroup;
  @Input() previousPasswordNecessary: boolean;

  capsLock: boolean;
}
