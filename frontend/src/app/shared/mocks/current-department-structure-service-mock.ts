import { Observable, of } from 'rxjs';
import { DepartmentStructure, DepartmentStructureRole } from '../model/ui/department-structure';
import { DepartmentStructureDto } from '../model/api/department-structure.dto';

export class CurrentDepartmentStructureServiceMock {
  getCurrentDepartmentId$(): Observable<number> {
    return of();
  }

  getCurrentDepartmentStructure$(): Observable<DepartmentStructure[]> {
    return of();
  }

  setCurrentDepartmentStructureByDepartmentId(departmentId: number): void {
    return ;
  }

  setCurrentDepartmentStructureByCompanyId(companyId: number): void {
    return ;
  }

  getCurrentDepartmentStructureList$(): Observable<DepartmentStructure[]> {
    return of();
  }

  private isDepartmentInStructure(departmentId: number, structure: DepartmentStructureDto[]): boolean {
    return true;
  }

  getDepartmentStructureListToReachDepartmentWithId$(departmentId: number): Observable<DepartmentStructure[]> {
    return of();
  }

  getDepartmentIdListToReachDepartmentWithId$(departmentId: number): Observable<number[]> {
    return of();
  }

  updateDepartmentStructureTeamRole(departmentId: number, newRole: DepartmentStructureRole): void {
    return;
  }

  updateDepartmentStructureTeamRoleRecursive(
    departmentId: number,
    newRole: DepartmentStructureRole,
    departmentStructureList: DepartmentStructure[]
  ): void {
    return;
  }
}
