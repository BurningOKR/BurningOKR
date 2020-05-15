//tslint:disable
import { Observable, of } from 'rxjs';
import { DepartmentStructure, DepartmentStructureRole } from '../model/ui/department-structure';
import { DepartmentStructureDto } from '../model/api/department-structure.dto';

export class DepartmentStructureMapperServiceMock {
  getCurrentDepartmentId$(): Observable<number> {
    return of();
  }

  getCurrentDepartmentStructure$(): Observable<DepartmentStructure[]> {
    return of();
  }

  setCurrentDepartmentStructureByDepartmentId(departmentId: number): void {
  }

  setCurrentDepartmentStructureByCompanyId(companyId: number): void {
  }

  getCurrentDepartmentStructureList$(): Observable<DepartmentStructure[]> {
    return of();
  }

  private isDepartmentInStructure(departmentId: number, structure: DepartmentStructureDto[]): boolean {
    if (structure) {
      for (const subStructure of structure) {
        if (subStructure.id === departmentId || this.isDepartmentInStructure(departmentId, subStructure.subDepartments)) {
          return true;
        }
      }

      return false;
    }
  }

  getDepartmentStructureListToReachDepartmentWithId$(departmentId: number): Observable<DepartmentStructure[]> {
    return of();
  }

  getDepartmentIdListToReachDepartmentWithId$(departmentId: number): Observable<number[]> {
    return of();
  }

  private getDepartmentStructureListToReachDepartmentWithIdRecursive(
    departmentId: number,
    structure: DepartmentStructure[],
    structureListToOpen: DepartmentStructure[]
  ): DepartmentStructure[] {
    return [];
  }

  updateDepartmentStructureTeamRole(departmentId: number, newRole: DepartmentStructureRole): void {
  }

  updateDepartmentStructureTeamRoleRecursive(
    departmentId: number,
    newRole: DepartmentStructureRole,
    departmentStructureList: DepartmentStructure[]
  ): void {
  }
}
