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
    expect((topicDraftAuthService as any).isCurrentUserCreator('mockUserId')).toBeTruthy();
    expect((topicDraftAuthService as any).isCurrentUserCreator('notMockUserId')).toBeFalsy();
  });

  it('should get the correct permissions upon creation', () => {
    currentUserServiceMock.getCurrentUser$ = jest.fn().mockReturnValue(of(mockUser));
    topicDraftAuthService = new TopicDraftAuthService(currentUserServiceMock);
    expect(topicDraftAuthService).toBeTruthy();
    expect((topicDraftAuthService as any).isAdmin).toBeFalsy();
    expect((topicDraftAuthService as any).isAuditor).toBeFalsy();
    expect((topicDraftAuthService as any).currentUser).toEqual(mockUser);

    currentUserServiceMock.getCurrentUser$ = jest.fn().mockReturnValue(of(mockAuditor));
    topicDraftAuthService = new TopicDraftAuthService(currentUserServiceMock);
    expect(topicDraftAuthService).toBeTruthy();
    expect((topicDraftAuthService as any).isAdmin).toBeFalsy();
    expect((topicDraftAuthService as any).isAuditor).toBeTruthy();
    expect((topicDraftAuthService as any).currentUser).toEqual(mockAuditor);

    currentUserServiceMock.getCurrentUser$ = jest.fn().mockReturnValue(of(mockAdmin));
    topicDraftAuthService = new TopicDraftAuthService(currentUserServiceMock);
    expect(topicDraftAuthService).toBeTruthy();
    expect((topicDraftAuthService as any).isAdmin).toBeTruthy();
    expect((topicDraftAuthService as any).isAuditor).toBeFalsy();
    expect((topicDraftAuthService as any).currentUser).toEqual(mockAdmin);
  });
});
