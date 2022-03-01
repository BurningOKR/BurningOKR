import { TopicDraftAuthService } from './topic-draft-auth.service';
import { User } from '../shared/model/api/user';
import { CurrentUserService } from '../core/services/current-user.service';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';

describe('TopicDraftAuthServiceService', () => {

  let topicDraftAuthService: TopicDraftAuthService;
  const currentUserServiceMock: CurrentUserService = new CurrentUserService(undefined, undefined);
  const mockUser: User = new User('mockUserId');
  const mockAdmin: User = new User('mockAdminId');
  const mockAuditor: User = new User('mockAuditorId');

  currentUserServiceMock.getCurrentUser$ = jest.fn().mockReturnValue(of(mockUser));

  currentUserServiceMock.isCurrentUserAdmin$  = jest.fn().mockImplementation(() => {
    return currentUserServiceMock.getCurrentUser$().pipe(map(user => user === mockAdmin));
  });

  currentUserServiceMock.isCurrentUserAuditor$ = jest.fn().mockImplementation(() => {
    return currentUserServiceMock.getCurrentUser$().pipe(map(user => user === mockAuditor));
  });

  beforeEach(() => {
    topicDraftAuthService = new TopicDraftAuthService(currentUserServiceMock);
  });

  it('should get the correct Creator Status', () => {
    expect((topicDraftAuthService as any).isCurrentUserCreator('mockUserId')).toBe(true);
    expect((topicDraftAuthService as any).isCurrentUserCreator('notMockUserId')).toBe(false);
  });

  it('should get the normal user permissions', () => {
    currentUserServiceMock.getCurrentUser$ = jest.fn().mockReturnValue(of(mockUser));
    topicDraftAuthService = new TopicDraftAuthService(currentUserServiceMock);
    expect(topicDraftAuthService).toBeTruthy();
    expect((topicDraftAuthService as any).isAdmin).toBe(false);
    expect((topicDraftAuthService as any).isAuditor).toBe(false);
    expect((topicDraftAuthService as any).currentUser).toEqual(mockUser);
  });

  it('should get the admin user permissions', () => {
    currentUserServiceMock.getCurrentUser$ = jest.fn().mockReturnValue(of(mockAdmin));
    topicDraftAuthService = new TopicDraftAuthService(currentUserServiceMock);
    expect(topicDraftAuthService).toBeTruthy();
    expect((topicDraftAuthService as any).isAdmin).toBe(true);
    expect((topicDraftAuthService as any).isAuditor).toBe(false);
    expect((topicDraftAuthService as any).currentUser).toEqual(mockAdmin);
  });

  it('should get the auditor user permissions', () => {
    currentUserServiceMock.getCurrentUser$ = jest.fn().mockReturnValue(of(mockAuditor));
    topicDraftAuthService = new TopicDraftAuthService(currentUserServiceMock);
    expect(topicDraftAuthService).toBeTruthy();
    expect((topicDraftAuthService as any).isAdmin).toBe(false);
    expect((topicDraftAuthService as any).isAuditor).toBe(true);
    expect((topicDraftAuthService as any).currentUser).toEqual(mockAuditor);
  });
});
