import { MediaMatcher } from '@angular/cdk/layout';
import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Observable, Subscription } from 'rxjs';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { OkrUnitSchema } from '../../shared/model/ui/okr-unit-schema';
import { CurrentOkrUnitSchemaService } from '../current-okr-unit-schema.service';
import { CurrentCompanyService } from '../current-company.service';

@Component({
  selector: 'app-navigation-sidebar',
  templateUrl: './navigation-sidebar.component.html',
  styleUrls: ['./navigation-sidebar.component.scss']
})
export class NavigationSidebarComponent implements OnInit, OnDestroy {
  @ViewChild('sideNav') sideNav: MatSidenav;

  mobileQuery: MediaQueryList;
  private readonly _mobileQueryListener: () => void;

  currentCompany$: Observable<CompanyUnit>;
  currentUnitSchema$: Observable<OkrUnitSchema[]>;
  navigationInformationSubscription: Subscription;

  constructor(
    changeDetectorRef: ChangeDetectorRef,
    media: MediaMatcher,
    private currentOkrUnitSchemaService: CurrentOkrUnitSchemaService,
    private currentCompanyService: CurrentCompanyService
  ) {
    this.mobileQuery = media.matchMedia('(max-width: 768px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnInit(): void {
    this.currentCompany$ = this.currentCompanyService.getCurrentCompany$();
    this.currentUnitSchema$ = this.currentOkrUnitSchemaService.getCurrentUnitSchemas$();
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }

  toggleSideNav(): void {
    this.sideNav.toggle();
  }

  closeSideNavMobileOnly(): void {
    if (this.mobileQuery.matches) {
      this.sideNav.close();
    }
  }
}
