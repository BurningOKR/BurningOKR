import { OkrChildUnitRoleService } from '../../shared/services/helper/okr-child-unit-role.service';
import { catchError, filter, map, switchMap, take, tap } from 'rxjs/operators';
import { CurrentOkrviewService } from '../current-okrview.service';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { Component, OnInit } from '@angular/core';
import { OkrUnitRole, OkrUnitSchema } from '../../shared/model/ui/okr-unit-schema';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { ExcelMapper } from '../excel-file/excel.mapper';
import { combineLatest, Observable, ObservableInput, of } from 'rxjs';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { OkrChildUnitFormComponent } from '../okr-child-unit/okr-child-unit-form/okr-child-unit-form.component';
import { CurrentOkrUnitSchemaService } from '../current-okr-unit-schema.service';
import { CurrentCycleService } from '../current-cycle.service';
import { ContextRole } from '../../shared/model/ui/context-role';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { UnitType } from '../../shared/model/api/OkrUnit/unit-type.enum';
import { OkrChildUnit } from '../../shared/model/ui/OrganizationalUnit/okr-child-unit';
import { TopicDraftCreationFormComponent } from '../okr-child-unit/okr-child-unit-form/topic-draft-creation-form/topic-draft-creation-form.component';
import { I18n } from '@ngx-translate/i18n-polyfill';

interface CompanyView {
  company: CompanyUnit;
  cycle: CycleUnit;
}

interface RoleDepartmentIds {
  currentlyMemberDepartmentIds: number[];
  currentlyManagerDepartmentIds: number[];
}

@Component({
  selector: 'app-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.scss']
})
export class CompanyComponent implements OnInit {
  company$: Observable<CompanyUnit>;
  cycle$: Observable<CycleUnit>;
  companyView$: Observable<CompanyView>;
  roleDepartmentIds$: Observable<RoleDepartmentIds>;

  currentUserRole$: Observable<ContextRole>;

  loadingError = false;

  constructor(
    private route: ActivatedRoute,
    private currentOkrViewService: CurrentOkrviewService,
    private currentOkrUnitSchemaService: CurrentOkrUnitSchemaService,
    private currentCycleService: CurrentCycleService,
    private companyMapperService: CompanyMapper,
    private matDialog: MatDialog,
    private roleService: OkrChildUnitRoleService,
    private excelFileService: ExcelMapper
  ) {
  }

  ngOnInit(): void {
    this.currentUserRole$ = this.roleService.getRoleWithoutContext$();

    this.company$ = this.route.paramMap
      .pipe(
        map((params: ParamMap) => +params.get('companyId')),
        switchMap((companyId: number) => this.companyMapperService.getCompanyById$(companyId, false)),
        catchError(() => {
          this.set404Error();

          return of(null);
        }),
        tap((company: CompanyUnit) => {
          if (company) {
            this.currentOkrViewService.browseCompany(company.id);
          }
        })
      );

    this.cycle$ = this.currentCycleService.getCurrentCycle$();

    this.companyView$ = combineLatest([this.company$, this.cycle$])
      .pipe(
        map(([company, cycle]: [CompanyUnit, CycleUnit]) => {
          return { company, cycle };
        })
      );

    this.roleDepartmentIds$ = this.currentOkrUnitSchemaService.getCurrentUnitSchemas$()
      .pipe(
        map((unitList: OkrUnitSchema[]) => this.categorizeSchemaMemberships(unitList))
      );
  }

  private categorizeSchemaMemberships(okrUnitSchemas: OkrUnitSchema[]): RoleDepartmentIds {
    let currentlyMemberDepartmentIds: number[] = [];
    let currentlyManagerDepartmentIds: number[] = [];

    if (okrUnitSchemas) {
      okrUnitSchemas.forEach(schema => {
        if (schema.userRole === OkrUnitRole.MEMBER) {
          currentlyMemberDepartmentIds.push(schema.id);
        } else if (schema.userRole === OkrUnitRole.MANAGER) {
          currentlyManagerDepartmentIds.push(schema.id);
        }
        const roleDepartmentIds: RoleDepartmentIds = this.categorizeSchemaMemberships(schema.subDepartments);
        currentlyManagerDepartmentIds = currentlyManagerDepartmentIds.concat(roleDepartmentIds.currentlyManagerDepartmentIds);
        currentlyMemberDepartmentIds = currentlyMemberDepartmentIds.concat(roleDepartmentIds.currentlyMemberDepartmentIds);
      });
    }

    return { currentlyMemberDepartmentIds, currentlyManagerDepartmentIds };
  }

  clickedAddSubBranch(company: CompanyUnit): void {
    const dialogReference: MatDialogRef<OkrChildUnitFormComponent, object> = this.matDialog.open(OkrChildUnitFormComponent, {
      data: { companyId: company.id, unitType: UnitType.OKR_BRANCH }
    });

    this.handleDialogClosed(dialogReference, company);
  }

  clickedAddSubDepartment(company: CompanyUnit): void {
    const dialogReference: MatDialogRef<OkrChildUnitFormComponent, object> = this.matDialog.open(OkrChildUnitFormComponent, {
      data: { companyId: company.id, unitType: UnitType.DEPARTMENT }
    });

    this.handleDialogClosed(dialogReference, company);
  }

  private handleDialogClosed(dialogReference: MatDialogRef<OkrChildUnitFormComponent>, company: CompanyUnit): void {
    dialogReference
      .afterClosed()
      .pipe(
        take(1),
        filter(v => v),
        switchMap((dialogClosed$: ObservableInput<object>) => dialogClosed$)
      )
      .subscribe(addedSubDepartment => this.onSubDepartmentAdded(company, addedSubDepartment as OkrChildUnit));
  }

  private onSubDepartmentAdded(company: CompanyUnit, addedSubDepartment: OkrChildUnit): void {
    company.okrChildUnitIds.push(addedSubDepartment.id);
    this.currentOkrViewService.refreshCurrentCompanyView(company.id);
  }

  clickedDownloadExcelFileForCompany(company: CompanyUnit): void {
    this.excelFileService.downloadExcelFileForCompany(company.id);
  }

  clickedDownloadExcelEmailFileForCompany(company: CompanyUnit): void {
    this.excelFileService.downloadExcelEmailFileForCompany(company.id);
  }

  private set404Error(): void {
    this.loadingError = true;
  }
}
