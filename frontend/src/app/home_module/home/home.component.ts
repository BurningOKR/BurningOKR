import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { CycleTransferService } from './cycle-transfer.service';
import { NavigationEnd, Router } from '@angular/router';
import { CycleState, CycleUnit } from '../../shared/model/ui/cycle-unit';
import { CycleWithHistoryCompany } from '../../shared/model/ui/cycle-with-history-company';
import { filter } from 'rxjs/operators';
import { CycleMapper } from '../../shared/services/mapper/cycle.mapper';
import { MatSidenav } from '@angular/material';
import { Subscription } from 'rxjs';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { MediaMatcher } from '@angular/cdk/layout';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {

  @ViewChild('sidenav', { static: true }) sidenav: MatSidenav;

  finishedLoadingMenu: boolean;
  showForeCast: boolean;
  cycleId: number;
  isActiveCycleState;
  isPreparationCycleState;
  isClosedCycleState;
  mobileQuery: MediaQueryList;
  cyclesWithHistoryCompanies: CycleWithHistoryCompany[];
  cycleWithHistoryCompany: CycleWithHistoryCompany;
  company: CompanyUnit;

  private subscriptions: Subscription[] = [];
  private readonly mobileQueryListener: () => void;

  constructor(
    private cdRef: ChangeDetectorRef,
    private media: MediaMatcher,
    private router: Router,
    private companyMapperService: CompanyMapper,
    private cycleMapperService: CycleMapper,
    private cycleService: CycleTransferService,
  ) {
    this.mobileQuery = media.matchMedia('(max-width: 768px');
    this.mobileQueryListener = () => this.cdRef.detectChanges();
    this.mobileQuery.addListener(this.mobileQueryListener);
  }

  ngOnInit(): void {
    this.subscriptions.push(
      this.router.events.pipe(filter(e => e instanceof NavigationEnd))
        .subscribe(() => {
        if (this.mobileQuery.matches) {
          this.sidenav.close();
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub: Subscription) => sub.unsubscribe());
    this.subscriptions = [];
    this.mobileQuery.removeListener(this.mobileQueryListener);
  }

  onCompanyLoaded(company: CompanyUnit): void {
    this.company = company;
    this.loadCycleByCompany(company);
    this.loadCyclesWithHistoryCompanies(company.id);
  }

  loadCycleByCompany(company: CompanyUnit): void {
    this.subscriptions.push(
      this.cycleMapperService.getCycleById$(company.cycleId)
        .subscribe((cycle: CycleUnit) => {
        this.cycleWithHistoryCompany = new CycleWithHistoryCompany(cycle, company);
        this.cycleId = cycle.id;
        this.cycleService.exportCycle(this.cycleWithHistoryCompany.cycle);
      })
    );
  }

  loadCyclesWithHistoryCompanies(companyId: number): void {
    this.subscriptions.push(
      this.companyMapperService
        .getCyclesWithHistoryCompanies$(companyId)
        .subscribe((cyclesWithHistoryCompanies: CycleWithHistoryCompany[]) => {
          this.cyclesWithHistoryCompanies = cyclesWithHistoryCompanies.sort((a, b) => {
            return a.cycle.startDate > b.cycle.startDate ? -1 : 1;
          });
          this.setLocalCycleStateValues();
        })
    );
  }

  navigateToCompanies(): void {
    this.router.navigate(['/companies']);
  }

  setLocalCycleStateValues(): void {
    if (this.cycleWithHistoryCompany.cycle) {
      this.isActiveCycleState = this.cycleWithHistoryCompany.cycle.cycleState === CycleState.ACTIVE;
      this.isPreparationCycleState = this.cycleWithHistoryCompany.cycle.cycleState === CycleState.PREPARATION;
      this.isClosedCycleState = this.cycleWithHistoryCompany.cycle.cycleState === CycleState.CLOSED;
    }
  }

  onSelectCycle($event: { value: number; }): void {
    this.cycleWithHistoryCompany = this.cyclesWithHistoryCompanies.find(value => value.cycle.id === $event.value);
    this.router.navigate([`/element/company-`, this.cycleWithHistoryCompany.company.id]);
  }
}
