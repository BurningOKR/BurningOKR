import { TopicDraftAuthService } from './topic-draft-auth.service';
import { User } from '../shared/model/api/user';
import { CurrentUserService } from '../core/services/current-user.service';
import { of } from 'rxjs';

describe('TopicDraftAuthServiceService', () => {

  let topicDraftAuthService: TopicDraftAuthService;
  const currentUserServiceMock: CurrentUserService = new CurrentUserService(undefined, undefined);
  const mockUser: User = new User('mockId');

  currentUserServiceMock.isCurrentUserAdmin$ = jest.fn().mockReturnValue(of(true));
  currentUserServiceMock.isCurrentUserAuditor$ = jest.fn().mockReturnValue(of(true));
  currentUserServiceMock.getCurrentUser$ = jest.fn().mockReturnValue(of(mockUser));

  beforeEach(() => {
    topicDraftAuthService = new TopicDraftAuthService(currentUserServiceMock);
  });

  it('should get the correct Creator Status', () => {
    expect((topicDraftAuthService as any).isCurrentUserCreator('mockId')).toBeTruthy();
    expect((topicDraftAuthService as any).isCurrentUserCreator('notMockId')).toBeFalsy();
  });

  it('should be created', () => {
    expect(topicDraftAuthService).toBeTruthy();
    expect((topicDraftAuthService as any).isAdmin).toBeTruthy();
    expect((topicDraftAuthService as any).isAuditor).toBeTruthy();
    expect((topicDraftAuthService as any).currentUser).toEqual(mockUser);

    expect(topicDraftAuthService).toBeTruthy();
    expect((topicDraftAuthService as any).isAdmin).toBeTruthy();
    expect((topicDraftAuthService as any).isAuditor).toBeTruthy();
    expect((topicDraftAuthService as any).currentUser).toEqual(mockUser);
  });
});
