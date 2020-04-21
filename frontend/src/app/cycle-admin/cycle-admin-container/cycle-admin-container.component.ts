import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { MatDialog } from '@angular/material';
import { CycleCreationFormComponent } from '../cycle-creation-form/cycle-creation-form.component';
import { filter, switchMap, take } from 'rxjs/operators';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';

@Component({
  selector: 'app-cycle-admin-container',
  templateUrl: './cycle-admin-container.component.html',
  styleUrls: ['./cycle-admin-container.component.scss']
})
export class CycleAdminContainerComponent implements OnInit {
  cycles$: Observable<CycleUnit[]>;

  company: CompanyUnit;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private companyService: CompanyMapper,
              private cycleCreationDialog: MatDialog) {
  }

  ngOnInit(): void {
    const companyId: number = Number(this.route.snapshot.paramMap.get('companyId'));
    this.companyService.getCompanyById$(companyId)
      .pipe(take(1))
      .subscribe(
        (company: CompanyUnit) => {
          this.company = company;
          this.loadCycles();
    });
  }

  createCycle(): void {
    const cycleDialogData: object = {
      data: {
        company: this.company,
        cycles$: this.cycles$
      }
    };
    this.cycleCreationDialog.open(CycleCreationFormComponent, cycleDialogData)
      .afterClosed()
      .pipe(
        take(1),
        filter(x => x),
        switchMap(x => x)
      )
      .subscribe(() => this.loadCycles());
  }

  private loadCycles(): void {
    this.cycles$ = this.companyService.getCyclesOfCompanyHistory$(this.company.id);
  }

  navigateToCompanies(): void {
    this.router.navigate(['/companies'])
      .catch();
  }

  routeToCompany(): void {
    this.router.navigate(['/okr', '/companies', this.company.id]);
  }
}
