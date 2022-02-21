import { TopicDraftStatusService } from './topic-draft-status.service';
import { TopicDraftMapper } from '../shared/services/mapper/topic-draft-mapper';
import { OkrTopicDraft } from '../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { status } from '../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { of } from 'rxjs';

describe('TopicDraftStatusService', () => {
  let topicDraftStatusService: TopicDraftStatusService;
  const topicDraftMapperMock: TopicDraftMapper = new TopicDraftMapper(undefined, undefined);
  topicDraftMapperMock.updateTopicDraftStatus$ = jest.fn().mockReturnValue(of(undefined));
  let testTopicDraft: OkrTopicDraft;

  beforeEach(() => {
    topicDraftStatusService = new TopicDraftStatusService(topicDraftMapperMock);
    testTopicDraft = new OkrTopicDraft(0,status.draft, undefined, 0, '', undefined,
      undefined, undefined, undefined, undefined, undefined, undefined,
      undefined, undefined, undefined);
  });

  it('should be created', () => {
    expect(topicDraftStatusService).toBeTruthy();
  });

  it('should change TopicDraft Status', () => {
    expect(testTopicDraft.currentStatus).toBe(status.draft);
    topicDraftStatusService.changeTopicDraftStatusTo(testTopicDraft, status.submitted);
    expect(testTopicDraft.currentStatus).toBe(status.submitted);
    topicDraftStatusService.changeTopicDraftStatusTo(testTopicDraft, status.approved);
    expect(testTopicDraft.currentStatus).toBe(status.approved);
    topicDraftStatusService.changeTopicDraftStatusTo(testTopicDraft, status.rejected);
    expect(testTopicDraft.currentStatus).toBe(status.rejected);
  });

  it('should not change TopicDraft Status when new status is null or undefined', () => {

    expect(testTopicDraft.currentStatus).toBe(status.draft);
    topicDraftStatusService.changeTopicDraftStatusTo(testTopicDraft, undefined);
    expect(testTopicDraft.currentStatus).toBe(status.draft);
    topicDraftStatusService.changeTopicDraftStatusTo(testTopicDraft, null);
    expect(testTopicDraft.currentStatus).toBe(status.draft);
  });
});
