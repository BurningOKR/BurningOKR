import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, combineLatest, Observable, Subject, Subscription } from 'rxjs';
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
  templateUrl: './department.component.html',
  styleUrls: ['./department.component.scss']
})
export class DepartmentComponent implements OnInit, OnDestroy {
  department: DepartmentUnit;
  currentUserRole$: Observable<ContextRole>;
  cycle$: Observable<CycleUnit>;
  departmentView$: Observable<DepartmentView>;
  department$: Observable<DepartmentUnit>;
  private componentLoaded$: Subject<boolean> = new BehaviorSubject<boolean>(false);

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
    this.updateView();
  }

  private updateView(): void {
    this.updateDepartment();
    this.updateCurrentCycle();
    this.updateCurrentUserRole();
    this.updateDepartmentViewAndComponentLoaded();
  }

  private updateCurrentUserRole(): void {
    this.currentUserRole$ =  this.department$
      .pipe(
        switchMap(department => {
          return this.departmentContextRoleService.getContextRoleForDepartment$(department);
        })
      );
  }

  private updateCurrentCycle(): void {
    this.cycle$ = this.currentCycleService.getCurrentCycle$();
  }

  private updateDepartment(): void {
    this.department$ = this.route.paramMap
      .pipe(
        switchMap(params => {
          const departmentId: number = +params.get('departmentId');
          this.currentOkrViewService.browseDepartment(departmentId);
          this.componentLoaded$.next(false);

          return this.departmentMapperService.getDepartmentById$(departmentId);
        }),
        tap(department => {
          this.department = department;
        })
      );
  }

  private updateDepartmentViewAndComponentLoaded(): void {
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
        tap(_ => {
          this.componentLoaded$.next(true);
        })
      );
  }

  // Template actions
  clickedEditDepartment(): void {
    const dialogReference: MatDialogRef<SubstructureFormComponent, object> = this.matDialog.open(SubstructureFormComponent, {
      data: { department: this.department }
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

  onDepartmentEdited(department: DepartmentUnit): void {
    this.currentOkrViewService.refreshCurrentDepartmentView(this.department.id);
    this.updateView();
  }

  toggleWhetherDepartmentIsActive(): void {
    this.department.isActive = !this.department.isActive;

    this.subscriptions.push(
      this.departmentMapperService
        .putDepartment$(this.department)
        .pipe(take(1))
        .subscribe(returnedDepartment => this.onDepartmentEdited(returnedDepartment))
    );
  }

  clickedRemoveDepartment(): void {
    const title: string = this.i18n({
      id: 'deleteDepartmentDialogTitle',
      value: 'Department {{name}} löschen?'
    }, {name: this.department.name});
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
            this.queryRemoveDepartment();
          }
        })
    );
  }

  canDepartmentBeRemoved(): boolean {
    return this.department.subDepartmentIds.length === 0;
  }

  queryRemoveDepartment(): void {
    this.subscriptions.push(
      this.departmentMapperService
        .deleteDepartment$(this.department.id)
        .pipe(take(1))
        .subscribe(() => {
          this.onDepartmentDeleted();
        })
    );
  }

  onDepartmentDeleted(): void {
    if (this.department.isParentStructureADepartment) {
      this.currentOkrViewService.refreshCurrentDepartmentView(this.department.parentStructureId);
    } else {
      // Therefore the parent structure is a company
      this.currentOkrViewService.refreshCurrentCompanyView(this.department.parentStructureId);
    }
    this.moveToParentStructure();
  }

  downloadDepartmentExcelFile(): void {
    this.excelService.downloadExcelFileForOkrTeam(this.department.id);
  }

  downloadDepartmentExcelEmailFile(): void {
    this.excelService.downloadExcelEmailFileForOkrTeam(this.department.id);
  }

  // Template helper functions
  moveToParentStructure(): void {
    if (this.department.isParentStructureADepartment) {
      this.router.navigate([`../${this.department.parentStructureId}`], { relativeTo: this.route })
        .catch();
    } else {
      this.router.navigate([`../../companies/${this.department.parentStructureId}`], { relativeTo: this.route })
        .catch();
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }
}
