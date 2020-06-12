import { OkrUnitId } from '../id-types';

export enum OkrUnitRole {
  USER = 'USER',
  MEMBER = 'MEMBER',
  MANAGER = 'MANAGER'
}

export class OkrUnitSchema {
  id: OkrUnitId;
  name: string;
  subDepartments: OkrUnitSchema[];
  userRole: OkrUnitRole;
  isActive: boolean;

  constructor(id: OkrUnitId, name: string, userRole: OkrUnitRole, isActive: boolean) {
    this.id = id;
    this.name = name;
    this.userRole = userRole;
    this.isActive = isActive;
  }
}
