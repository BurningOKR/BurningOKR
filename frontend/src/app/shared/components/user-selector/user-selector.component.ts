import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { User } from '../../model/api/user';

@Component({
  selector: 'app-user-selector',
  templateUrl: './user-selector.component.html',
  styleUrls: ['./user-selector.component.css']
})
export class UserSelectorComponent implements OnInit {

  @Input() users$: Observable<User[]>;
  @Input() placeholderText: string = 'Placeholder not set';
  @Input() resultControl: FormControl;
  @Input() noUser: string = 'No User';
  userFilter: FormControl = new FormControl();

  ngOnInit(): void {
    this.userFilter.patchValue('');
  }

  filter(user: User, filter: string): boolean {
    const lFilter: string  = filter.toLowerCase();

    return user.surname.toLowerCase()
        .includes(lFilter) ||
      user.givenName.toLowerCase()
        .includes(lFilter);
  }
}
