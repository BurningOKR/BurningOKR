import { Injectable } from '@angular/core';
import { CurrentUserService } from '../core/services/current-user.service';
import { OkrTopicDraft } from '../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { User } from '../shared/model/api/user';
import { take } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class TopicDraftPermissionService {

  private isAdmin: boolean;
  private currentUser: User;

  constructor(private currentUserService: CurrentUserService) {
    this.initializeAuthService();
  }

  initializeAuthService(): void {
    this.currentUserService.getCurrentUser$().pipe(take(1))
      .subscribe(user => this.currentUser = user);
    this.currentUserService.isCurrentUserAdmin$().pipe(take(1))
      .subscribe(isAdmin => this.isAdmin = isAdmin);
  }

  hasCurrentUserDeletePermissions(topicDraft: OkrTopicDraft): boolean {
    return this.isAdmin || this.isCurrentUserCreator(topicDraft.initiatorId);
  }

  hasCurrentUserApprovingPermissions(): boolean {
    return this.isAdmin;
  }

  hasCurrentUserSubmissionPermissions(topicDraft: OkrTopicDraft): boolean {
    return this.isCurrentUserCreator(topicDraft.initiatorId);
  }

  hasCurrentUserConvertToTeamPermissions(): boolean {
    return this.isAdmin;
  }

  hasCurrentUserEditingPermissions(topicDraft: OkrTopicDraft): boolean {
    return this.isAdmin || this.isCurrentUserCreator(topicDraft.initiatorId);
  }

  private isCurrentUserCreator(initiatorId: string): boolean {
    return this.currentUser.id === initiatorId;
  }
}
