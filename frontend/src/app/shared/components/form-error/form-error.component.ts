import { Component, Input, OnInit } from '@angular/core';
import { ControlHelperService } from '../../services/helper/control-helper.service';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-form-error',
  templateUrl: './form-error.component.html',
  styleUrls: ['./form-error.component.css']
})
export class FormErrorComponent {

  @Input() control: FormControl;

  constructor(private controlHelperService: ControlHelperService,
  ) {
  }

  getErrorMessage(): string {
    return this.controlHelperService.getErrorMessage(this.control);
  }

}
