import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, Observable, Subscription } from 'rxjs';
import { filter, map, switchMap, take, tap } from 'rxjs/operators';
import { CycleUnit } from '../../../shared/model/ui/cycle-unit';
import { DepartmentUnit } from '../../../shared/model/ui/OrganizationalUnit/department-unit';
import {
  ContextRole,
  DepartmentContextRoleService
} from '../../../shared/services/helper/department-context-role.service';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { SubstructureFormComponent } from '../substructure-form/substructure-form.component';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData
} from '../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { CurrentOkrviewService } from '../../current-okrview.service';
import { ExcelMapper } from '../../excel-file/excel.mapper';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { CurrentCycleService } from '../../current-cycle.service';

interface DepartmentView {
  cycle: CycleUnit;
  currentUserRole: ContextRole;
  department: DepartmentUnit;
}

@Component({
  selector: 'app-department',
  templateUrl: './sub-structure.component.html',
  styleUrls: ['./sub-structure.component.scss']
})
export class SubStructureComponent implements OnInit, OnDestroy {
  currentUserRole$: Observable<ContextRole>;
  cycle$: Observable<CycleUnit>;
  departmentView$: Observable<DepartmentView>;
  department$: Observable<DepartmentUnit>;

  subscriptions: Subscription[] = [];

  constructor(
    private departmentMapperService: DepartmentMapper,
    private departmentContextRoleService: DepartmentContextRoleService,
    private matDialog: MatDialog,
    private router: Router,
    private route: ActivatedRoute,
    private currentOkrViewService: CurrentOkrviewService,
    private currentCycleService: CurrentCycleService,
    private excelService: ExcelMapper,
    private i18n: I18n
  ) {}

  ngOnInit(): void {
    this.department$ = this.route.paramMap
      .pipe(
        switchMap(params => {
          const departmentId: number = +params.get('departmentId');
          this.currentOkrViewService.browseDepartment(departmentId);

          return this.departmentMapperService.getDepartmentById$(departmentId);
        })
      );

    this.currentUserRole$ = this.department$
      .pipe(
        switchMap(department => {
          return this.departmentContextRoleService.getContextRoleForDepartment$(department);
        })
      );

    this.cycle$ = this.currentCycleService.getCurrentCycle$();

    this.departmentView$ = combineLatest([
      this.department$,
      this.cycle$,
      this.currentUserRole$
    ])
      .pipe(
        map((([department, cycle, userRole]: [DepartmentUnit, CycleUnit, ContextRole]) => {
          const info: DepartmentView = {
            currentUserRole: userRole,
            cycle,
            department
          };

          return info;
        })),
      );
  }

  // Template actions
  clickedEditDepartment(department: DepartmentUnit): void {
    const dialogReference: MatDialogRef<SubstructureFormComponent, object> = this.matDialog.open(SubstructureFormComponent, {
      data: { department }
    });

    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(
          take(1),
          filter(v => v as any),
          switchMap(n => n as any),
        )
        .subscribe(editedDepartment => this.onDepartmentEdited(editedDepartment as DepartmentUnit))
    );
  }

  toggleDepartmentActive(department: DepartmentUnit): void {
    department.isActive = !department.isActive;

    this.subscriptions.push(
      this.departmentMapperService
        .putDepartment$(department)
        .pipe(take(1))
        .subscribe(returnedDepartment => this.onDepartmentEdited(returnedDepartment))
    );
  }

  onDepartmentEdited(department: DepartmentUnit): void {
    this.currentOkrViewService.refreshCurrentDepartmentView(department.id);
    // this.updateView(); TODO: (R.J 04.06.20) Update View
  }

  clickedRemoveDepartment(department: DepartmentUnit): void {
    const title: string = this.i18n({
      id: 'deleteDepartmentDialogTitle',
      value: 'Department {{name}} löschen?'
    }, {name: department.name});
    const message: string = this.i18n({
      id: 'deleteDepartmentDialogMessage',
      value: 'Es werden auch alle untergeordneten Objectives, KeyResults und Kommentare gelöscht.'
    });
    const confirmButtonText: string = this.i18n({id: 'delete', value: 'Löschen'});

    const data: ConfirmationDialogData = {
      message,
      title,
      confirmButtonText
    };

    const dialogReference: MatDialogRef<ConfirmationDialogComponent, object> =
      this.matDialog.open(ConfirmationDialogComponent, {width: '600px', data});
    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(take(1))
        .subscribe(isConfirmed => {
          if (isConfirmed) {
            this.queryRemoveDepartment(department);
          }
        })
    );
  }

  // TODO: (R.J. 02.06.20) THIS SHOULD NOT ALWAYS RETURN TRUE. It should return false, when it is a corporateObjectiveStructure with subStructures.
  canDepartmentBeRemoved(): boolean {
    return true; // this.department.subStructureIds.length === 0;
  }

  queryRemoveDepartment(department: DepartmentUnit): void {
    this.subscriptions.push(
      this.departmentMapperService
        .deleteDepartment$(department.id)
        .pipe(take(1))
        .subscribe(() => {
          this.onDepartmentDeleted(department);
        })
    );
  }

  onDepartmentDeleted(department: DepartmentUnit): void {
    if (department.isParentStructureACorporateObjectiveStructure) {
      this.currentOkrViewService.refreshCurrentDepartmentView(department.parentStructureId);
    } else {
      // Therefore the parent structure is a company
      this.currentOkrViewService.refreshCurrentCompanyView(department.parentStructureId);
    }
    this.moveToParentStructure(department);
  }

  downloadDepartmentExcelFile(department: DepartmentUnit): void {
    this.excelService.downloadExcelFileForOkrTeam(department.id);
  }

  downloadDepartmentExcelEmailFile(department: DepartmentUnit): void {
    this.excelService.downloadExcelEmailFileForOkrTeam(department.id);
  }

  // Template helper functions
  moveToParentStructure(department: DepartmentUnit): void {
    if (department.isParentStructureACorporateObjectiveStructure) {
      this.router.navigate([`../${department.parentStructureId}`], { relativeTo: this.route })
        .catch();
    } else {
      this.router.navigate([`../../companies/${department.parentStructureId}`], { relativeTo: this.route })
        .catch();
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }
}
