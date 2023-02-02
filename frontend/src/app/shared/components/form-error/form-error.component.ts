import { Component, Input } from '@angular/core';
import { ValidationErrorService } from '../../services/helper/validation-error.service';
import { AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-form-error',
  templateUrl: './form-error.component.html',
  styleUrls: ['./form-error.component.css'],
})
export class FormErrorComponent {

  @Input() control: AbstractControl;

  constructor(private controlHelperService: ValidationErrorService,
  ) {
  }

  getErrorMessage(): string {
    return this.controlHelperService.getErrorMessage(this.control);
  }

}
