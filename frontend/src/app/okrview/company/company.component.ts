import {
  OkrChildUnitRoleService
} from '../../shared/services/helper/okr-child-unit-role.service';
import { filter, switchMap, take } from 'rxjs/operators';
import { CurrentOkrviewService } from '../current-okrview.service';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { OkrUnitSchema, OkrUnitRole } from '../../shared/model/ui/okr-unit-schema';
import { MatDialog, MatDialogRef } from '@angular/material';
import { OkrDepartment } from '../../shared/model/ui/OrganizationalUnit/okr-department';
import { ActivatedRoute } from '@angular/router';
import { ExcelMapper } from '../excel-file/excel.mapper';
import { Observable, ObservableInput, Subscription } from 'rxjs';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { OkrChildUnitFormComponent } from '../okr-child-unit/okr-child-unit-form/okr-child-unit-form.component';
import { CurrentOkrUnitSchemaService } from '../current-okr-unit-schema.service';
import { CurrentCompanyService } from '../current-company.service';
import { CurrentCycleService } from '../current-cycle.service';
import { CompanyId } from '../../shared/model/id-types';
import { ContextRole } from '../../shared/model/ui/context-role';

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
    private currentOkrUnitSchemaService: CurrentOkrUnitSchemaService,
    private currentCompanyService: CurrentCompanyService,
    private currentCycleService: CurrentCycleService,
    private matDialog: MatDialog,
    private roleService: OkrChildUnitRoleService,
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
      this.currentOkrUnitSchemaService
        .getCurrentUnitSchemas$()
        .subscribe(unitList => this.calculateMembershipDepartmentIds(unitList))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }

  calculateMembershipDepartmentIds(okrUnitSchemas: OkrUnitSchema[]): void {
    this.currentlyManagerDepartmentIds = [];
    this.currentlyMemberDepartmentIds = [];

    this.categorizeSchemaMemberships(okrUnitSchemas);
  }

  categorizeSchemaMemberships(okrUnitSchemas: OkrUnitSchema[]): void {
    if (okrUnitSchemas) {
      okrUnitSchemas.forEach(schema => {
        if (schema.userRole === OkrUnitRole.MEMBER) {
          this.currentlyMemberDepartmentIds.push(schema.id);
        } else if (schema.userRole === OkrUnitRole.MANAGER) {
          this.currentlyManagerDepartmentIds.push(schema.id);
        }
        this.categorizeSchemaMemberships(schema.subDepartments);
      });
    }
  }

  clickedAddSubDepartment(): void {
    const dialogReference: MatDialogRef<OkrChildUnitFormComponent, object> = this.matDialog.open(OkrChildUnitFormComponent, {
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
        .subscribe(addedSubDepartment => this.onSubDepartmentAdded(addedSubDepartment as OkrDepartment))
    );
  }

  onSubDepartmentAdded(addedSubDepartment: OkrDepartment): void {
    this.company.childUnitIds.push(addedSubDepartment.id);
    this.currentOkrViewService.refreshCurrentCompanyView(this.company.id);
  }

  clickedDownloadExcelFileForCompany(): void {
    this.excelFileService.downloadExcelFileForCompany(this.company.id);
  }

  clickedDownloadExcelEmailFileForCompany(): void {
    this.excelFileService.downloadExcelEmailFileForCompany(this.companyId);
  }
}
