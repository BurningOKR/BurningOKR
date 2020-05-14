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
import { MatDialog, MatDialogRef } from '@angular/material';
import { StructureFormComponent } from '../structure-form/structure-form.component';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { CompanyApiService } from '../../shared/services/api/company-api.service';
import { DeleteDialogComponent } from '../../shared/components/delete-dialog/delete-dialog.component';
import { DeleteDialogData } from '../../shared/model/ui/delete-dialog-data';

@Component({
  selector: 'app-structure-card',
  templateUrl: './structure-card.component.html',
  styleUrls: ['./structure-card.component.scss']
})
export class StructureCardComponent implements OnInit, OnDestroy {
  @Input() company: CompanyUnit;
  @Output() companyDeleted: EventEmitter<CompanyUnit> = new EventEmitter<CompanyUnit>();
  @Output() newCycleStarted: EventEmitter<void> = new EventEmitter<void>();

  cyclesWithHistoryCompanies: CycleWithHistoryCompany[];
  chosenCycleWithHistoryCompany: CycleWithHistoryCompany;
  activeCycle: CycleUnit;
  isCurrentUserAdmin = false;

  private subscriptions: Subscription[] = [];

  constructor(
    private cycleMapper: CycleMapper,
    private companyMapper: CompanyMapper,
    private formDialog: MatDialog,
    private companyApiService: CompanyApiService,
    private router: Router,
    private currentUserService: CurrentUserService,
    private i18n: I18n
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
      this.formDialog.open(StructureFormComponent, {
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

  private getDataForCompanyDeletionDialog(): {data: DeleteDialogData} {
    return {
      data: {
        title: this.getTitleForCompanyDeletionDialog(),
        objectNameWithArticle: this.getContentDescriptionForCompanyDeletionDialog(),
        dangerContent: this.getSubstructureWarningIfNecessary()
      }
    };
  }

  private getTitleForCompanyDeletionDialog(): string {
    return this.i18n({
      id: 'generalDeleteDialogTitle',
      description: 'General title for deleting of an object',
      value: '{{objectName}} löschen?'
    }, {objectName: this.company.name});
  }

  private getContentDescriptionForCompanyDeletionDialog(): string {
    return this.i18n({
      id: 'companyWithArticle',
      value: 'die Firma'
    });
  }

  private getSubstructureWarningIfNecessary(): string {
    if (this.hasSubStructure()) {
      return this.i18n({
        id: 'deleteCompanyHasSubstructuresWarning',
        description: 'Warn the user if the company still has substructures',
        value: '{{companyName}} enthält noch Unterstrukturen'
      }, {companyName: this.company.name});
    } else {
      return '';
    }
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

  onSelectCycle($event: { value: number; }): void {
    this.chosenCycleWithHistoryCompany = this.cyclesWithHistoryCompanies.find(value => value.cycle.id === $event.value);
  }

  hasSubStructure(): boolean {
    return this.company.departmentIds.length > 0 && this.company.corporateObjectiveStructureIds.length > 0;
  }
}
