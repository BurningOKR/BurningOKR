import { TopicDraftStatusService } from './topic-draft-status.service';

describe('TopicDraftStatusService', () => {
  let topicDraftStatusService: TopicDraftStatusService;

  beforeEach(() => {
    topicDraftStatusService = new TopicDraftStatusService(undefined);
  });

  it('should be created', () => {
    expect(topicDraftStatusService).toBeTruthy();
  });
});
