import { Injectable } from '@angular/core';
import { CurrentUserService } from '../core/services/current-user.service';
import { OkrTopicDraft } from '../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { User } from '../shared/model/api/user';
import { take } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TopicDraftPermissionService {

  private isAdmin: boolean;
  private isAuditor: boolean;
  private currentUser: User;
  constructor(private currentUserService: CurrentUserService) {
    this.initializeAuthService();
  }

  initializeAuthService(): void {
    this.currentUserService.getCurrentUser$().pipe(take(1))
      .subscribe(user => this.currentUser = user);
    this.currentUserService.isCurrentUserAdmin$().pipe(take(1))
      .subscribe(isAdmin => this.isAdmin = isAdmin);
    this.currentUserService.isCurrentUserAuditor$().pipe(take(1))
      .subscribe(isAuditor => this.isAuditor = isAuditor);
  }

  hasCurrentUserDeletePermissions(topicDraft: OkrTopicDraft): boolean {
    return this.isCurrentUserAdminOrCreator(topicDraft.initiatorId);
  }

  hasCurrentUserApprovingPermissions(): boolean {
    return this.isCurrentUserAdminOrAuditor();
  }

  hasCurrentUserSubmissionPermissions(topicDraft: OkrTopicDraft): boolean {
    return this.isCurrentUserCreator(topicDraft.initiatorId);
  }

  hasCurrentUserConvertToTeamPermissions(): boolean {
    return this.isAdmin;
  }

  hasCurrentUserEditingPermissions(topicDraft: OkrTopicDraft): boolean {
    return this.isCurrentUserAdminOrCreator(topicDraft.initiatorId);
  }

  private isCurrentUserAdminOrCreator(initiatorId: string): boolean {
    return this.isAdmin || this.isCurrentUserCreator(initiatorId);
  }

  private isCurrentUserAdminOrAuditor(): boolean {
    return this.isAdmin || this.isAuditor;
  }

  private isCurrentUserCreator(initiatorId: string): boolean {
    return this.currentUser.id === initiatorId;
  }
}
