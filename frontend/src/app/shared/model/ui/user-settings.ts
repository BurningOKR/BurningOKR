import { CompanyId } from '../api/company.dto';
import { UserId } from '../api/user';
import { DepartmentId } from '../api/department.dto';

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
