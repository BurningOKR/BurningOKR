import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { UserService } from '../../shared/services/helper/user.service';
import { User } from '../../shared/model/api/user';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-okr-topic-description-form',
  templateUrl: './okr-topic-description-form.component.html',
  styleUrls: ['./okr-topic-description-form.component.css']
})
export class OkrTopicDescriptionFormComponent implements OnInit {

  @Input() descriptionForm: FormGroup;
  users$: Observable<User[]>;
  @Input() minBeginn: Date = new Date();
  userFilters: FormControl[] = [new FormControl()];

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.users$ = this.userService.getAllUsers$();
    this.userFilters.forEach(control => control.patchValue(''));
  }

  filter(user: User, filter: string): boolean {
    const lFilter: string  = filter.toLowerCase();

    return user.surname.toLowerCase()
      .includes(lFilter) ||
      user.givenName.toLowerCase()
      .includes(lFilter);
  }
}
