import { User, UserId } from '../shared/model/api/user';
import { UserMapper } from '../shared/services/mapper/user.mapper';
import { CurrentUserService } from '../core/services/current-user.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { UserAutocompleteInputComponent } from '../shared/components/user-autocomplete-input/user-autocomplete-input.component';
import { MatDialog, MatDialogRef } from '@angular/material';
import { map, take } from 'rxjs/operators';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData
} from '../shared/components/confirmation-dialog/confirmation-dialog.component';
import { combineLatest, Observable, ReplaySubject, Subject, Subscription } from 'rxjs';
import { UserApiService } from '../shared/services/api/user-api.service';
import { I18n } from '@ngx-translate/i18n-polyfill';

@Component({
  selector: 'app-admin-view',
  templateUrl: './admin-view.component.html',
  styleUrls: ['./admin-view.component.scss'],
})
export class AdminViewComponent implements OnInit {
  @ViewChild('newAdminForm', {static: false}) newAdminForm: UserAutocompleteInputComponent;

  adminUsers$: Subject<User[]> = new ReplaySubject<User[]>(1);
  subscriptions: Subscription[] = [];

  currentUserId$: Observable<UserId>;
  excludedIdsShouldUpdate: any;

  constructor(
    private userApiService: UserApiService,
    private userMapperService: UserMapper,
    private currentUserService: CurrentUserService,
    private matDialog: MatDialog,
    private router: Router,
    private i18n: I18n
  ) {
  }

  ngOnInit(): void {
    this.currentUserId$ = this.generateUserIdObservable();

    this.getAdminUsers$();
  }

  private generateUserIdObservable(): Observable<UserId> {
    return this.currentUserService.getCurrentUser()
      .pipe(
        map((user: User) => {
          return user.id;
        }),
      );
  }

  private getAdminUsers$(): void {
    combineLatest([
      this.userApiService.getUsers$(),
      this.userApiService.getAdmins$()]
    )
      .pipe(
        take(1),
        map(([users, adminStrings]: [User[], string[]]) => {
          return this.getAdminUsers(users, adminStrings);
        }),
      )
      .subscribe((users: User[]) => {
        this.adminUsers$.next(users);
      });
  }

  private getAdminUsers(users: User[], adminStrings: string[]): any[] {
    return users.Where(user => {
      return adminStrings.Contains(user.id);
    });
  }

  defineNewAdmin(user: User): void {
    this.newAdminForm.setIsDisabled(true);
    this.userMapperService.addAdmin$(user)
      .pipe(take(1))
      .subscribe((_: User) => {
          this.onNewAdminDefined(user);
        }
      );
  }

  onNewAdminDefined(newAdmin: User): void {
    this.newAdminForm.setIsDisabled(false);
    this.newAdminForm.setFormText('');
    this.adminUsers$.pipe(
      take(1),
    )
      .subscribe(users => {
        users.push(newAdmin);
        this.adminUsers$.next(users);
        this.excludedIdsShouldUpdate = [];
      });
  }

  onDeleteAdminButtonClicked(adminToDelete: User): void {
    const title: string = this.i18n({
      id: 'deleteAdminDialog_title',
      value: 'Admin löschen'
    });
    const message: string =
      this.i18n({
        id: 'deleteAdminDialog_message',
        value: '{{surname}}, {{name}} von den Admins löschen?'
      }, {surname: adminToDelete.surname, name: adminToDelete.givenName});
    const confirmButtonText: string = this.i18n({
      id: 'deleteButtonText',
      description: 'deleteButtonText',
      value: 'Löschen'
    });
    const data: ConfirmationDialogData = {
      title,
      message,
      confirmButtonText
    };
    const dialogReference: MatDialogRef<ConfirmationDialogComponent, object> = this.matDialog.open(ConfirmationDialogComponent, {
      width: '600px',
      data
    });
    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(take(1))
        .subscribe(isConfirmed => {
          if (isConfirmed) {
            this.queryAdminDeletion(adminToDelete);
          }
        })
    );
  }

  queryAdminDeletion(adminToDelete: User): void {
      this.userMapperService
        .deleteAdmin$(adminToDelete.id)
        .pipe(take(1))
        .subscribe(() => {
          this.onAdminDeleted(adminToDelete);
        });
  }

  onAdminDeleted(deletedAdmin: User): void {
    this.adminUsers$.pipe(
      take(1),
    )
      .subscribe(users => {
        const usersWithoutDeleted: User[] = users.Where(user => user.id !== deletedAdmin.id);
        this.adminUsers$.next(usersWithoutDeleted);
        this.excludedIdsShouldUpdate = [];
      });
  }

  navigateToCompanies(): void {
    this.router.navigate(['/companies']);
  }
}
