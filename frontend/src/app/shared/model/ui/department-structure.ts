import { DepartmentId } from '../id-types';

export enum DepartmentStructureRole {
  USER = 'USER',
  MEMBER = 'MEMBER',
  MANAGER = 'MANAGER'
}

export class DepartmentStructure {
  id: DepartmentId;
  name: string;
  subDepartments: DepartmentStructure[];
  userRole: DepartmentStructureRole;
  isActive: boolean;

  constructor(id: DepartmentId, name: string, userRole: DepartmentStructureRole, isActive: boolean) {
    this.id = id;
    this.name = name;
    this.userRole = userRole;
    this.isActive = isActive;
  }
}
