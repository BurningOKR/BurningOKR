import { Component, EventEmitter, OnInit } from '@angular/core';
import { InitStateFormComponent } from '../init-state-form/init-state-form.component';
import { FormGroup } from '@angular/forms';
import { InitState } from '../../../../../../../shared/model/api/init-state';

@Component({
  selector: 'app-welcome-init-state-form',
  templateUrl: './welcome-init-state-form.component.html',
  styleUrls: ['./welcome-init-state-form.component.css']
})
export class WelcomeInitStateFormComponent implements InitStateFormComponent {
  form: FormGroup;
  eventEmitter: EventEmitter<InitState>;
}
