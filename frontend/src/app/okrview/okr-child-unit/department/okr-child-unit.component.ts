import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, Observable, of, Subscription } from 'rxjs';
import { catchError, filter, map, shareReplay, switchMap, take, tap } from 'rxjs/operators';
import { CycleUnit } from '../../../shared/model/ui/cycle-unit';
import { OkrDepartment } from '../../../shared/model/ui/OrganizationalUnit/okr-department';
import { OkrChildUnitRoleService } from '../../../shared/services/helper/okr-child-unit-role.service';
import { OkrChildUnitFormComponent } from '../okr-child-unit-form/okr-child-unit-form.component';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData
} from '../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { CurrentOkrviewService } from '../../current-okrview.service';
import { ExcelMapper } from '../../excel-file/excel.mapper';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { CurrentCycleService } from '../../current-cycle.service';
import { ContextRole } from '../../../shared/model/ui/context-role';
import { OkrChildUnit } from '../../../shared/model/ui/OrganizationalUnit/okr-child-unit';
import { OkrUnitService } from '../../../shared/services/mapper/okr-unit.service';
import { OkrBranch } from '../../../shared/model/ui/OrganizationalUnit/okr-branch';
import { HttpErrorResponse } from '@angular/common/http';
import { CurrentOkrUnitSchemaService } from '../../current-okr-unit-schema.service';

interface DepartmentView {
  cycle: CycleUnit;
  currentUserRole: ContextRole;
  okrChildUnit: OkrChildUnit;
}

interface ActiveTabs {
  teamsTab: boolean;
  childUnitTab: boolean;
  descriptionTab: boolean;
}

@Component({
  selector: 'app-okr-child-unit',
  templateUrl: './okr-child-unit.component.html',
  styleUrls: ['./okr-child-unit.component.scss']
})
export class OkrChildUnitComponent implements OnInit, OnDestroy {
  currentUserRole$: Observable<ContextRole>;
  cycle$: Observable<CycleUnit>;
  departmentView$: Observable<DepartmentView>;
  okrChildUnit$: Observable<OkrChildUnit>;
  activeTabs$: Observable<ActiveTabs>;

  error404: boolean = false;

  subscriptions: Subscription[] = [];

  constructor(
    private okrUnitService: OkrUnitService,
    private departmentContextRoleService: OkrChildUnitRoleService,
    private matDialog: MatDialog,
    private router: Router,
    private route: ActivatedRoute,
    private currentOkrViewService: CurrentOkrviewService,
    private currentCycleService: CurrentCycleService,
    private currentUnitSchemaService: CurrentOkrUnitSchemaService,
    private excelService: ExcelMapper,
    private i18n: I18n
  ) {
  }

  ngOnInit(): void {
    this.okrChildUnit$ = this.route.paramMap
      .pipe(
        switchMap(params => {
          const unitId: number = +params.get('departmentId');

          return this.okrUnitService.getOkrChildUnitById$(unitId, false)
            .pipe(
              catchError((error: HttpErrorResponse) => {
                this.set404State();

                return of(null);
              }),
            );
        }),
        tap((unit: OkrChildUnit) => {
          if (unit) {
            this.currentOkrViewService.browseDepartment(unit.id);
          }
        }),
        shareReplay(1)
      );

    this.currentUserRole$ = this.okrChildUnit$
      .pipe(
        switchMap(childUnit => {
          return this.departmentContextRoleService.getContextRoleForOkrChildUnit$(childUnit);
        })
      );

    this.cycle$ = this.currentCycleService.getCurrentCycle$();

    this.departmentView$ = combineLatest([
      this.okrChildUnit$,
      this.cycle$,
      this.currentUserRole$
    ])
      .pipe(
        map((([childUnit, cycle, userRole]: [OkrChildUnit, CycleUnit, ContextRole]) => {
          const info: DepartmentView = {
            currentUserRole: userRole,
            cycle,
            okrChildUnit: childUnit
          };

          return info;
        })),
      );

    this.activeTabs$ = this.okrChildUnit$
      .pipe(
        map((childUnit: OkrChildUnit) => {
          return {
            childUnitTab: childUnit instanceof OkrBranch,
            teamsTab: childUnit instanceof OkrDepartment,
            descriptionTab: childUnit instanceof OkrDepartment
          };
        })
      );
  }

