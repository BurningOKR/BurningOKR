import { MediaMatcher } from '@angular/cdk/layout';
import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Observable, Subscription } from 'rxjs';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { StructureSchema } from '../../shared/model/ui/structure-schema';
import { CurrentStructureSchemeService } from '../current-structure-scheme.service';
import { CurrentCompanyService } from '../current-company.service';

@Component({
  selector: 'app-navigation-sidebar',
  templateUrl: './navigation-sidebar.component.html',
  styleUrls: ['./navigation-sidebar.component.scss']
})
export class NavigationSidebarComponent implements OnInit, OnDestroy {
  @ViewChild('sideNav', { static: false }) sideNav: MatSidenav;

  mobileQuery: MediaQueryList;
  private readonly _mobileQueryListener: () => void;

  currentCompany$: Observable<CompanyUnit>;
  currentstructureSchema$: Observable<StructureSchema[]>;
  navigationInformationSubscription: Subscription;

  constructor(
    changeDetectorRef: ChangeDetectorRef,
    media: MediaMatcher,
    private currentstructureSchemaService: CurrentStructureSchemeService,
    private currentCompanyService: CurrentCompanyService
  ) {
    this.mobileQuery = media.matchMedia('(max-width: 700px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnInit(): void {
    this.currentCompany$ = this.currentCompanyService.getCurrentCompany$();
    this.currentstructureSchema$ = this.currentstructureSchemaService.getCurrentStructureSchemas$();
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }

  toggleOpenSideNav(): void {
    this.sideNav.toggle();
  }
}
