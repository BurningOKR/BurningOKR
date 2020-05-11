import { Component, EventEmitter } from '@angular/core';
import { InitStateFormComponent } from '../init-state-form/init-state-form.component';
import { FormGroup } from '@angular/forms';
import { InitState } from '../../../../../../../shared/model/api/init-state';

@Component({
  selector: 'app-complete-init-state-form',
  templateUrl: './complete-init-state-form.component.html',
  styleUrls: ['./complete-init-state-form.component.css']
})
export class CompleteInitStateFormComponent extends InitStateFormComponent {
  form: FormGroup;
  eventEmitter: EventEmitter<InitState> = new EventEmitter<InitState>();
}
