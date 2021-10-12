import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { User } from '../../model/api/user';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-user-selector-multi',
  templateUrl: './user-selector-multi.component.html',
  styleUrls: ['./user-selector-multi.component.css']
})
export class UserSelectorMultiComponent implements OnInit {

  @Input() placeholder: string = 'No Placeholder Set.';
  @Input() users$: Observable<User[]>;
  @Input() formGroup: FormGroup;
  @Input() controlName;
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
