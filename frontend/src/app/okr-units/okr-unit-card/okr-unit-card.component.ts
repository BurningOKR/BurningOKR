import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { filter, switchMap, take } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { CurrentUserService } from '../../core/services/current-user.service';
import { DeleteDialogComponent } from '../../shared/components/delete-dialog/delete-dialog.component';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { CycleWithHistoryCompany } from '../../shared/model/ui/cycle-with-history-company';
import { DeleteDialogData } from '../../shared/model/ui/delete-dialog-data';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { CompanyApiService } from '../../shared/services/api/company-api.service';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { CycleMapper } from '../../shared/services/mapper/cycle.mapper';
import { OkrUnitFormComponent } from '../okr-unit-form/okr-unit-form.component';

@Component({
  selector: 'app-okr-unit-card',
  templateUrl: './okr-unit-card.component.html',
  styleUrls: ['./okr-unit-card.component.scss'],
})
export class OkrUnitCardComponent implements OnInit {
  @Input() company: CompanyUnit;
  @Output() companyDeleted: EventEmitter<CompanyUnit> = new EventEmitter<CompanyUnit>();
  @Output() newCycleStarted: EventEmitter<void> = new EventEmitter<void>();

  cyclesWithHistoryCompanies: CycleWithHistoryCompany[];
  chosenCycleWithHistoryCompany: CycleWithHistoryCompany;
  activeCycle: CycleUnit;
  isCurrentUserAdmin = false;
  isPlayground: boolean = environment.playground;

  constructor(
    private cycleMapper: CycleMapper,
    private companyMapper: CompanyMapper,
    private formDialog: MatDialog,
    private companyApiService: CompanyApiService,
    private router: Router,
    private currentUserService: CurrentUserService,
    private translate: TranslateService,
  ) {
  }

  ngOnInit(): void {
    this.currentUserService
      .isCurrentUserAdmin$().pipe(take(1))
      .subscribe((isAdmin: boolean) => {
        this.isCurrentUserAdmin = isAdmin.valueOf();
      });
    this.cycleMapper.getCycleById$(this.company.cycleId).pipe(take(1))
      .subscribe((cycle: CycleUnit) => {
        this.chosenCycleWithHistoryCompany = new CycleWithHistoryCompany(cycle, this.company);
        this.activeCycle = cycle;
      });
    this.loadCyclesWithHistoryCompanies$();
  }

  selectCompany(): void {
    if (this.chosenCycleWithHistoryCompany) {
      this.router.navigate([`okr/companies/${this.chosenCycleWithHistoryCompany.company.id}`], { replaceUrl: false })
        .catch();
    }
  }

  editCompany(): void {
    this.formDialog.open(OkrUnitFormComponent, {
      data: {
        company: this.company,
      },
    })
      .afterClosed()
      .pipe(
        take(1),
        filter(x => x),
        switchMap(x => x),
      )
      .subscribe((newCompany: CompanyUnit) => {
        this.company.name = newCompany.name;
      });
  }

  deleteCompany(): void {
    const data: object = this.getDataForCompanyDeletionDialog();
    const dialogRef: MatDialogRef<DeleteDialogComponent> = this.formDialog.open(DeleteDialogComponent, data);

    dialogRef.afterClosed()
      .pipe(
        filter(v => v),
        switchMap(() => {
          return this.companyApiService.deleteCompanyById$(this.company.id);
        }),
      )
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

  private getDataForCompanyDeletionDialog(): { data: DeleteDialogData } {
    return {
      data: {
        title: this.translate.instant('okr-unit-card.label'),
        objectNameWithArticle: this.company.name,
        dangerContent: this.getChildUnitWarningIfNecessary(),
      },
    };
  }

  private loadCyclesWithHistoryCompanies$(): void {
    this.companyMapper
      .getCyclesWithHistoryCompanies$(this.company.id).pipe(take(1))
      .subscribe((cyclesWithHistoryCompanies: CycleWithHistoryCompany[]) => {
        this.cyclesWithHistoryCompanies = cyclesWithHistoryCompanies.sort((a, b) => {
          return a.cycle.startDate < b.cycle.startDate ? 1 : a.cycle.startDate === b.cycle.startDate ? 0 : -1;
        });
      });
  }

  private getChildUnitWarningIfNecessary(): string {
    if (this.hasChildUnit()) {
      return this.translate.instant(
        'okr-unit-card.delete-company-has-child-unit-warning',
        { value: this.company.name },
      );
    } else {
      return '';
    }
  }
}
