import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { OkrTopicDraft } from '../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { TopicDraftMapper } from '../shared/services/mapper/topic-draft-mapper';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-submitted-topic-drafts',
  templateUrl: './submitted-topic-drafts.component.html',
  styleUrls: ['./submitted-topic-drafts.component.css']
})
export class SubmittedTopicDraftsComponent implements OnInit {

  topicDrafts$: Observable<OkrTopicDraft[]>;

  columnsToDisplay = ['topic', 'initiator', 'beginning', 'contributesTo', 'status', 'comments', 'actions'];
  rowData = new MatTableDataSource([] as OkrTopicDraft[]);

  constructor(private router: Router,
              private topicDraftMapper: TopicDraftMapper,
              private i18n: I18n
              ) { }

  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  i18nTopicTableHeader: string = this.i18n({
    id: 'topic_table_header',
    description: 'Submitted topic drafts component "Topic" header',
    value: 'Thema'
  });
  i18nInitiatorTableHeader: string = this.i18n({
    id: 'initiator_table_header',
    description: 'Submitted topic drafts component "Initiator" header',
    value: 'Einreicher'
  });
  i18nBeginningTableHeader: string = this.i18n({
    id: 'beginning_table_header',
    description: 'Submitted topic drafts component "Beginning" header',
    value: 'Beginn'
  });
  i18nContributesToTableHeader: string = this.i18n({
    id: 'component_contributesTo',
    description: 'Submitted topic drafts component "Contributes To" header',
    value: 'Zählt ein auf'
  });
  i18nStatusTableHeader: string = this.i18n({
    id: 'taskState',
    description: 'Submitted topic drafts component "Status" header',
    value: 'Status'
  });
  i18nCommentsTableHeader: string = this.i18n({
    id: 'comments_table_header',
    description: 'Submitted topic drafts component "Comments" header',
    value: 'Kommentare'
  });
  i18nActionsTableHeader: string = this.i18n({
    id: 'actions_table_header',
    description: 'Submitted topic drafts component "Actions" header',
    value: 'Aktionen'
  });

  ngOnInit(): void {
    this.loadAllTopicDrafts();
    this.rowData.sort = this.sort;
    this.rowData.paginator = this.paginator;

    this.topicDrafts$.subscribe(topicDrafts => {
        this.rowData.data = topicDrafts;
    });
  }

  navigateToCompanies(): void {
    this.router.navigate(['companies'])
        .catch();
  }

  loadAllTopicDrafts(): void {
    this.topicDrafts$ = this.topicDraftMapper.getAllTopicDrafts$();
  }
}
