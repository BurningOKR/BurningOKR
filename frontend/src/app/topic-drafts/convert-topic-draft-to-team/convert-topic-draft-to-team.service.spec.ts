import { TestBed } from '@angular/core/testing';

import { ConvertTopicDraftToTeamService } from './convert-topic-draft-to-team.service';

describe('ConvertTopicDraftToTeamService', () => {
  let service: ConvertTopicDraftToTeamService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConvertTopicDraftToTeamService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
