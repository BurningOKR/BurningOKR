import { Observable, of } from 'rxjs';
import { StructureSchema, structureSchemaRole } from '../model/ui/structure-schema';
import { StructureSchemeDto } from '../model/api/structure-scheme-dto';

export class CurrentstructureSchemaServiceMock {
  getCurrentDepartmentId$(): Observable<number> {
    return of();
  }

  getCurrentstructureSchema$(): Observable<StructureSchema[]> {
    return of();
  }

  setCurrentstructureSchemaByDepartmentId(departmentId: number): void {
    return ;
  }

  setCurrentstructureSchemaByCompanyId(companyId: number): void {
    return ;
  }

  getCurrentstructureSchemaList$(): Observable<StructureSchema[]> {
    return of();
  }

  private isDepartmentInStructure(departmentId: number, structure: StructureSchemeDto[]): boolean {
    return true;
  }

  getstructureSchemaListToReachDepartmentWithId$(departmentId: number): Observable<StructureSchema[]> {
    return of();
  }

  getDepartmentIdListToReachDepartmentWithId$(departmentId: number): Observable<number[]> {
    return of();
  }

  updatestructureSchemaTeamRole(departmentId: number, newRole: structureSchemaRole): void {
    return;
  }

  updatestructureSchemaTeamRoleRecursive(
    departmentId: number,
    newRole: structureSchemaRole,
    structureSchemaList: StructureSchema[]
  ): void {
    return;
  }
}
