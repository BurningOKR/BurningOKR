import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { OkrTopicDraft } from '../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { TopicDraftMapper } from '../shared/services/mapper/topic-draft-mapper';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TopicDraftCreationFormComponent } from '../okrview/okr-child-unit/okr-child-unit-form/topic-draft-creation-form/topic-draft-creation-form.component';
import { filter, switchMap, take } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-submitted-topic-drafts',
  templateUrl: './submitted-topic-drafts.component.html',
  styleUrls: ['./submitted-topic-drafts.component.css']
})
export class SubmittedTopicDraftsComponent implements OnInit, OnDestroy {

  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  topicDrafts$: Observable<OkrTopicDraft[]>;
  columnsToDisplay = ['topic', 'initiator', 'beginning', 'contributesTo', 'status', 'comments', 'actions'];

  rowData = new MatTableDataSource([] as OkrTopicDraft[]);

   topicTableHeader: string;

  subscriptions: Subscription[] = [];

  constructor(private router: Router,
              private topicDraftMapper: TopicDraftMapper,
              private i18n: I18n,
              private translate: TranslateService,
              private snackBar: MatSnackBar,
              private matDialog: MatDialog
              ) {
  }

  clickedAddTopicDraft(): void {
    // creates fitting config for either okrbranch id or company id
    const config: any = {width: '600px', data: {}};

    const dialogReference: MatDialogRef<TopicDraftCreationFormComponent> = this.matDialog.open(TopicDraftCreationFormComponent, config);

    dialogReference
      .afterClosed()
      .pipe(
        take(1),
        filter(v => v),
        switchMap(n => n)
      )
      .subscribe(() => {
        this.loadAllTopicDrafts();
      });
  }

  ngOnInit(): void {
    this.loadAllTopicDrafts();
    this.rowData.sort = this.sort;
    this.rowData.paginator = this.paginator;
  }
  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  loadAllTopicDrafts(): void {

    this.getAllTopicDrafts();

    const subscription: Subscription = this.topicDrafts$.subscribe(topicDrafts => {
      this.rowData.data = topicDrafts;
    });
    this.subscriptions.push(subscription);
  }

  navigateToCompanies(): void {
    this.router.navigate(['companies'])
        .catch();
  }

  getAllTopicDrafts(): void {
    this.topicDrafts$ = this.topicDraftMapper.getAllTopicDrafts$();
  }
}
