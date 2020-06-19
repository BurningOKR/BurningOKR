export class ContextRole {
  isAdmin = false;
  isOKRManager = false;
  isOKRMember = false;

  isAtleastAdmin(): boolean {
    return this.isAdmin;
  }

  isAtleastOKRManager(): boolean {
    return this.isAdmin || this.isOKRManager;
  }

  isAtleastOKRMember(): boolean {
    return this.isAdmin || this.isOKRManager || this.isOKRMember;
  }
}
