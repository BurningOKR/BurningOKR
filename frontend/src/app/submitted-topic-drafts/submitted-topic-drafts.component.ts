import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { OkrTopicDraft } from '../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { TopicDraftMapper } from '../shared/services/mapper/topic-draft-mapper';

@Component({
  selector: 'app-submitted-topic-drafts',
  templateUrl: './submitted-topic-drafts.component.html',
  styleUrls: ['./submitted-topic-drafts.component.css']
})
export class SubmittedTopicDraftsComponent implements OnInit {

  topicDrafts$: Observable<OkrTopicDraft[]>;

  constructor(private router: Router,
              private topicDraftMapper: TopicDraftMapper
              ) { }

  ngOnInit(): void {
    this.loadAllTopicDrafts();
  }

  navigateToCompanies(): void {
    this.router.navigate(['companies'])
        .catch();
  }

  loadAllTopicDrafts(): void {
    this.topicDrafts$ = this.topicDraftMapper.getAllTopicDrafts$();
  }
}
