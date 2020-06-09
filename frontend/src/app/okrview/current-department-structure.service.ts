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

  private currentDepartmentStructures$: ReplaySubject<DepartmentStructure[]> = new ReplaySubject<DepartmentStructure[]>(1);
  private currentDepartmentId$: ReplaySubject<number> = new ReplaySubject<number>(1);

  constructor(private departmentStructureMapperService: DepartmentStructureMapper) {
  }

  getCurrentDepartmentId$(): Observable<number> {
    return this.currentDepartmentId$.asObservable();
  }

  setCurrentDepartmentStructuresByDepartmentId(departmentId: number): void {
    this.currentDepartmentId$.next(departmentId);
    this.departmentStructureMapperService
      .getDepartmentStructuresOfDepartment$(departmentId)
      .pipe(take(1))
      .subscribe(departmentStructures => {
        this.currentDepartmentStructures$.next(departmentStructures);
      });
  }

  setCurrentDepartmentStructuresByCompanyId(companyId: number): void {
    this.departmentStructureMapperService
      .getDepartmentStructuresOfCompany$(companyId)
      .pipe(take(1))
      .subscribe(departmentStructures => {
        this.currentDepartmentStructures$.next(departmentStructures);
      });
  }

  getCurrentDepartmentStructures$(): Observable<DepartmentStructure[]> {
    return this.currentDepartmentStructures$.asObservable();
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

  getDepartmentStructuresToReachDepartmentWithId$(departmentId: number): Observable<DepartmentStructure[]> {
    const departmentStructures: DepartmentStructure[] = [];

    return this.currentDepartmentStructures$
      .pipe(
        map((currentDepartmentStructures: DepartmentStructure[]) => {
            return this.getDepartmentStructuresToReachDepartmentWithIdRecursive(
              departmentId,
              currentDepartmentStructures,
              departmentStructures
            );
          }
        )
      );
  }

  getDepartmentIdListToReachDepartmentWithId$(departmentId: number): Observable<number[]> {
    return this.getDepartmentStructuresToReachDepartmentWithId$(departmentId)
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

  private getDepartmentStructuresToReachDepartmentWithIdRecursive(
    departmentId: number,
    structure: DepartmentStructure[],
    structuresToOpen: DepartmentStructure[]
  ): DepartmentStructure[] {
    if (structure) {
      for (const subDepartment of structure) {
        if (this.isDepartmentInStructure(departmentId, subDepartment.subDepartments)) {
          structuresToOpen.push(subDepartment);
          this.getDepartmentStructuresToReachDepartmentWithIdRecursive(
            departmentId,
            subDepartment.subDepartments,
            structuresToOpen
          );
        }
      }
    }

    return structuresToOpen;
  }

  updateDepartmentStructuresTeamRole(departmentId: number, newRole: DepartmentStructureRole): void {
    this.currentDepartmentStructures$.pipe(
      take(1),
      map((currentDepartmentStructures: DepartmentStructure[]) => {
        this.updateDepartmentStructuresTeamRoleRecursive(departmentId, newRole, currentDepartmentStructures);

        return currentDepartmentStructures;
      })
    )
      .subscribe((updatedDepartmentStructures: DepartmentStructure[]) => {
        this.currentDepartmentStructures$.next(updatedDepartmentStructures);
      });
  }

  updateDepartmentStructuresTeamRoleRecursive(
    departmentId: number,
    newRole: DepartmentStructureRole,
    departmentStructures: DepartmentStructure[]
  ): void {
    departmentStructures.forEach(structure => {
      if (structure.id === departmentId) {
        structure.userRole = newRole;
      } else {
        this.updateDepartmentStructuresTeamRoleRecursive(departmentId, newRole, structure.subDepartments);
      }
    });
  }
}
