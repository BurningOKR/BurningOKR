import { CompanyId, StructureId, UserId } from '../id-types';

export class UserSettings {
  id: number;
  userId: UserId;
  defaultCompanyId: CompanyId;
  defaultTeamId: StructureId; // can be wrong

  constructor(id: number, userId: string, defaultCompanyId: CompanyId, defaultTeamId: StructureId) {
    this.id = id;
    this.userId = userId;
    this.defaultCompanyId = defaultCompanyId;
    this.defaultTeamId = defaultTeamId;
  }
}
