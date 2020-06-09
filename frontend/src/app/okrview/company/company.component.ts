import {
  ContextRole,
  DepartmentContextRoleService
} from '../../shared/services/helper/department-context-role.service';
import { filter, switchMap, take } from 'rxjs/operators';
import { CurrentOkrviewService } from '../current-okrview.service';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { DepartmentStructure, DepartmentStructureRole } from '../../shared/model/ui/department-structure';
import { MatDialog, MatDialogRef } from '@angular/material';
import { DepartmentUnit } from '../../shared/model/ui/OrganizationalUnit/department-unit';
import { ActivatedRoute } from '@angular/router';
import { ExcelMapper } from '../excel-file/excel.mapper';
import { Observable, ObservableInput, Subscription } from 'rxjs';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { SubstructureFormComponent } from '../substructure/substructure-form/substructure-form.component';
import { CurrentDepartmentStructureService } from '../current-department-structure.service';
import { CurrentCompanyService } from '../current-company.service';
import { CurrentCycleService } from '../current-cycle.service';
import { CompanyId } from '../../shared/model/id-types';

@Component({
  selector: 'app-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.scss']
})
export class CompanyComponent implements OnInit, OnDestroy {
  companyId: CompanyId;
  company: CompanyUnit;
  cycle: CycleUnit;

  subscriptions: Subscription[] = [];
  currentUserRole$: Observable<ContextRole>;

  currentlyMemberDepartmentIds: number[] = [];
  currentlyManagerDepartmentIds: number[] = [];

  constructor(
    private route: ActivatedRoute,
    private currentOkrViewService: CurrentOkrviewService,
    private currentDepartmentStructureService: CurrentDepartmentStructureService,
    private currentCompanyService: CurrentCompanyService,
    private currentCycleService: CurrentCycleService,
    private matDialog: MatDialog,
    private roleService: DepartmentContextRoleService,
    private excelFileService: ExcelMapper
  ) {
  }

  ngOnInit(): void {
    this.currentUserRole$ = this.roleService.getRoleWithoutContext$();

    this.subscriptions.push(
      this.route.paramMap.subscribe(params => {
        this.companyId = +params.get('companyId');
        this.currentOkrViewService.browseCompany(this.companyId);
      })
    );
    this.subscriptions.push(
      this.currentCompanyService.getCurrentCompany$()
        .subscribe(company => (this.company = company))
    );
    this.subscriptions.push(
      this.currentCycleService.getCurrentCycle$()
        .subscribe(currentCycle => (this.cycle = currentCycle))
    );
    this.subscriptions.push(
      this.currentDepartmentStructureService
        .getCurrentDepartmentStructures$()
        .subscribe(structures => this.calculateMembershipDepartmentIds(structures))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }

  calculateMembershipDepartmentIds(structures: DepartmentStructure[]): void {
    this.currentlyManagerDepartmentIds = [];
    this.currentlyMemberDepartmentIds = [];

    this.categorizeDepartmentStructureMemberships(structures);
  }

  categorizeDepartmentStructureMemberships(currentStructures: DepartmentStructure[]): void {
    if (currentStructures) {
      currentStructures.forEach(structure => {
        if (structure.userRole === DepartmentStructureRole.MEMBER) {
          this.currentlyMemberDepartmentIds.push(structure.id);
        } else if (structure.userRole === DepartmentStructureRole.MANAGER) {
          this.currentlyManagerDepartmentIds.push(structure.id);
        }
        this.categorizeDepartmentStructureMemberships(structure.subDepartments);
      });
    }
  }

  clickedAddSubDepartment(): void {
    const dialogReference: MatDialogRef<SubstructureFormComponent, object> = this.matDialog.open(SubstructureFormComponent, {
      data: {companyId: this.company.id}
    });

    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(
          take(1),
          filter(v => v as any),
          switchMap((dialogClosed$: ObservableInput<object>) => dialogClosed$)
        )
        .subscribe(addedSubDepartment => this.onSubDepartmentAdded(addedSubDepartment as DepartmentUnit))
    );
  }

  onSubDepartmentAdded(addedSubDepartment: DepartmentUnit): void {
    this.company.departmentIds.push(addedSubDepartment.id);
    this.currentOkrViewService.refreshCurrentCompanyView(this.company.id);
  }

  clickedDownloadExcelFileForCompany(): void {
    this.excelFileService.downloadExcelFileForCompany(this.company.id);
  }

  clickedDownloadExcelEmailFileForCompany(): void {
    this.excelFileService.downloadExcelEmailFileForCompany(this.companyId);
  }
}
