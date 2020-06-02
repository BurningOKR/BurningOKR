import { StructureId } from '../id-types';

export enum structureSchemaRole {
  USER = 'USER',
  MEMBER = 'MEMBER',
  MANAGER = 'MANAGER'
}

export class StructureSchema {
  id: StructureId;
  name: string;
  subDepartments: StructureSchema[];
  userRole: structureSchemaRole;
  isActive: boolean;

  constructor(id: StructureId, name: string, userRole: structureSchemaRole, isActive: boolean) {
    this.id = id;
    this.name = name;
    this.userRole = userRole;
    this.isActive = isActive;
  }
}
