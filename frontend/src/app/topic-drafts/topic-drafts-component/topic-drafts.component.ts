import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { filter, switchMap, take } from 'rxjs/operators';
import { TopicDraftCreationFormComponent } from '../../okrview/okr-child-unit/okr-child-unit-form/topic-draft-creation-form/topic-draft-creation-form.component';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { TopicDraftMapper } from '../../shared/services/mapper/topic-draft-mapper';

@Component({
  selector: 'app-topic-drafts',
  templateUrl: './topic-drafts.component.html',
  styleUrls: ['./topic-drafts.component.css'],
})
export class TopicDraftsComponent implements OnInit {

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  columnsToDisplay = ['topic', 'initiator', 'beginning', 'contributesTo', 'status', 'comments', 'actions'];

  rowData = new MatTableDataSource([] as OkrTopicDraft[]);

  constructor(
    private router: Router,
    private topicDraftMapper: TopicDraftMapper,
    private translate: TranslateService,
    private snackBar: MatSnackBar,
    private matDialog: MatDialog,
  ) {
  }

  clickedAddTopicDraft(): void {
    const config: any = { width: '600px', data: {} };

    const dialogReference: MatDialogRef<TopicDraftCreationFormComponent> = this.matDialog.open(
      TopicDraftCreationFormComponent,
      config,
    );

    dialogReference
      .afterClosed()
      .pipe(
        take(1),
        filter(v => v),
        switchMap(n => n),
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

  loadAllTopicDrafts(): void {
    this.topicDraftMapper.getAllTopicDrafts$()
      .pipe(take(1))
      .subscribe(topicDrafts => {
        this.rowData.data = topicDrafts;
      });
  }
}
