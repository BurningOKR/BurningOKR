import { Component, OnInit, ViewChild } from '@angular/core';
import { MatCheckboxChange, MatDialog, MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { filter, map, switchMap, take } from 'rxjs/operators';
import { Router } from '@angular/router';
import { User } from '../../../../shared/model/api/user';
import { BehaviorSubject, combineLatest, forkJoin, Observable, of } from 'rxjs';
import { LocalUserApiService } from '../../../../shared/services/api/local-user-api.service';
import { ConfirmationDialogComponent } from '../../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { ImportCsvDialogComponent } from './forms/import-csv-dialog/import-csv-dialog.component';
import { UserDialogData } from './forms/user-dialog-data';
import { UserDialogComponent } from './forms/user-dialog/user-dialog.component';
import { CurrentUserService } from '../../../services/current-user.service';

export interface LocalUserManagementUser extends User {
  isAdmin: boolean;
}

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {

  private users$: BehaviorSubject<LocalUserManagementUser[]> = new BehaviorSubject<LocalUserManagementUser[]>([]);
  private filteredUsers$: BehaviorSubject<LocalUserManagementUser[]> = new BehaviorSubject<LocalUserManagementUser[]>([]);
  currentUser$: BehaviorSubject<User> = new BehaviorSubject<User>(new User());

  columnsToDisplay = ['photo', 'active', 'email', 'givenName', 'department', 'jobTitle', 'isAdmin', 'actions'];
  rowData = new MatTableDataSource([] as User[]);

  showDeactivatedUsers: boolean = false;

  constructor(
    private currentUserService: CurrentUserService,
    private dialog: MatDialog,
    private router: Router,
    private userService: LocalUserApiService
  ) {
  }

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  private getUserCreationDialogData(): { data: UserDialogData } {
    return {
      data: {
        title: 'Benutzer erstellen',
      }
    };
  }

  ngOnInit(): void {
    this.initializeUsers();
    this.initializeCurrentUser();
    this.rowData.sort = this.sort;
    this.rowData.paginator = this.paginator;
    this.users$.asObservable()
      .subscribe(users => {
        const filteredUsers: LocalUserManagementUser[] = this.filterUsers(users);
        this.filteredUsers$.next(filteredUsers);
      });

    this.filteredUsers$.subscribe(users => {
      this.rowData.data = users;
    });
  }

  handleEdit(userToEdit: LocalUserManagementUser): void {
    this.dialog.open(UserDialogComponent, this.getEditDialogData(userToEdit))
      .afterClosed()
      .pipe(
        filter(v => {
          return v;
        }),
        switchMap((editedUser: LocalUserManagementUser) => {
          return combineLatest([of(editedUser), this.userService.getAdminIds$()]);
        }),
        switchMap(([editedUser, adminIds]: [LocalUserManagementUser, string[]]) => {
          if (editedUser.isAdmin) {
            return this.updateUserAndSetToAdminIfChanged$(adminIds, editedUser);
          } else {
            return this.updateUserAndRemoveAdminIfChanged$(adminIds, editedUser);
          }
        })
      )
      .subscribe((editedUser: LocalUserManagementUser) => {
        this.editUserInTable(userToEdit, editedUser);
      });
  }

  private updateUserAndRemoveAdminIfChanged$(adminIds: string[], editedUser: LocalUserManagementUser): Observable<LocalUserManagementUser> {
    if (adminIds.Contains(editedUser.id)) {
      return forkJoin([
        this.userService.putUser$(editedUser)
          .pipe(map((user: LocalUserManagementUser) => {
            user.isAdmin = false;

            return user;
          })),
        this.userService.deleteAdmin$(editedUser.id)]
      )
        .pipe(map(([user, _]: [LocalUserManagementUser, boolean]) => user));
    } else {
      return this.userService.putUser$(editedUser)
        .pipe(map((user: LocalUserManagementUser) => {
          user.isAdmin = adminIds.Contains(user.id);

          return user;
        }));
    }
  }

  private updateUserAndSetToAdminIfChanged$(adminIds: string[], editedUser: LocalUserManagementUser): Observable<LocalUserManagementUser> {
    if (!adminIds.Contains(editedUser.id)) {
      return forkJoin([
        this.userService.putUser$(editedUser)
          .pipe(map((user: LocalUserManagementUser) => {
            user.isAdmin = true;

            return user;
          })),
        this.userService.addAdmin$(editedUser)]
      )
        .pipe(map(([user, _]: [LocalUserManagementUser, User]) => user));
    } else {
      return this.userService.putUser$(editedUser)
        .pipe(map((user: LocalUserManagementUser) => {
          user.isAdmin = adminIds.Contains(user.id);

          return user;
        }));
    }
  }

  private editUserInTable(userToEdit: LocalUserManagementUser, editedUser: LocalUserManagementUser): void {
    const indexOfUserToEdit: number = this.users$.getValue()
      .indexOf(userToEdit);
    const newList: LocalUserManagementUser[] = this.users$.getValue();
    newList[indexOfUserToEdit] = editedUser;
    this.users$.next(newList);
  }

  applyFilter(filterValue: string): void {
    this.rowData.filter = filterValue
      .trim()
      .toLowerCase();
  }

  handleDeactivate(userToDeactivate: LocalUserManagementUser): void {
    this.dialog.open(ConfirmationDialogComponent, this.getConfirmDeactivateDialogData(userToDeactivate))
      .afterClosed()
      .pipe(
        filter(v => v),
        switchMap(() => {
          return this.userService.deactivateUser$(userToDeactivate.id);
        })
      )
      .subscribe(() => {
        const deactivatedUser: LocalUserManagementUser = userToDeactivate;
        deactivatedUser.active = false;
        this.editUserInTable(userToDeactivate, deactivatedUser);
      });
  }

  handleActivate(userToBeActivated: LocalUserManagementUser): void {
    this.dialog.open(ConfirmationDialogComponent, this.getConfirmActivateDialogData(userToBeActivated))
      .afterClosed()
      .pipe(
        filter(v => v),
        switchMap(() => {
          const userWithActiveFlagSet: LocalUserManagementUser = userToBeActivated;

          userWithActiveFlagSet.active = true;

          return this.userService.putUser$(userWithActiveFlagSet);
        })
      )
      .subscribe((activatedUser: LocalUserManagementUser) => {
        activatedUser.isAdmin = userToBeActivated.isAdmin;
        this.editUserInTable(userToBeActivated, activatedUser);
      });
  }

  handleCreate(): void {
    this.dialog.open(UserDialogComponent, this.getUserCreationDialogData())
      .afterClosed()
      .pipe(
        filter(v => v)
      )
      .subscribe((user: LocalUserManagementUser) => {
        this.createNewUser(user);
      });
  }

  getConfirmDeactivateDialogData(user: LocalUserManagementUser): { data: { title: string; message: string }; width: string } {
    return {
      width: '500px',
      data: {
        title: 'Benutzer deaktivieren',
        message: `Möchten Sie ${user.givenName} ${user.surname} wirklich deaktivieren?
         Der Benutzer kann die Anwendung danach nicht mehr verwenden
          und wird beim nächsten Zykluswechsel automatisch aus allen Teams entfernt.`,
      }
    };
  }

  getConfirmActivateDialogData(user: LocalUserManagementUser): { data: { title: string; message: string }; width: string } {
    return {
      width: '500px',
      data: {
        title: 'Benutzer reaktivieren',
        message: `Möchten Sie ${user.givenName} ${user.surname} reaktivieren?
         Der Benutzer kann die Anwendung danach wieder in vollem Umfang verwenden.`,
      }
    };
  }

  getEditDialogData(user: LocalUserManagementUser): { data: UserDialogData } {
    return {
      data: {
        title: 'Benutzer bearbeiten',
        user
      }
    };
  }

  getResetPasswordDialogData(user: LocalUserManagementUser): { data: UserDialogData } {
    return {
      data: {
        title: 'Passwort zurücksetzen',
        user
      }
    };
  }

  private initializeUsers(): void {
    combineLatest([this.userService.getUsers$(), this.userService.getAdminIds$()])
      .pipe(
        take(1),
        map(([users, adminIds]: [User[], string[]]) => {
          return users.map((user: LocalUserManagementUser) => {
            if (adminIds.Contains(user.id)) {
              user.isAdmin = true;
            }

            return user;
          });
        })
      )
      .subscribe(users => {
        this.users$.next(users);
      });
  }

  private initializeCurrentUser(): void {
    this.currentUserService.getCurrentUser$()
      .pipe(take(1))
      .subscribe((reveived: User) => {
        this.currentUser$.next(reveived);
      });
  }

  private createNewUser(user: LocalUserManagementUser): void {
    this.createNewUserOnServer$(user)
      .subscribe((newUser: LocalUserManagementUser) => {
          this.addUserToTable(newUser);
        }
      );
  }

  private addUserToTable(user: LocalUserManagementUser): void {
    const userListToUpdate: LocalUserManagementUser[] = this.users$.getValue();
    userListToUpdate.push(user);
    this.users$.next(userListToUpdate);
  }

  private createNewUserOnServer$(user: LocalUserManagementUser): Observable<LocalUserManagementUser> {
    return this.userService.createUser$(user)
      .pipe(
        take(1),
        switchMap((newUser: User) => {
          if (user.isAdmin) {
            return this.userService.addAdmin$(newUser)
              .pipe(switchMap(_ => of(newUser)));
          } else {
            return of(newUser);
          }
        }),
        map((newUser: User) => {
          const newViewUser: LocalUserManagementUser = newUser as LocalUserManagementUser;
          newViewUser.isAdmin = user.isAdmin;

          return newViewUser;
        })
      );
  }

  navigateToCompanies(): void {
    this.router.navigate(['/companies'])
      .catch();
  }

  onShowDeactivatedUserChange($event: MatCheckboxChange): void {
    this.showDeactivatedUsers = $event.checked;
    this.filteredUsers$.next(this.filterUsers(this.users$.getValue()));
  }

  private filterUsers(users: LocalUserManagementUser[]): LocalUserManagementUser[] {
    let filteredUsers: LocalUserManagementUser[] = users;

    if (!this.showDeactivatedUsers) {
      filteredUsers = filteredUsers.Where(user => user.active);
    }

    return filteredUsers;
  }

  handleCsvImport(): void {
    this.dialog.open(ImportCsvDialogComponent)
      .afterClosed()
      .pipe(
        filter(v => v),
        switchMap(users => {
          return this.userService.bulkCreateUsers$(users);
        }),
      )
      .subscribe(users => {
        const localUsers: LocalUserManagementUser[] = users.map(user => {
          return user as LocalUserManagementUser;
        });
        localUsers.forEach(user => this.addUserToTable(user));
      });
  }

}
