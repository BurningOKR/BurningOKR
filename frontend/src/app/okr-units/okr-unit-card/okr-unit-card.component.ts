import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { Subscription } from 'rxjs';
import { CycleMapper } from '../../shared/services/mapper/cycle.mapper';
import { Router } from '@angular/router';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { CycleWithHistoryCompany } from '../../shared/model/ui/cycle-with-history-company';
import { filter, switchMap, take } from 'rxjs/operators';
import { CurrentUserService } from '../../core/services/current-user.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { OkrUnitFormComponent } from '../okr-unit-form/okr-unit-form.component';
import { CompanyApiService } from '../../shared/services/api/company-api.service';
import { DeleteDialogComponent } from '../../shared/components/delete-dialog/delete-dialog.component';
import { DeleteDialogData } from '../../shared/model/ui/delete-dialog-data';
import { environment } from '../../../environments/environment';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-okr-unit-card',
  templateUrl: './okr-unit-card.component.html',
  styleUrls: ['./okr-unit-card.component.scss']
})
export class OkrUnitCardComponent implements OnInit, OnDestroy {
  @Input() company: CompanyUnit;
  @Output() companyDeleted: EventEmitter<CompanyUnit> = new EventEmitter<CompanyUnit>();
  @Output() newCycleStarted: EventEmitter<void> = new EventEmitter<void>();

  cyclesWithHistoryCompanies: CycleWithHistoryCompany[];
  chosenCycleWithHistoryCompany: CycleWithHistoryCompany;
  activeCycle: CycleUnit;
  isCurrentUserAdmin = false;
  isPlayground: boolean = environment.playground;

  private subscriptions: Subscription[] = [];

  constructor(
    private cycleMapper: CycleMapper,
    private companyMapper: CompanyMapper,
    private formDialog: MatDialog,
    private companyApiService: CompanyApiService,
    private router: Router,
    private currentUserService: CurrentUserService,
    private translate: TranslateService
) {
  }

  ngOnInit(): void {
    this.subscriptions.push(
      this.currentUserService
        .isCurrentUserAdmin$()
        .subscribe((isAdmin: boolean) => {
          this.isCurrentUserAdmin = isAdmin.valueOf();
        })
    );
    this.subscriptions.push(this.cycleMapper.getCycleById$(this.company.cycleId)
      .subscribe((cycle: CycleUnit) => {
      this.chosenCycleWithHistoryCompany = new CycleWithHistoryCompany(cycle, this.company);
      this.activeCycle = cycle;
    }));
    this.loadCyclesWithHistoryCompanies$();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub: Subscription) => sub.unsubscribe());
  }

  selectCompany(): void {
    if (this.chosenCycleWithHistoryCompany) {
      this.router.navigate([`okr/companies/${this.chosenCycleWithHistoryCompany.company.id}`], { replaceUrl: true })
        .catch();
    }
  }

  editCompany(): void {
    this.subscriptions.push(
      this.formDialog.open(OkrUnitFormComponent, {
        data: {
          company: this.company
        }
      })
        .afterClosed()
        .pipe(
          take(1),
          filter(x => x),
          switchMap(x => x)
        )
        .subscribe((newCompany: CompanyUnit) => {
          this.company.name = newCompany.name;
        })
    );
  }

  deleteCompany(): void {
    const data: object = this.getDataForCompanyDeletionDialog();
    const dialogRef: MatDialogRef<DeleteDialogComponent> = this.formDialog.open(DeleteDialogComponent, data);

    dialogRef.afterClosed()
        .pipe(
      filter(v => v),
      switchMap(() => {
        return  this.companyApiService.deleteCompanyById$(this.company.id);
      }))
      .subscribe(() => {
        this.companyDeleted.emit(this.company);
      });
  }

  onSelectCycle($event: { value: number }): void {
    this.chosenCycleWithHistoryCompany = this.cyclesWithHistoryCompanies.find(value => value.cycle.id === $event.value);
  }

  hasChildUnit(): boolean {
    return this.company.okrChildUnitIds.length > 0;
  }

  private getDataForCompanyDeletionDialog(): {data: DeleteDialogData} {
    return {
      data: {
        title: this.translate.instant('okr-unit-card.label'),
        objectNameWithArticle: this.company.name,
        dangerContent: this.getChildUnitWarningIfNecessary()
      }
    };
  }

  private loadCyclesWithHistoryCompanies$(): void {
    this.subscriptions.push(
      this.companyMapper
        .getCyclesWithHistoryCompanies$(this.company.id)
        .subscribe((cyclesWithHistoryCompanies: CycleWithHistoryCompany[]) => {
          this.cyclesWithHistoryCompanies = cyclesWithHistoryCompanies.sort((a, b) => {
            return a.cycle.startDate < b.cycle.startDate ? 1 : a.cycle.startDate === b.cycle.startDate ? 0 : -1;
          });
        })
    );
  }

  private getChildUnitWarningIfNecessary(): string {
    if (this.hasChildUnit()) {
      return this.translate.instant('okr-unit-card.delete-company-has-child-unit-warning',
        {value: this.company.name});
    } else {
      return '';
    }
  }
}
