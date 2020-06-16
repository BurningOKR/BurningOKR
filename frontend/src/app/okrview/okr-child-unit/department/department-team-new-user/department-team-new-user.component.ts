import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { UserAutocompleteInputComponent } from '../../../../shared/components/user-autocomplete-input/user-autocomplete-input.component';
import { User } from '../../../../shared/model/api/user';

@Component({
  selector: 'app-department-team-new-user',
  templateUrl: './department-team-new-user.component.html',
  styleUrls: ['./department-team-new-user.component.scss']
})
export class DepartmentTeamNewUserComponent {
  @ViewChild('inputForm', { static: false }) inputForm: UserAutocompleteInputComponent;

  @Input()
  inputPlaceholderText: string;

  @Output()
  choseUser: EventEmitter<User> = new EventEmitter();

  autoCompleteChoseUser(chosenUser: User): void {
    this.choseUser.emit(chosenUser);
    this.inputForm.setFormText('');
  }
}
