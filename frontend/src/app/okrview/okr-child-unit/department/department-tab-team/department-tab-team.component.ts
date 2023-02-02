import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { CurrentUserService } from '../../../../core/services/current-user.service';
import { ConfigurationManagerService } from '../../../../core/settings/configuration-manager.service';

import {
  ConfirmationDialogComponent,
  ConfirmationDialogData,
} from '../../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { User } from '../../../../shared/model/api/user';
import { Configuration } from '../../../../shared/model/ui/configuration';
import { ContextRole } from '../../../../shared/model/ui/context-role';
import { CycleUnit } from '../../../../shared/model/ui/cycle-unit';
import { OkrUnitRole } from '../../../../shared/model/ui/okr-unit-schema';
import { OkrDepartment } from '../../../../shared/model/ui/OrganizationalUnit/okr-department';
import { DepartmentMapper } from '../../../../shared/services/mapper/department.mapper';
import { CurrentOkrUnitSchemaService } from '../../../current-okr-unit-schema.service';

@Component({
  selector: 'app-department-tab-team',
  templateUrl: './department-tab-team.component.html',
  styleUrls: ['./department-tab-team.component.scss'],
})
export class DepartmentTabTeamComponent implements OnInit, OnChanges {
  @Input() department: OkrDepartment;
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;

  canEditManagers = false;
  canEditMembers = false;
  topicSponsorsActivated$: Observable<boolean>;

  constructor(
    private departmentMapperService: DepartmentMapper,
    private configurationManagerService: ConfigurationManagerService,
    private currentUserService: CurrentUserService,
    private currentOkrUnitSchemaService: CurrentOkrUnitSchemaService,
    private matDialog: MatDialog,
    private translate: TranslateService,
  ) {
  }

  ngOnInit(): void {
    this.updateRights();

    this.topicSponsorsActivated$ = this.configurationManagerService.getTopicSponsorsActivated$()
      .pipe(
        map((configuration: Configuration) => {
          return configuration.value === 'true' || configuration.value === true;
        }),
      );
  }

  ngOnChanges(): void {
    this.updateRights();
  }

  clickedDeleteOKRMaster(): void {
    const title: string = this.translate.instant('department-tab-team.deletion-dialog.title-master');
    const message: string = this.translate.instant('department-tab-team.deletion-dialog.message');
    const confirmButtonText: string = this.translate.instant('department-tab-team.deletion-dialog.button-text');
    const data: ConfirmationDialogData = {
      confirmButtonText,
      title,
      message,
    };
    const dialogReference: MatDialogRef<ConfirmationDialogComponent, string> = this.matDialog.open(
      ConfirmationDialogComponent,
      {
        width: '600px',
        data,
      },
    );

    dialogReference
      .afterClosed()
      .pipe(take(1))
      .subscribe(isConfirmed => {
        if (isConfirmed) {
          this.deleteOkrMaster();
        }
      });
  }

  clickedDefineOKRMaster(user: User): void {
    this.department.okrMasterId = user.id;
    this.updateUserList();
  }

  clickedDeleteOKRTopicSponsor(): void {
    const title: string = this.translate.instant('department-tab-team.deletion-dialog.title-sponsor');
    const message: string = this.translate.instant('department-tab-team.deletion-dialog.message');
    const confirmButtonText: string = this.translate.instant('department-tab-team.deletion-dialog.button-text');
    const data: ConfirmationDialogData = {
      confirmButtonText,
      title,
      message,
    };
    const dialogReference: MatDialogRef<ConfirmationDialogComponent, string> = this.matDialog.open(
      ConfirmationDialogComponent,
      {
        width: '600px',
        data,
      },
    );

    dialogReference
      .afterClosed()
      .pipe(take(1))
      .subscribe(isConfirmed => {
        if (isConfirmed) {
          this.deleteOkrTopicSponsor();
        }
      });
  }

  clickedDefineOKRTopicSponsor(user: User): void {
    this.department.okrTopicSponsorId = user.id;
    this.updateUserList();
  }

  clickedDeleteOKRMember(memberId: string): void {
    const title: string = this.translate.instant('department-tab-team.deletion-dialog.title-member');
    const message: string = this.translate.instant('department-tab-team.deletion-dialog.message-member');
    const confirmButtonText: string = this.translate.instant('department-tab-team.deletion-dialog.button-text');
    const data: ConfirmationDialogData = {
      confirmButtonText,
      title,
      message,
    };
    const dialogReference: MatDialogRef<ConfirmationDialogComponent, string> = this.matDialog.open(
      ConfirmationDialogComponent,
      {
        width: '600px',
        data,
      },
    );

    dialogReference
      .afterClosed()
      .pipe(take(1))
      .subscribe(isConfirmed => {
        if (isConfirmed) {
          this.deleteOkrMember(memberId);
        }
      });
  }

  clickedDefineOKRMember(user: User): void {
    if (this.department.okrMemberIds.includes(user.id)) {
      return;
    }
    this.department.okrMemberIds.push(user.id);
    this.updateUserList();
  }

  private deleteOkrMaster(): void {
    this.department.okrMasterId = null;
    this.updateUserList();
  }

  private deleteOkrTopicSponsor(): void {
    this.department.okrTopicSponsorId = null;
    this.updateUserList();
  }

  private deleteOkrMember(memberId: string): void {
    const memberIndex: number = this.department.okrMemberIds.indexOf(memberId);
    this.department.okrMemberIds.splice(memberIndex, 1);
    this.updateUserList();
  }

  private updateRights(): void {
    this.canEditManagers = (this.cycle.isCycleActive() || this.cycle.isCycleInPreparation()) && this.currentUserRole.isAtleastAdmin();
    this.canEditMembers = (this.cycle.isCycleActive() || this.cycle.isCycleInPreparation()) && this.currentUserRole.isAtleastOKRManager();
  }

  private updateUserList(): void {
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

  private querySaveTeam(): void {
    this.departmentMapperService
      .putDepartment$(this.department)
      .pipe(take(1))
      .subscribe();
  }

  private getCurrentUserIdPromiseFromUserService$(): Observable<string> {
    return this.currentUserService.getCurrentUser$()
      .pipe(
        map(currentUser => currentUser.id),
        take(1),
      );
  }
}
