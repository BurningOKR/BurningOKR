import { Component, Input, OnInit } from '@angular/core';
import { ValidationErrorService } from '../../services/helper/validation-error.service';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-form-error',
  templateUrl: './form-error.component.html',
  styleUrls: ['./form-error.component.css']
})
export class FormErrorComponent {

  @Input() control: FormControl;

  constructor(private controlHelperService: ValidationErrorService,
  ) {
  }

  getErrorMessage(): string {
    return this.controlHelperService.getErrorMessage(this.control);
  }

}
