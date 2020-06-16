import { Component, OnInit } from '@angular/core';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { Observable } from 'rxjs';
import { CurrentUserService } from '../../core/services/current-user.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { OkrUnitFormComponent } from '../okr-unit-form/okr-unit-form.component';
import { filter, switchMap, take } from 'rxjs/operators';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';

@Component({
  selector: 'app-okr-unit-dashboard',
  templateUrl: './okr-unit-dashboard.component.html',
  styleUrls: ['./okr-unit-dashboard.component.scss']
})
export class OkrUnitDashboardComponent implements OnInit {
  companies$: Observable<CompanyUnit[]>;
  isCurrentUserAdmin$: Observable<boolean>;

  constructor(
    private currentUserService: CurrentUserService,
    private companyFormDialog: MatDialog,
    private companyMapperService: CompanyMapper
  ) {
  }

  ngOnInit(): void {
    this.isCurrentUserAdmin$ = this.currentUserService.isCurrentUserAdmin$();
    this.updateCompanies();
  }

  private updateCompanies(): void {
    this.companies$ = this.companyMapperService.getActiveCompanies$();
  }

  addCompany(): void {
    const dialogRef: MatDialogRef<OkrUnitFormComponent, any> = this.companyFormDialog.open(OkrUnitFormComponent, {
      data: {}
    });

    dialogRef.afterClosed()
      .pipe(
        take(1),
        filter(v => v),
        switchMap(v => v))
      .subscribe(() => {
        this.updateCompanies();
      });
  }
}
