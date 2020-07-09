import { Component, EventEmitter, OnInit } from '@angular/core';
import { InitStateFormComponent } from '../init-state-form/init-state-form.component';
import { InitState } from '../../../../../../shared/model/api/init-state';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-set-azure-admin-init-state-form',
  templateUrl: './set-azure-admin-init-state-form.component.html',
  styleUrls: ['./set-azure-admin-init-state-form.component.css']
})
export class SetAzureAdminInitStateFormComponent extends InitStateFormComponent {

  eventEmitter: EventEmitter<InitState>;
  form: FormGroup;

  constructor() {
    super();
  }
}
