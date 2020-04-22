import { Component, OnInit } from '@angular/core';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { Observable } from 'rxjs';
import { CurrentUserService } from '../../core/services/current-user.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { StructureFormComponent } from '../structure-form/structure-form.component';
import { filter, switchMap, take } from 'rxjs/operators';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';

@Component({
  selector: 'app-structures-dashboard',
  templateUrl: './structure-dashboard.component.html',
  styleUrls: ['./structure-dashboard.component.scss']
})
export class StructureDashboardComponent implements OnInit {
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
    const dialogRef: MatDialogRef<StructureFormComponent, any> = this.companyFormDialog.open(StructureFormComponent, {
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
