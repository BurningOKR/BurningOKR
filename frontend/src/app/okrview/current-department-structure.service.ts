import { Injectable } from '@angular/core';
import { DepartmentStructure, DepartmentStructureRole } from '../shared/model/ui/department-structure';
import { Observable, ReplaySubject } from 'rxjs';
import { DepartmentStructureDto } from '../shared/model/api/department-structure.dto';
import { map, take } from 'rxjs/operators';
import { DepartmentStructureMapper } from '../shared/services/mapper/department-structure.mapper';

@Injectable({
  providedIn: 'root'
})
export class CurrentDepartmentStructureService {

  private currentDepartmentStructure$: ReplaySubject<DepartmentStructure[]> = new ReplaySubject<DepartmentStructure[]>(1);
  private currentDepartmentId$: ReplaySubject<number> = new ReplaySubject<number>(1);

  constructor(private departmentStructureMapperService: DepartmentStructureMapper) {
  }

  getCurrentDepartmentId$(): Observable<number> {
    return this.currentDepartmentId$.asObservable();
  }

  getCurrentDepartmentStructure$(): Observable<DepartmentStructure[]> {
    return this.currentDepartmentStructure$.asObservable();
  }

  setCurrentDepartmentStructureByDepartmentId(departmentId: number): void {
    this.currentDepartmentId$.next(departmentId);
    this.departmentStructureMapperService
      .getDepartmentStructureOfDepartment$(departmentId)
      .pipe(take(1))
      .subscribe(departmentStructure => {
        this.currentDepartmentStructure$.next(departmentStructure);
      });
  }

  setCurrentDepartmentStructureByCompanyId(companyId: number): void {
    this.departmentStructureMapperService
      .getDepartmentStructureOfCompany$(companyId)
      .pipe(take(1))
      .subscribe(departmentStructure => {
        this.currentDepartmentStructure$.next(departmentStructure);
      });
  }

  getCurrentDepartmentStructureList$(): Observable<DepartmentStructure[]> {
    return this.currentDepartmentStructure$;
  }

  // TODO: refactor, so that there are at minimum 2 returns
  private isDepartmentInStructure(departmentId: number, structure: DepartmentStructureDto[]): boolean {
    if (structure) {
      for (const subStructure of structure) {
        if (subStructure.id === departmentId) {
          return true;
        } else if (this.isDepartmentInStructure(departmentId, subStructure.subDepartments)) {
          return true;
        }
      }

      return false;
    }
  }

  getDepartmentStructureListToReachDepartmentWithId$(departmentId: number): Observable<DepartmentStructure[]> {
    const departmentStructureList: DepartmentStructure[] = [];

    return this.currentDepartmentStructure$
      .pipe(
        map((departmentStructure: DepartmentStructure[]) => {
            return this.getDepartmentStructureListToReachDepartmentWithIdRecursive(
              departmentId,
              departmentStructure,
              departmentStructureList
            );
          }
        )
      );
  }

  getDepartmentIdListToReachDepartmentWithId$(departmentId: number): Observable<number[]> {
    return this.getDepartmentStructureListToReachDepartmentWithId$(departmentId)
      .pipe(
        map((departmentStructureArray: DepartmentStructure[]) => {
            return departmentStructureArray.map((departmentStructure: DepartmentStructure) => {
                return departmentStructure.id;
              }
            );
          }
        )
      );
  }

  private getDepartmentStructureListToReachDepartmentWithIdRecursive(
    departmentId: number,
    structure: DepartmentStructure[],
    structureListToOpen: DepartmentStructure[]
  ): DepartmentStructure[] {
    if (structure) {
      for (const subDepartment of structure) {
        if (this.isDepartmentInStructure(departmentId, subDepartment.subDepartments)) {
          structureListToOpen.push(subDepartment);
          this.getDepartmentStructureListToReachDepartmentWithIdRecursive(
            departmentId,
            subDepartment.subDepartments,
            structureListToOpen
          );
        }
      }
    }

    return structureListToOpen;
  }

  updateDepartmentStructureTeamRole(departmentId: number, newRole: DepartmentStructureRole): void {
    this.currentDepartmentStructure$.pipe(
      take(1),
      map((currentDepartmentStructure: DepartmentStructure[]) => {
        this.updateDepartmentStructureTeamRoleRecursive(departmentId, newRole, currentDepartmentStructure);

        return currentDepartmentStructure;
      })
    )
      .subscribe((updatedDepartmentStructure: DepartmentStructure[]) => {
        this.currentDepartmentStructure$.next(updatedDepartmentStructure);
      });
  }

  updateDepartmentStructureTeamRoleRecursive(
    departmentId: number,
    newRole: DepartmentStructureRole,
    departmentStructureList: DepartmentStructure[]
  ): void {
    departmentStructureList.forEach(structure => {
      if (structure.id === departmentId) {
        structure.userRole = newRole;
      } else {
        this.updateDepartmentStructureTeamRoleRecursive(departmentId, newRole, structure.subDepartments);
      }
    });
  }
}