  // Template actions
  // TODO: (R.J. 06.10.2020) 404 Error handling for dialogs is also needed...
  clickedEditChildUnit(childUnit: OkrChildUnit): void {
    const dialogReference: MatDialogRef<OkrChildUnitFormComponent, object> = this.matDialog.open(OkrChildUnitFormComponent, {
      data: { childUnit }
    });

    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(
          take(1),
          filter(v => v as any),
          switchMap(n => n as any),
        )
        .subscribe(editedChildUnit => this.onChildUnitEdited(editedChildUnit as OkrChildUnit))
    );
  }

  toggleChildActive(okrChildUnit: OkrChildUnit): void {
    okrChildUnit.isActive = !okrChildUnit.isActive;

    this.subscriptions.push(
      this.okrUnitService
        .putOkrChildUnit$(okrChildUnit, false)
        .pipe(take(1))
        .subscribe(
          returnedChildUnit => this.onChildUnitEdited(returnedChildUnit),
          () => this.set404State())
    );
  }

  onChildUnitEdited(okrChildUnit: OkrChildUnit): void {
    this.currentOkrViewService.browseDepartment(okrChildUnit.id);
  }

  clickedRemoveChildUnit(okrChildUnit: OkrChildUnit): void {
    const title: string = this.i18n({
      id: 'deleteDepartmentDialogTitle',
      value: '{{name}} löschen?'
    }, { name: okrChildUnit.name });
    const message: string = this.i18n({
      id: 'deleteDepartmentDialogMessage',
      value: 'Es werden auch alle untergeordneten Objectives, KeyResults und Kommentare gelöscht.'
    });
    const confirmButtonText: string = this.i18n({ id: 'capitalised_delete', value: 'Löschen' });

    const data: ConfirmationDialogData = {
      message,
      title,
      confirmButtonText
    };

    const dialogReference: MatDialogRef<ConfirmationDialogComponent, object> =
      this.matDialog.open(ConfirmationDialogComponent, { width: '600px', data });
    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(take(1))
        .subscribe(isConfirmed => {
          if (isConfirmed) {
            this.queryRemoveChildUnit(okrChildUnit);
          }
        })
    );
  }

  canChildUnitBeRemoved(okrChildUnit: OkrChildUnit): boolean {
    return this.isDepartmentUnit(okrChildUnit) ||
      (okrChildUnit instanceof OkrBranch && okrChildUnit.okrChildUnitIds.length === 0);
  }

  isDepartmentUnit(okrChildUnit: OkrChildUnit): boolean {
    return okrChildUnit instanceof OkrDepartment;
  }

  queryRemoveChildUnit(okrChildUnit: OkrChildUnit): void {
    this.subscriptions.push(
      this.okrUnitService
        .deleteOkrChildUnit$(okrChildUnit, false)
        .pipe(take(1))
        .subscribe(() => {
          this.onChildUnitDeleted(okrChildUnit);
        },
          () => this.onChildUnitDeleted(okrChildUnit))
    );
  }

  onChildUnitDeleted(okrChildUnit: OkrChildUnit): void {
    if (okrChildUnit.isParentUnitABranch) {

      this.currentOkrViewService.refreshCurrentDepartmentView(okrChildUnit.parentUnitId);
    } else {
      // Therefore the parent schema is a company
      this.currentOkrViewService.refreshCurrentCompanyView(okrChildUnit.parentUnitId);
    }
    this.moveToParentUnit(okrChildUnit);
  }

  downloadOkrChildUnitExcelFile(okrChildUnit: OkrChildUnit): void {
    this.excelService.downloadExcelFileForOkrChildUnit(okrChildUnit.id);
  }

  downloadDepartmentExcelEmailFile(department: OkrDepartment): void {
    this.excelService.downloadExcelEmailFileForOkrTeam(department.id);
  }

  // Template helper functions
  moveToParentUnit(okrChildUnit: OkrChildUnit): void {
    if (okrChildUnit.isParentUnitABranch) {
      this.router.navigate([`../${okrChildUnit.parentUnitId}`], { relativeTo: this.route })
        .catch();
    } else {
      this.router.navigate([`../../companies/${okrChildUnit.parentUnitId}`], { relativeTo: this.route })
        .catch();
    }
  }

  moveToParentUnit404(): void {
    const unitId: number = +this.route.snapshot.paramMap.get('departmentId');
    this.currentUnitSchemaService.getParentUnitId$(unitId)
      .pipe(take(1))
      .subscribe((id: number) => {
        if (!!id) {
          this.error404 = false;
          this.router.navigate(['..', id], { relativeTo: this.route });
        } else {
          this.router.navigate(['/companies']);
        }
      });
  }

  private set404State(): void {
    this.error404 = true;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }
}
