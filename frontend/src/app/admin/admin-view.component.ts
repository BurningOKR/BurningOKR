import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import 'linq4js';
import { combineLatest, Observable, ReplaySubject, Subject } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { CurrentUserService } from '../core/services/current-user.service';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData,
} from '../shared/components/confirmation-dialog/confirmation-dialog.component';
import { UserAutocompleteInputComponent } from '../shared/components/user-autocomplete-input/user-autocomplete-input.component';
import { User } from '../shared/model/api/user';
import { UserId } from '../shared/model/id-types';
import { UserService } from '../shared/services/helper/user.service';

@Component({
  selector: 'app-admin-view',
  templateUrl: './admin-view.component.html',
  styleUrls: ['./admin-view.component.scss'],
})
export class AdminViewComponent implements OnInit {
  @ViewChild('newAdminForm') newAdminForm: UserAutocompleteInputComponent;

  adminUsers$: Subject<User[]> = new ReplaySubject<User[]>(1);

  isPlayground: boolean = environment.playground;

  currentUserId$: Observable<UserId>;
  excludedIdsShouldUpdate: any;

  constructor(
    private userService: UserService,
    private currentUserService: CurrentUserService,
    private matDialog: MatDialog,
    private router: Router,
    private translate: TranslateService,
  ) {
  }

  ngOnInit(): void {
    this.currentUserId$ = this.getCurrentUserId$();

    this.getAdminUsers$();
  }

  defineNewAdmin(user: User): void {
    this.newAdminForm.setIsDisabled(true);
    this.userService.addAdmin$(user)
      .pipe(take(1))
      .subscribe((_: User) => {
          this.onNewAdminDefined(user);
        },
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
    const title: string = this.translate.instant('admin-view.delete-dialog.title');
    const message: string = this.translate.instant('admin-view.delete-dialog.message',
      { surname: adminToDelete.surname, name: adminToDelete.givenName });
    const confirmButtonText: string = this.translate.instant('admin-view.delete-dialog.button-text');
    const data: ConfirmationDialogData = {
      title,
      message,
      confirmButtonText,
    };
    const dialogReference: MatDialogRef<ConfirmationDialogComponent, object> = this.matDialog.open(ConfirmationDialogComponent, {
      width: '600px',
      data,
    });
    dialogReference
      .afterClosed()
      .pipe(take(1))
      .subscribe(isConfirmed => {
        if (isConfirmed) {
          this.queryAdminDeletion(adminToDelete);
        }
      });
  }

  queryAdminDeletion(adminToDelete: User): void {
    this.userService
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

  private getCurrentUserId$(): Observable<UserId> {
    return this.currentUserService.getCurrentUser$()
      .pipe(
        map((user: User) => {
          return user.id;
        }),
      );
  }

  private getAdminUsers$(): void {
    combineLatest([
      this.userService.getUsers$(),
      this.userService.getAdminIds$()],
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
}
