import { TestBed } from '@angular/core/testing';

import { TopicDraftAuthService } from './topic-draft-auth.service';

describe('TopicDraftAuthServiceService', () => {
  let service: TopicDraftAuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TopicDraftAuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
