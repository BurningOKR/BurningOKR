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

  const draftTopicDraft: OkrTopicDraft = getMockTopicDraft(status.draft);
  const submittedTopicDraft: OkrTopicDraft = getMockTopicDraft(status.submitted);
  const approvedTopicDraft: OkrTopicDraft = getMockTopicDraft(status.approved);
  const rejectedTopicDraft: OkrTopicDraft = getMockTopicDraft(status.rejected);

  beforeEach(() => {
    topicDraftStatusService = new TopicDraftStatusService(topicDraftMapperMock);
    testTopicDraft = getMockTopicDraft(status.draft);
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
    topicDraftStatusService.changeTopicDraftStatusTo(testTopicDraft, status.draft);
    expect(testTopicDraft.currentStatus).toBe(status.draft);
  });

  it('should not change TopicDraft Status when new status is null or undefined', () => {
    expect(testTopicDraft.currentStatus).toBe(status.draft);
    topicDraftStatusService.changeTopicDraftStatusTo(testTopicDraft, undefined);
    expect(testTopicDraft.currentStatus).toBe(status.draft);
    topicDraftStatusService.changeTopicDraftStatusTo(testTopicDraft, null);
    expect(testTopicDraft.currentStatus).toBe(status.draft);
  });

  it('should show if the topicDraft can be converted to a team',  () => {
    expect(TopicDraftStatusService.isTopicDraftConvertableToTeam(draftTopicDraft)).toBe(false);
    expect(TopicDraftStatusService.isTopicDraftConvertableToTeam(submittedTopicDraft)).toBe(false);
    expect(TopicDraftStatusService.isTopicDraftConvertableToTeam(approvedTopicDraft)).toBe(true);
    expect(TopicDraftStatusService.isTopicDraftConvertableToTeam(rejectedTopicDraft)).toBe(false);
  });

  it('should show if the topicDraft can be approved or rejected',  () => {
    expect(TopicDraftStatusService.isTopicDraftInApprovingStage(draftTopicDraft)).toBe(false);
    expect(TopicDraftStatusService.isTopicDraftInApprovingStage(submittedTopicDraft)).toBe(true);
    expect(TopicDraftStatusService.isTopicDraftInApprovingStage(approvedTopicDraft)).toBe(true);
    expect(TopicDraftStatusService.isTopicDraftInApprovingStage(rejectedTopicDraft)).toBe(true);
  });

  it('should show if the topicDraft can be edited',  () => {
    expect(TopicDraftStatusService.isTopicDraftInSubmissionStage(draftTopicDraft)).toBe(true);
    expect(TopicDraftStatusService.isTopicDraftInSubmissionStage(submittedTopicDraft)).toBe(true);
    expect(TopicDraftStatusService.isTopicDraftInSubmissionStage(approvedTopicDraft)).toBe(false);
    expect(TopicDraftStatusService.isTopicDraftInSubmissionStage(rejectedTopicDraft)).toBe(false);
  });

  it('should show if the topicDraft is approved or rejected',  () => {
    expect(TopicDraftStatusService.isTopicDraftApprovedOrRejected(draftTopicDraft)).toBe(false);
    expect(TopicDraftStatusService.isTopicDraftApprovedOrRejected(submittedTopicDraft)).toBe(false);
    expect(TopicDraftStatusService.isTopicDraftApprovedOrRejected(approvedTopicDraft)).toBe(true);
    expect(TopicDraftStatusService.isTopicDraftApprovedOrRejected(rejectedTopicDraft)).toBe(true);
  });
});

function getMockTopicDraft(topicDraftStatus: status): OkrTopicDraft {
  return new OkrTopicDraft(0, topicDraftStatus, undefined, 0, '', undefined,
    undefined, undefined, undefined, undefined, undefined, undefined,
    undefined, undefined, undefined);
}
