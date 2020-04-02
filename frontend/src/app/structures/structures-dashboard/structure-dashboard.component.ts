import { Component, OnDestroy, OnInit } from '@angular/core';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { Observable, Subscription } from 'rxjs';
import { CurrentUserService } from '../../core/services/current-user.service';
import { ActiveCompaniesService } from './active-companies.service';
import { User } from '../../shared/model/api/user';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { StructureFormComponent } from '../structure-form/structure-form.component';
import { filter, switchMap, take } from 'rxjs/operators';

@Component({
  selector: 'app-structures-dashboard',
  templateUrl: './structure-dashboard.component.html',
  styleUrls: ['./structure-dashboard.component.scss']
})
export class StructureDashboardComponent implements OnInit, OnDestroy {
  currentUser: User;
  companies$: Observable<CompanyUnit[]>;
  isCurrentUserAdmin: Observable<boolean>;

  private addCompanySubscription: Subscription;

  constructor(
    private activeCompaniesService: ActiveCompaniesService,
    private currentUserService: CurrentUserService,
    private companyFormDialog: MatDialog
  ) {
  }

  // TODO: no async on init
  async ngOnInit(): Promise<void> {
    this.companies$ = this.activeCompaniesService.getCompaniesResult$();
    this.updateCompanies();
    this.isCurrentUserAdmin = this.currentUserService.isCurrentUserAdmin();

    // TODO Fix
    await this.currentUserService.getCurrentUser()
      .toPromise()
      .then(currentUser => this.currentUser = currentUser);
  }

  ngOnDestroy(): void {
    if (this.addCompanySubscription) {
      this.addCompanySubscription.unsubscribe();
    }
  }

  private updateCompanies(): void {
    this.activeCompaniesService.triggerCompaniesUpdate();
  }

  addCompany(): void {
    const dialogRef: MatDialogRef<StructureFormComponent, any> = this.companyFormDialog.open(StructureFormComponent, {
      data: {}
    });

    dialogRef.afterClosed()
      .pipe(take(1),
        filter(v => v),
        switchMap(v => v))
      .subscribe(() => {
        this.updateCompanies();
      });
  }
}
