import { User } from '../../model/api/user';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { debounceTime, filter, map, startWith, switchMap } from 'rxjs/operators';
import { UserService } from '../../services/helper/user.service';
import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { merge, Observable, Subject, Subscription } from 'rxjs';
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

  userList$: Observable<User[]>;
  filteredUsers$: Observable<User[]>;
  subscriptions: Subscription[] = [];
  private focusChanged$: Subject<string> = new Subject<string>();

  // Time to wait after new input before calculating the suggestions for autocomplete in ms
  private autoCompleteWaitTime: number = 200;

  constructor(private userService: UserService) {
  }

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

  selectedUser(event: MatAutocompleteSelectedEvent): void {
    const selectedUser: User = event.option.value;
    this.choseUser.emit(selectedUser);
  }

  focusChanged(): void {
    this.focusChanged$.next(this.inputFormControl.value);
  }

  private loadUserListFromService(): void {
    this.userList$ = this.userService
      .getAllUsers$()
      .pipe(
        map((users: User[]) =>
          users.sort((a, b) => {
            return a.surname < b.surname ? -1 : a.surname === b.surname ? 0 : 1;
          })
        )
      );
  }

  private setupFormControlAutocomplete(): void {
    this.filteredUsers$ = merge(this.inputFormControl.valueChanges, this.focusChanged$)
      .pipe(
        filter(value => typeof value === 'string'), // Hacky fix for JS ignoring parameter types
        debounceTime(this.autoCompleteWaitTime),
        startWith(''),
        switchMap(inputString => this.getFilteredUserList$(inputString))
      );
  }

  private getFilteredUserList$(inputString: string): Observable<User[]> {
    const lowerCaseInput: string = inputString.toLowerCase();

    return this.userList$.pipe(
      map((users: User[]) => {
        return users.filter(user => {
          const givenName: string = user.givenName.toLowerCase();
          const surname: string = user.surname.toLowerCase();

          return !this.isUserExcluded(user.id) && (givenName.includes(lowerCaseInput) || surname.includes(lowerCaseInput));
        });
      })
    );
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
}
