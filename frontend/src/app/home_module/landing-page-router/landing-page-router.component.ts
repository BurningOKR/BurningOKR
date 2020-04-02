import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { HealthcheckApiService } from '../../shared/services/api/healthcheck-api.service';

@Component({
  selector: 'app-landing-page-router',
  templateUrl: './landing-page-router.component.html',
  styleUrls: ['./landing-page-router.component.scss']
})
export class LandingPageRouterComponent implements OnInit, OnDestroy {
  subscription: Subscription;

  constructor(private companyMapperService: CompanyMapper,
              private router: Router,
              private healthcheckService: HealthcheckApiService) {
  }

  ngOnInit(): void {
    this.healthcheckService.isAlive$()
      .subscribe(
      () => {
        this.subscription = this.companyMapperService.getActiveCompanies$()
          .subscribe(uniqueCompanies => {
            if (uniqueCompanies.length === 1) {
              setTimeout(() => {
                this.router.navigate(['/okr/companies/', uniqueCompanies[0].id])
                  .catch();
              }, 100);
            } else {
              setTimeout(() => {
                this.router.navigate(['/companies'])
                  .catch();
              }, 100);
            }
          });
      }, () => this.router.navigate(['/error'])
    );
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
