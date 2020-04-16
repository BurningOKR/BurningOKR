import { MediaMatcher } from '@angular/cdk/layout';
import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Observable, Subscription } from 'rxjs';
import { CurrentOkrviewService } from '../current-okrview.service';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { DepartmentStructure } from '../../shared/model/ui/department-structure';
import { CurrentNavigationService } from '../current-navigation.service';
import { CurrentDepartmentStructureService } from '../current-department-structure.service';
import { DepartmentNavigationInformation } from '../../shared/model/ui/department-navigation-information';

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
  currentDepartmentStructure$: Observable<DepartmentStructure[]>;
  currentNavigationInformation = new  DepartmentNavigationInformation(-1, []);
  navigationInformationSubscription: Subscription;

  constructor(
    changeDetectorRef: ChangeDetectorRef,
    media: MediaMatcher,
    private currentOkrViewService: CurrentOkrviewService,
    private currentNavigationService: CurrentNavigationService,
    private currentDepartmentStructureService: CurrentDepartmentStructureService
  ) {
    this.mobileQuery = media.matchMedia('(max-width: 700px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnInit(): void {
    this.currentCompany$ = this.currentOkrViewService.getCurrentCompany$();
    this.currentDepartmentStructure$ = this.currentDepartmentStructureService.getCurrentDepartmentStructureList$();
    this.navigationInformationSubscription = this.currentNavigationService
      .getCurrentDepartmentNavigationInformation$()
      .subscribe(x => (this.currentNavigationInformation = x));
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
    this.navigationInformationSubscription.unsubscribe();
  }

  toggleOpenSideNav(): void {
    this.sideNav.toggle();
  }
}
