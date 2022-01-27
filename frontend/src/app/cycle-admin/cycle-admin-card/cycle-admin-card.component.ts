import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { filter, map, startWith, switchMap, take } from 'rxjs/operators';
import { DeleteDialogComponent } from '../../shared/components/delete-dialog/delete-dialog.component';
import { CycleState, CycleUnit } from '../../shared/model/ui/cycle-unit';
import { DeleteDialogData } from '../../shared/model/ui/delete-dialog-data';
import { CycleMapper } from '../../shared/services/mapper/cycle.mapper';
import { CycleEditFormComponent, CycleEditFormData } from '../cycle-edit-form/cycle-edit-form.component';

@Component({
  selector: 'app-cycle-admin-card',
  templateUrl: './cycle-admin-card.component.html',
  styleUrls: ['./cycle-admin-card.component.scss'],
})
export class CycleAdminCardComponent {

  @Input() cycle: CycleUnit;
  @Input() allCyclesOfCompany$: Observable<CycleUnit[]>;
  @Output() cycleChanged: EventEmitter<CycleUnit> = new EventEmitter<CycleUnit>();

  cycleState = CycleState;
  currentLang$: Observable<string>;

  constructor(
    private translate: TranslateService,
    private dialog: MatDialog,
    private cycleMapper: CycleMapper,
  ) {
    this.currentLang$ = translate.onLangChange.pipe(map(lang => lang.lang), startWith(translate.currentLang));
  }

  deleteCycle(): void {
    const data: { data: DeleteDialogData } = this.getDataForCycleDeletionDialog();

    this.dialog.open(DeleteDialogComponent, data)
      .afterClosed()
      .pipe(
        filter(v => v),
        switchMap(() => {
          return this.cycleMapper.deleteCycleById$(this.cycle.id);
        }))
      .subscribe(() => {
        this.cycleChanged.emit(this.cycle);
      });
  }

  getDataForCycleDeletionDialog(): { data: DeleteDialogData } {
    return {
      data: {
        title: this.getCycleTranslation(),
        objectNameWithArticle: this.getGeneralDeleteDialogContentTranslation(),
      },
    };
  }

  getCycleTranslation(): string {
    return this.translate.instant('cycle-admin-card.deletion-dialog.title');
  }

  getGeneralDeleteDialogContentTranslation(): string {
    return this.translate.instant('cycle-admin-card.deletion-dialog.content', {
      cycleStart: this.cycle.startDate.toLocaleDateString(),
      cycleEnd: this.cycle.endDate.toLocaleDateString(),
    });
  }

  editCycle(): void {
    const data: CycleEditFormData = {
      cycle: this.cycle,
      allCyclesOfCompany$: this.allCyclesOfCompany$,
    };

    this.dialog.open(CycleEditFormComponent, { data })
      .afterClosed()
      .pipe(
        take(1),
        filter(x => x),
        switchMap(x => x),
      )
      .subscribe((cycle: CycleUnit) => {
        this.cycle = cycle;
        this.cycleChanged.emit(this.cycle);
      });
  }
}
