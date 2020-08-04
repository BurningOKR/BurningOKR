import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import { Observable, Subscription } from 'rxjs';
import { map, take } from 'rxjs/operators';

import {
  ConfirmationDialogComponent,
  ConfirmationDialogData
} from '../../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { OkrDepartment } from '../../../../shared/model/ui/OrganizationalUnit/okr-department';
import { CycleUnit } from '../../../../shared/model/ui/cycle-unit';
import { DepartmentMapper } from '../../../../shared/services/mapper/department.mapper';
import { CurrentUserService } from '../../../../core/services/current-user.service';
import { OkrUnitRole } from '../../../../shared/model/ui/okr-unit-schema';
import { User } from '../../../../shared/model/api/user';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { CurrentOkrUnitSchemaService } from '../../../current-okr-unit-schema.service';
import { ContextRole } from '../../../../shared/model/ui/context-role';
import { ConfigurationManagerService } from '../../../../core/settings/configuration-manager.service';
import { Configuration } from '../../../../shared/model/ui/configuration';

@Component({
  selector: 'app-department-tab-team',
  templateUrl: './department-tab-team.component.html',
  styleUrls: ['./department-tab-team.component.scss']
})
export class DepartmentTabTeamComponent implements OnInit, OnDestroy {
  @Input() department: OkrDepartment;
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;

  canEditManagers = false;
  canEditMembers = false;
  subscriptions: Subscription[] = [];
  topicSponsorsActivated$: Observable<boolean>;

  constructor(
    private departmentMapperService: DepartmentMapper,
    private configurationManagerService: ConfigurationManagerService,
    private currentUserService: CurrentUserService,
    private currentOkrUnitSchemaService: CurrentOkrUnitSchemaService,
    private matDialog: MatDialog,
    private i18n: I18n
  ) {}

  ngOnInit(): void {
    this.canEditManagers = (this.cycle.isCycleActive() || this.cycle.isCycleInPreparation()) && this.currentUserRole.isAtleastAdmin();
    this.canEditMembers = (this.cycle.isCycleActive() || this.cycle.isCycleInPreparation()) && this.currentUserRole.isAtleastOKRManager();
    this.topicSponsorsActivated$ = this.configurationManagerService.getTopicSponsorsActivated$()
      .pipe(
        map((configuration: Configuration) => {
          return configuration.value === 'true' || configuration.value === true;
        })
      );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(x => x.unsubscribe());
    this.subscriptions = [];
  }

  clickedDeleteOKRMaster(): void {
    const title: string = this.i18n({
      id: 'deleteOkrMasterDialog_title',
      value: 'OKR Master entfernen'
    });
    const message: string = this.i18n({
      id: 'deleteOkrMasterDialog_message',
      value: 'Möchten Sie dies tun? Dieser Benutzer wird seine Managerrechte für dieses Team verlieren.'
    });
    const confirmButtonText: string = this.i18n({
      id: 'deleteOkrMasterDialog_confirmButtonText',
      value: 'Entfernen'
    });
    const data: ConfirmationDialogData = {
      confirmButtonText,
      title,
      message
    };
    const dialogReference: MatDialogRef<ConfirmationDialogComponent, string> = this.matDialog.open(ConfirmationDialogComponent, {
      width: '600px',
      data
    });

    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(take(1))
        .subscribe(isConfirmed => {
          if (isConfirmed) {
            this.deleteOkrMaster();
          }
        })
    );
  }

  deleteOkrMaster(): void {
    this.department.okrMasterId = undefined;
    this.updateUserList();
  }

  clickedDefineOKRMaster(user: User): void {
    this.department.okrMasterId = user.id;
    this.updateUserList();
  }

  clickedDeleteOKRTopicSponsor(): void {
    const title: string = this.i18n({
      id: 'deleteOkrTopicSponsorDialog_title',
      value: 'OKR Themenpate entfernen'
    });
    const message: string = this.i18n({
      id: 'deleteOkrTopicSponsorDialog_message',
      value: 'Möchten Sie dies tun? Dieser Benutzer wird seine Managerrechte für dieses Team verlieren.'
    });
    const confirmButtonText: string = this.i18n({
      id: 'deleteOkrTopicSponsorDialog_title_confirmButtonText',
      value: 'Entfernen'
    });
    const data: ConfirmationDialogData = {
      confirmButtonText,
      title,
      message
    };
    const dialogReference: MatDialogRef<ConfirmationDialogComponent, string> = this.matDialog.open(ConfirmationDialogComponent, {
      width: '600px',
      data
    });

    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(take(1))
        .subscribe(isConfirmed => {
          if (isConfirmed) {
            this.deleteOkrTopicSponsor();
          }
        })
    );
  }

  deleteOkrTopicSponsor(): void {
    this.department.okrTopicSponsorId = undefined;
    this.updateUserList();
  }

  clickedDefineOKRTopicSponsor(user: User): void {
    this.department.okrTopicSponsorId = user.id;
    this.updateUserList();
  }

  clickedDeleteOKRMember(memberId: string): void {
    const title: string = this.i18n({
      id: 'deleteOkrMemberDialog_title',
      value: 'Mitglied entfernen'
    });
    const message: string = this.i18n({
      id: 'deleteOkrMemberDialog_message',
      value: 'Möchten Sie dieses Mitglied von dem Team entfernen? Der Benutzer wird seine Mitgliedsrechte in diesem Team verlieren.'
    });
    const confirmButtonText: string = this.i18n({
      id: 'deleteOkrMemberDialog_confirmButtonText',
      value: 'Entfernen'
    });
    const data: ConfirmationDialogData = {
      confirmButtonText,
      title,
      message
    };
    const dialogReference: MatDialogRef<ConfirmationDialogComponent, string> = this.matDialog.open(ConfirmationDialogComponent, {
      width: '600px',
      data
    });

    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(take(1))
        .subscribe(isConfirmed => {
          if (isConfirmed) {
            this.deleteOkrMember(memberId);
          }
        })
    );
  }

  deleteOkrMember(memberId: string): void {
    const memberIndex: number = this.department.okrMemberIds.indexOf(memberId);
    this.department.okrMemberIds.splice(memberIndex, 1);
    this.updateUserList();
  }

  clickedDefineOKRMember(user: User): void {
    if (this.department.okrMemberIds.includes(user.id)) {
      return;
    }
    this.department.okrMemberIds.push(user.id);
    this.updateUserList();
  }

  updateUserList(): void {
    this.querySaveTeam();
    this.getCurrentUserIdPromiseFromUserService$()
      .subscribe((currentUserId: string) => {
        let newRole: OkrUnitRole;

        if (this.department.okrMasterId === currentUserId || this.department.okrTopicSponsorId === currentUserId) {
          newRole = OkrUnitRole.MANAGER;
        } else if (this.department.okrMemberIds.includes(currentUserId)) {
          newRole = OkrUnitRole.MEMBER;
        } else {
          newRole = OkrUnitRole.USER;
        }

        this.currentOkrUnitSchemaService.updateSchemaTeamRole(this.department.id, newRole);
      });
  }

  querySaveTeam(): void {
    this.departmentMapperService
      .putDepartment$(this.department)
      .pipe(take(1))
      .subscribe();
  }

  getCurrentUserIdPromiseFromUserService$(): Observable<string> {
    return this.currentUserService.getCurrentUser$()
      .pipe(
        map(currentUser => currentUser.id),
        take(1)
      );
  }
}
