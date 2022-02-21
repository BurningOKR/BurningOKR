import { TopicDraftAuthService } from './topic-draft-auth.service';
import { User } from '../shared/model/api/user';
import { CurrentUserService } from '../core/services/current-user.service';
import { of } from 'rxjs';

describe('TopicDraftAuthServiceService', () => {
  let topicDraftAuthService: TopicDraftAuthService;
  const currentUserServiceMock: CurrentUserService = new CurrentUserService(undefined, undefined);
  const mockUser: User = new User();

  currentUserServiceMock.isCurrentUserAdmin$ = jest.fn(() => of(true));
  currentUserServiceMock.isCurrentUserAuditor$ = jest.fn(() => of(true));
  currentUserServiceMock.getCurrentUser$ = jest.fn(() => of(mockUser));

  beforeEach(() => {
    topicDraftAuthService = new TopicDraftAuthService(currentUserServiceMock);
  });

  it('should be created', () => {
    expect(topicDraftAuthService).toBeTruthy();
  });
});
