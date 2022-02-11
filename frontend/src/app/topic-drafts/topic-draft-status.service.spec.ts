import { TestBed } from '@angular/core/testing';

import { TopicDraftStatusService } from './topic-draft-status.service';

describe('TopicDraftStatusService', () => {
  let service: TopicDraftStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TopicDraftStatusService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
