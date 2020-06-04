import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, Observable, of, Subscription } from 'rxjs';
import { filter, map, switchMap, take } from 'rxjs/operators';
import { CycleUnit } from '../../../shared/model/ui/cycle-unit';
import { DepartmentUnit } from '../../../shared/model/ui/OrganizationalUnit/department-unit';
import { SubStructureContextRoleService } from '../../../shared/services/helper/sub-structure-context-role.service';
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
import { ContextRole } from '../../../shared/model/ui/context-role';
import { SubStructure } from '../../../shared/model/ui/OrganizationalUnit/sub-structure';
import { StructureMapper } from '../../../shared/services/mapper/structure.mapper';

interface DepartmentView {
  cycle: CycleUnit;
  currentUserRole: ContextRole;
  subStructure: SubStructure;
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
  subStructure$: Observable<SubStructure>;

  subscriptions: Subscription[] = [];

  constructor(
    private departmentMapperService: DepartmentMapper,
    private structureMapperService: StructureMapper,
    private departmentContextRoleService: SubStructureContextRoleService,
    private matDialog: MatDialog,
    private router: Router,
    private route: ActivatedRoute,
    private currentOkrViewService: CurrentOkrviewService,
    private currentCycleService: CurrentCycleService,
    private excelService: ExcelMapper,
    private i18n: I18n
  ) {}

  ngOnInit(): void {
    this.subStructure$ = this.route.paramMap
      .pipe(
        switchMap(params => {
          const structureId: number = +params.get('departmentId');
          this.currentOkrViewService.browseDepartment(structureId);

          return this.structureMapperService.getSubStructureById$(structureId);
        })
      );

    this.currentUserRole$ = this.subStructure$
      .pipe(
        switchMap(subStructure => {
          return this.departmentContextRoleService.getContextRoleForSubStructure$(subStructure);
        })
      );

    this.cycle$ = this.currentCycleService.getCurrentCycle$();

    this.departmentView$ = combineLatest([
      this.subStructure$,
      this.cycle$,
      this.currentUserRole$
    ])
      .pipe(
        map((([subStructure, cycle, userRole]: [SubStructure, CycleUnit, ContextRole]) => {
          const info: DepartmentView = {
            currentUserRole: userRole,
            cycle,
            subStructure
          };

          return info;
        })),
      );
  }

  // TODO: (R.J. 04.06.20) clickedEditDepartment for SubStructures
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
        .subscribe(editedDepartment => this.onSubStructureEdited(editedDepartment as DepartmentUnit))
    );
  }

  toggleSubStructureActive(subStructure: SubStructure): void {
    subStructure.isActive = !subStructure.isActive;

    this.subscriptions.push(
      this.structureMapperService
        .putSubStructure$(subStructure)
        .pipe(take(1))
        .subscribe(returnedSubStructure => this.onSubStructureEdited(returnedSubStructure))
    );
  }

  onSubStructureEdited(subStructure: SubStructure): void {
    this.currentOkrViewService.refreshCurrentDepartmentView(subStructure.id);
    // this.updateView(); TODO: (R.J 04.06.20) Update View
  }

  clickedRemoveSubStructure(subStructure: SubStructure): void {
    const title: string = this.i18n({
      id: 'deleteDepartmentDialogTitle',
      value: '{{name}} löschen?'
    }, {name: subStructure.name});
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
            this.queryRemoveSubStructure(subStructure);
          }
        })
    );
  }

  // TODO: (R.J. 02.06.20) THIS SHOULD NOT ALWAYS RETURN TRUE. It should return false, when it is a corporateObjectiveStructure with subStructures.
  canDepartmentBeRemoved(): boolean {
    return true; // this.subStructure.subStructureIds.length === 0;
  }

  queryRemoveSubStructure(subStructure: SubStructure): void {
    this.subscriptions.push(
      this.structureMapperService
        .deleteSubStructure$(subStructure)
        .pipe(take(1))
        .subscribe(() => {
          this.onSubStructureDeleted(subStructure);
        })
    );
  }

  onSubStructureDeleted(subStructure: SubStructure): void {
    if (subStructure.isParentStructureACorporateObjectiveStructure) {
      this.currentOkrViewService.refreshCurrentDepartmentView(subStructure.parentStructureId);
    } else {
      // Therefore the parent structure is a company
      this.currentOkrViewService.refreshCurrentCompanyView(subStructure.parentStructureId);
    }
    this.moveToParentStructure(subStructure);
  }

  downloadDepartmentExcelFile(department: DepartmentUnit): void {
    this.excelService.downloadExcelFileForOkrTeam(department.id);
  }

  downloadDepartmentExcelEmailFile(department: DepartmentUnit): void {
    this.excelService.downloadExcelEmailFileForOkrTeam(department.id);
  }

  // Template helper functions
  moveToParentStructure(subStructure: SubStructure): void {
    if (subStructure.isParentStructureACorporateObjectiveStructure) {
      this.router.navigate([`../${subStructure.parentStructureId}`], { relativeTo: this.route })
        .catch();
    } else {
      this.router.navigate([`../../companies/${subStructure.parentStructureId}`], { relativeTo: this.route })
        .catch();
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }
}
