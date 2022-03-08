import { TopicDraftPermissionService } from './topic-draft-permission.service';
import { User } from '../shared/model/api/user';
import { CurrentUserService } from '../core/services/current-user.service';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { OkrTopicDraft } from '../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';

describe('TopicDraftPermissionServiceService', () => {

  let topicDraftPermissionService: TopicDraftPermissionService;
  const currentUserServiceMock: CurrentUserService = new CurrentUserService(undefined, undefined);
  const mockUser: User = new User('mockUserId');
  const mockAdmin: User = new User('mockAdminId');
  const mockAuditor: User = new User('mockAuditorId');

  currentUserServiceMock.isCurrentUserAdmin$  = jest.fn().mockImplementation(() => {
    return currentUserServiceMock.getCurrentUser$().pipe(map(user => user === mockAdmin));
  });

  currentUserServiceMock.isCurrentUserAuditor$ = jest.fn().mockImplementation(() => {
    return currentUserServiceMock.getCurrentUser$().pipe(map(user => user === mockAuditor));
  });

  beforeEach(() => {
    setCurrentUserInPermissionService(mockUser);
  });

  it('should get the correct Creator Status', () => {
    expect((topicDraftPermissionService as any).isCurrentUserCreator('mockUserId')).toBe(true);
    expect((topicDraftPermissionService as any).isCurrentUserCreator('notMockUserId')).toBe(false);
  });

  it('should get the normal user permissions', () => {
    expect(topicDraftPermissionService).toBeTruthy();
    expect((topicDraftPermissionService as any).isAdmin).toBe(false);
    expect((topicDraftPermissionService as any).isAuditor).toBe(false);
    expect((topicDraftPermissionService as any).currentUser).toEqual(mockUser);
  });

  it('should get the admin user permissions', () => {
    setCurrentUserInPermissionService(mockAdmin);
    expect(topicDraftPermissionService).toBeTruthy();
    expect((topicDraftPermissionService as any).isAdmin).toBe(true);
    expect((topicDraftPermissionService as any).isAuditor).toBe(false);
    expect((topicDraftPermissionService as any).currentUser).toEqual(mockAdmin);
  });

  it('should get the auditor user permissions', () => {
    setCurrentUserInPermissionService(mockAuditor);
    expect(topicDraftPermissionService).toBeTruthy();
    expect((topicDraftPermissionService as any).isAdmin).toBe(false);
    expect((topicDraftPermissionService as any).isAuditor).toBe(true);
    expect((topicDraftPermissionService as any).currentUser).toEqual(mockAuditor);
  });

  it('should get if the User is allowed to delete', () => {
    expect(topicDraftPermissionService.hasCurrentUserDeletePermissions(getMockTopicDraft(mockUser))).toBe(true);
    expect(topicDraftPermissionService.hasCurrentUserDeletePermissions(getMockTopicDraft(mockAuditor))).toBe(false);
    expect(topicDraftPermissionService.hasCurrentUserDeletePermissions(getMockTopicDraft(mockAdmin))).toBe(false);
  });

  it('should get if the Auditor is allowed to delete', () => {
    setCurrentUserInPermissionService(mockAuditor);
    expect(topicDraftPermissionService.hasCurrentUserDeletePermissions(getMockTopicDraft(mockUser))).toBe(false);
    expect(topicDraftPermissionService.hasCurrentUserDeletePermissions(getMockTopicDraft(mockAuditor))).toBe(true);
    expect(topicDraftPermissionService.hasCurrentUserDeletePermissions(getMockTopicDraft(mockAdmin))).toBe(false);
  });

  it('should get if the Admin is allowed to delete', () => {
    setCurrentUserInPermissionService(mockAdmin);
    expect(topicDraftPermissionService.hasCurrentUserDeletePermissions(getMockTopicDraft(mockUser))).toBe(true);
    expect(topicDraftPermissionService.hasCurrentUserDeletePermissions(getMockTopicDraft(mockAuditor))).toBe(true);
    expect(topicDraftPermissionService.hasCurrentUserDeletePermissions(getMockTopicDraft(mockAdmin))).toBe(true);
  });

  it('should get if the CurrentUser can approve', () => {
    expect(topicDraftPermissionService.hasCurrentUserApprovingPermissions()).toBe(false);
    setCurrentUserInPermissionService(mockAuditor);
    expect(topicDraftPermissionService.hasCurrentUserApprovingPermissions()).toBe(true);
    setCurrentUserInPermissionService(mockAdmin);
    expect(topicDraftPermissionService.hasCurrentUserApprovingPermissions()).toBe(true);
  });

  it('should get if the CurrentUser can change the submission status', () => {
    expect(topicDraftPermissionService.hasCurrentUserSubmissionPermissions(getMockTopicDraft(mockUser))).toBe(true);
    expect(topicDraftPermissionService.hasCurrentUserSubmissionPermissions(getMockTopicDraft(mockAuditor))).toBe(false);
    expect(topicDraftPermissionService.hasCurrentUserSubmissionPermissions(getMockTopicDraft(mockAdmin))).toBe(false);
    setCurrentUserInPermissionService(mockAuditor);
    expect(topicDraftPermissionService.hasCurrentUserSubmissionPermissions(getMockTopicDraft(mockUser))).toBe(false);
    expect(topicDraftPermissionService.hasCurrentUserSubmissionPermissions(getMockTopicDraft(mockAuditor))).toBe(true);
    expect(topicDraftPermissionService.hasCurrentUserSubmissionPermissions(getMockTopicDraft(mockAdmin))).toBe(false);
    setCurrentUserInPermissionService(mockAdmin);
    expect(topicDraftPermissionService.hasCurrentUserSubmissionPermissions(getMockTopicDraft(mockUser))).toBe(false);
    expect(topicDraftPermissionService.hasCurrentUserSubmissionPermissions(getMockTopicDraft(mockAuditor))).toBe(false);
    expect(topicDraftPermissionService.hasCurrentUserSubmissionPermissions(getMockTopicDraft(mockAdmin))).toBe(true);
  });

  it('should get if the CurrentUser can convert to a team', () => {
    expect(topicDraftPermissionService.hasCurrentUserConvertToTeamPermissions()).toBe(false);
    setCurrentUserInPermissionService(mockAuditor);
    expect(topicDraftPermissionService.hasCurrentUserConvertToTeamPermissions()).toBe(false);
    setCurrentUserInPermissionService(mockAdmin);
    expect(topicDraftPermissionService.hasCurrentUserConvertToTeamPermissions()).toBe(true);
  });

  it('should get if the CurrentUser can edit', () => {
    expect(topicDraftPermissionService.hasCurrentUserEditingPermissions(getMockTopicDraft(mockUser))).toBe(true);
    expect(topicDraftPermissionService.hasCurrentUserEditingPermissions(getMockTopicDraft(mockAuditor))).toBe(false);
    expect(topicDraftPermissionService.hasCurrentUserEditingPermissions(getMockTopicDraft(mockAdmin))).toBe(false);
    setCurrentUserInPermissionService(mockAuditor);
    expect(topicDraftPermissionService.hasCurrentUserEditingPermissions(getMockTopicDraft(mockUser))).toBe(false);
    expect(topicDraftPermissionService.hasCurrentUserEditingPermissions(getMockTopicDraft(mockAuditor))).toBe(true);
    expect(topicDraftPermissionService.hasCurrentUserEditingPermissions(getMockTopicDraft(mockAdmin))).toBe(false);
    setCurrentUserInPermissionService(mockAdmin);
    expect(topicDraftPermissionService.hasCurrentUserEditingPermissions(getMockTopicDraft(mockUser))).toBe(true);
    expect(topicDraftPermissionService.hasCurrentUserEditingPermissions(getMockTopicDraft(mockAuditor))).toBe(true);
    expect(topicDraftPermissionService.hasCurrentUserEditingPermissions(getMockTopicDraft(mockAdmin))).toBe(true);
  });

  function setCurrentUserInPermissionService(user: User): void {
    currentUserServiceMock.getCurrentUser$ = jest.fn().mockReturnValue(of(user));
    topicDraftPermissionService = new TopicDraftPermissionService(currentUserServiceMock);
  }
});

function getMockTopicDraft(user: User): OkrTopicDraft {
  return new OkrTopicDraft(0, undefined, user, 0, '', user.id,
    undefined, undefined, undefined, undefined, undefined, undefined,
    undefined, undefined, undefined);
}
