import { CompanyId, DepartmentId, UserId } from '../id-types';

export class UserSettings {
  id: number;
  userId: UserId;
  defaultCompanyId: CompanyId;
  defaultTeamId: DepartmentId; // can be wrong

  constructor(id: number, userId: string, defaultCompanyId: CompanyId, defaultTeamId: DepartmentId) {
    this.id = id;
    this.userId = userId;
    this.defaultCompanyId = defaultCompanyId;
    this.defaultTeamId = defaultTeamId;
  }
}
