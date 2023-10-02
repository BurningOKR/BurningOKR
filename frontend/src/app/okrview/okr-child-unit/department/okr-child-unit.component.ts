import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { combineLatest, Observable, of } from 'rxjs';
import { catchError, filter, map, shareReplay, switchMap, take, tap } from 'rxjs/operators';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData,
} from '../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { ContextRole } from '../../../shared/model/ui/context-role';
import { CycleUnit } from '../../../shared/model/ui/cycle-unit';
import { OkrBranch } from '../../../shared/model/ui/OrganizationalUnit/okr-branch';
import { OkrChildUnit } from '../../../shared/model/ui/OrganizationalUnit/okr-child-unit';
import { OkrDepartment } from '../../../shared/model/ui/OrganizationalUnit/okr-department';
import { OkrChildUnitRoleService } from '../../../shared/services/helper/okr-child-unit-role.service';
import { OkrUnitService } from '../../../shared/services/mapper/okr-unit.service';
import { CurrentCycleService } from '../../current-cycle.service';
import { CurrentOkrUnitSchemaService } from '../../current-okr-unit-schema.service';
import { CurrentOkrviewService } from '../../current-okrview.service';
import { ExcelMapper } from '../../excel-file/excel.mapper';
import { OkrChildUnitFormComponent } from '../okr-child-unit-form/okr-child-unit-form.component';
import { UnitType } from '../../../shared/model/api/OkrUnit/unit-type.enum';

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
  styleUrls: ['./okr-child-unit.component.scss'],
})
export class OkrChildUnitComponent implements OnInit {
  currentUserRole$: Observable<ContextRole>;
  cycle$: Observable<CycleUnit>;
  departmentView$: Observable<DepartmentView>;
  okrChildUnit$: Observable<OkrChildUnit>;
  activeTabs$: Observable<ActiveTabs>;
  unitType = UnitType;

  error404: boolean = false;

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
    private translate: TranslateService,
  ) {
  }

  ngOnInit(): void {
    this.okrChildUnit$ = this.route.paramMap
      .pipe(
        switchMap(params => {
          const unitId: number = +params.get('departmentId');

          return this.okrUnitService.getOkrChildUnitById$(unitId, false)
            .pipe(
              catchError(() => {
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
        shareReplay(1),
      );

    this.currentUserRole$ = this.okrChildUnit$
      .pipe(
        switchMap(childUnit => {
          return this.departmentContextRoleService.getContextRoleForOkrChildUnit$(childUnit);
        }),
      );

    this.cycle$ = this.currentCycleService.getCurrentCycle$();

    this.departmentView$ = combineLatest([
      this.okrChildUnit$,
      this.cycle$,
      this.currentUserRole$,
    ])
      .pipe(
        map((([childUnit, cycle, userRole]: [OkrChildUnit, CycleUnit, ContextRole]) => {
          const info: DepartmentView = {
            currentUserRole: userRole,
            cycle,
            okrChildUnit: childUnit,
          };

          return info;
        })),
      );

    this.activeTabs$ = this.okrChildUnit$
      .pipe(
        map((childUnit: OkrChildUnit) => {
          return {
            childUnitTab: childUnit.type === UnitType.BRANCH,
            teamsTab: childUnit.type === UnitType.DEPARTMENT,
            descriptionTab: childUnit.type === UnitType.DEPARTMENT,
          };
        }),
      );
  }

  // Template actions

  clickedEditChildUnit(childUnit: OkrChildUnit): void {
    const dialogReference: MatDialogRef<OkrChildUnitFormComponent, object> = this.matDialog.open(
      OkrChildUnitFormComponent,
      {
        data: { childUnit },
      },
    );
    dialogReference
      .afterClosed()
      .pipe(
        take(1),
        filter(v => v as any),
        switchMap(n => n as any),
      )
      .subscribe(editedChildUnit => this.onChildUnitEdited(editedChildUnit as OkrChildUnit));
  }

  toggleChildActive(okrChildUnit: OkrChildUnit): void {
    okrChildUnit.isActive = !okrChildUnit.isActive;

    this.okrUnitService
      .putOkrChildUnit$(okrChildUnit, false)
      .pipe(take(1))
      .subscribe(
        returnedChildUnit => this.onChildUnitEdited(returnedChildUnit),
        () => this.set404State(),
      );
  }

  onChildUnitEdited(okrChildUnit: OkrChildUnit): void {
    this.currentOkrViewService.browseDepartment(okrChildUnit.id);
  }

  clickedRemoveChildUnit(okrChildUnit: OkrChildUnit): void {
    const title: string = this.translate.instant('okr-child-unit.deletion-dialog.title', { name: okrChildUnit.name });
    const message: string = this.translate.instant('okr-child-unit.deletion-dialog.message');
    const confirmButtonText: string = this.translate.instant('okr-child-unit.deletion-dialog.button-text');

    const data: ConfirmationDialogData = {
      message,
      title,
      confirmButtonText,
    };

    const dialogReference: MatDialogRef<ConfirmationDialogComponent, object> =
      this.matDialog.open(ConfirmationDialogComponent, { width: '600px', data });

    dialogReference
      .afterClosed()
      .pipe(take(1))
      .subscribe(isConfirmed => {
        if (isConfirmed) {
          this.queryRemoveChildUnit(okrChildUnit);
        }
      });
  }

  canChildUnitBeRemoved(okrChildUnit: OkrChildUnit): boolean {
    return this.isDepartmentUnit(okrChildUnit) ||
      (this.isOkrBranch(okrChildUnit) && (okrChildUnit as OkrBranch).okrChildUnitIds?.length === 0);
  }

  isDepartmentUnit(okrChildUnit: OkrChildUnit): boolean {
    return okrChildUnit.type === UnitType.DEPARTMENT;
  }

  isOkrBranch(okrChildUnit: OkrChildUnit): boolean {
    return okrChildUnit.type === UnitType.BRANCH;
  }

  queryRemoveChildUnit(okrChildUnit: OkrChildUnit): void {
    this.okrUnitService
      .deleteOkrChildUnit$(okrChildUnit, false)
      .pipe(take(1))
      .subscribe(
        () => {
          this.onChildUnitDeleted(okrChildUnit);
        },
        () => this.onChildUnitDeleted(okrChildUnit),
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
}
