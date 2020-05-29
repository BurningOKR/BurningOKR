//tslint:disable
import { Observable, of } from 'rxjs';
import { StructureSchema, structureSchemaRole } from '../model/ui/structure-schema';
import { StructureSchemeDto } from '../model/api/structure-scheme-dto';

export class StructureSchemaMapperServiceMock {
  getCurrentDepartmentId$(): Observable<number> {
    return of();
  }

  getCurrentstructureSchema$(): Observable<StructureSchema[]> {
    return of();
  }

  setCurrentstructureSchemaByDepartmentId(departmentId: number): void {
  }

  setCurrentstructureSchemaByCompanyId(companyId: number): void {
  }

  getCurrentstructureSchemaList$(): Observable<StructureSchema[]> {
    return of();
  }

  private isDepartmentInStructure(departmentId: number, structure: StructureSchemeDto[]): boolean {
    if (structure) {
      for (const subStructure of structure) {
        if (subStructure.id === departmentId || this.isDepartmentInStructure(departmentId, subStructure.subDepartments)) {
          return true;
        }
      }

      return false;
    }
  }

  getstructureSchemaListToReachDepartmentWithId$(departmentId: number): Observable<StructureSchema[]> {
    return of();
  }

  getDepartmentIdListToReachDepartmentWithId$(departmentId: number): Observable<number[]> {
    return of();
  }

  private getstructureSchemaListToReachDepartmentWithIdRecursive(
    departmentId: number,
    structure: StructureSchema[],
    structureListToOpen: StructureSchema[]
  ): StructureSchema[] {
    return [];
  }

  updatestructureSchemaTeamRole(departmentId: number, newRole: structureSchemaRole): void {
  }

  updatestructureSchemaTeamRoleRecursive(
    departmentId: number,
    newRole: structureSchemaRole,
    structureSchemaList: StructureSchema[]
  ): void {
  }
}
