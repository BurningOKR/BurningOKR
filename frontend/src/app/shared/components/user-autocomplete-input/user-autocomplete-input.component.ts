import { User } from '../../model/api/user';
import { MatAutocompleteSelectedEvent } from '@angular/material';
import { debounceTime, distinctUntilChanged, filter, map, startWith } from 'rxjs/operators';
import { UserMapper } from '../../services/mapper/user.mapper';
import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-user-autocomplete-input',
  templateUrl: './user-autocomplete-input.component.html',
  styleUrls: ['./user-autocomplete-input.component.scss']
})
export class UserAutocompleteInputComponent implements OnInit, OnDestroy {
  @Input() placeHolderText: string = 'Set placeholder text';
  @Input() userIdsToExclude: string[] = [];
  @Output() choseUser = new EventEmitter<User>();
  @ViewChild('inputField', { static: true }) inputField: ElementRef;

  inputFormControl = new FormControl();

  userList: User[];
  filteredUsers$: Observable<User[]>;
  subscriptions: Subscription[] = [];

  // Time to wait after new input before calculating the suggestions for autocomplete in ms
  private autoCompleteWaitTime: number = 200;

  constructor(private userMapperService: UserMapper) {}

  ngOnInit(): void {
    this.loadUserListFromService();
    this.setupFormControlAutocomplete();
  }
  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }

  setIsDisabled(isDisabled: boolean): void {
    if (isDisabled) {
      this.inputFormControl.disable();
    } else {
      this.inputFormControl.enable();
    }
  }

  setFocusOnInput(): void {
    this.inputField.nativeElement.focus();
  }

  setFormText(newText: string): void {
    this.inputFormControl.setValue(newText);
  }

  getDisplayName(selectedUser: User): string | User {
    return selectedUser ? `${selectedUser.surname}, ${selectedUser.givenName}` : selectedUser;
  }

  private loadUserListFromService(): void {
    this.subscriptions.push(
      this.userMapperService
        .getAllUsers$()
        .pipe(
          map((users: User[]) =>
            users.sort((a, b) => {
              return a.surname < b.surname ? -1 : a.surname === b.surname ? 0 : 1;
            })
          )
        )
        .subscribe((users: User[]) => {
          this.userList = users;
        })
    );
  }

  private setupFormControlAutocomplete(): void {
    this.filteredUsers$ = this.inputFormControl.valueChanges.pipe(
      filter(value => typeof value === 'string'), // Hacky fix for JS ignoring parameter types
      debounceTime(this.autoCompleteWaitTime),
      distinctUntilChanged(),
      startWith(''),
      map(inputString => this.getFilteredUserList(inputString))
    );
  }

  private getFilteredUserList(inputString: string): User[] {
    const lowerCaseInput: string = inputString.toLowerCase();

    return this.userList.filter(user => {
      const givenName: string = user.givenName.toLowerCase();
      const surname: string = user.surname.toLowerCase();

      return !this.isUserExcluded(user.id) && (givenName.includes(lowerCaseInput) || surname.includes(lowerCaseInput));
    });
  }

  private isUserExcluded(userIdToExlcude: string): boolean {
    if (this.userIdsToExclude) {
      for (const userId of this.userIdsToExclude) {
        if (userId === userIdToExlcude) {
          return true;
        }
      }
    }

    return false;
  }

  selectedUser(event: MatAutocompleteSelectedEvent): void {
    const selectedUser: User = event.option.value;
    this.choseUser.emit(selectedUser);
  }
}
