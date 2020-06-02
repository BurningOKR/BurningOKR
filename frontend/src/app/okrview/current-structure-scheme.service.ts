import { Injectable } from '@angular/core';
import { StructureSchema, structureSchemaRole } from '../shared/model/ui/structure-schema';
import { Observable, ReplaySubject } from 'rxjs';
import { StructureSchemeDto } from '../shared/model/api/structure/structure-scheme-dto';
import { map, take } from 'rxjs/operators';
import { StructureSchemaMapper } from '../shared/services/mapper/structure-schema-mapper.service';

@Injectable({
  providedIn: 'root'
})
export class CurrentStructureSchemeService {

  private currentStructureSchema$: ReplaySubject<StructureSchema[]> = new ReplaySubject<StructureSchema[]>(1);
  private currentStructureId$: ReplaySubject<number> = new ReplaySubject<number>(1);

  constructor(private structureSchemaMapperService: StructureSchemaMapper) {
  }

  getCurrentStructureId$(): Observable<number> {
    return this.currentStructureId$.asObservable();
  }

  setCurrentStructureSchemaByStructureId(departmentId: number): void {
    this.currentStructureId$.next(departmentId);
    this.structureSchemaMapperService
      .getStructureSchemaOfDepartment$(departmentId)
      .pipe(take(1))
      .subscribe(structureSchema => {
        this.currentStructureSchema$.next(structureSchema);
      });
  }

  setCurrentStructureSchemaByCompanyId(companyId: number): void {
    this.structureSchemaMapperService
      .getStructureSchemaOfCompany$(companyId)
      .pipe(take(1))
      .subscribe(structureSchema => {
        this.currentStructureSchema$.next(structureSchema);
      });
  }

  getCurrentStructureSchemas$(): Observable<StructureSchema[]> {
    return this.currentStructureSchema$;
  }

  private isDepartmentInStructureSchema(departmentId: number, structure: StructureSchemeDto[]): boolean {
    if (structure) {
      for (const subStructure of structure) {
        if (subStructure.id === departmentId || this.isDepartmentInStructureSchema(departmentId, subStructure.subDepartments)) {
          return true;
        }
      }

      return false;
    }
  }

  getStructureSchemasToReachStructureWithId$(departmentId: number): Observable<StructureSchema[]> {
    const structureSchemas: StructureSchema[] = [];

    return this.currentStructureSchema$
      .pipe(
        map((structureSchema: StructureSchema[]) => {
            return this.getStructureSchemasToReachStructureWithIdRecursive(
              departmentId,
              structureSchema,
              structureSchemas
            );
          }
        )
      );
  }

  getStructureIdsToReachStructureWithId$(departmentId: number): Observable<number[]> {
    return this.getStructureSchemasToReachStructureWithId$(departmentId)
      .pipe(
        map((structureSchemas: StructureSchema[]) => {
            return structureSchemas.map((structureSchema: StructureSchema) => {
                return structureSchema.id;
              }
            );
          }
        )
      );
  }

  private getStructureSchemasToReachStructureWithIdRecursive(
    departmentId: number,
    structure: StructureSchema[],
    structureListToOpen: StructureSchema[]
  ): StructureSchema[] {
    if (structure) {
      for (const subDepartment of structure) {
        if (this.isDepartmentInStructureSchema(departmentId, subDepartment.subDepartments)) {
          structureListToOpen.push(subDepartment);
          this.getStructureSchemasToReachStructureWithIdRecursive(
            departmentId,
            subDepartment.subDepartments,
            structureListToOpen
          );
        }
      }
    }

    return structureListToOpen;
  }

  updateStructureSchemaTeamRole(departmentId: number, newRole: structureSchemaRole): void {
    this.currentStructureSchema$.pipe(
      take(1),
      map((currentStructureSchemas: StructureSchema[]) => {
        this.updateStructureSchemaTeamRoleRecursive(departmentId, newRole, currentStructureSchemas);

        return currentStructureSchemas;
      })
    )
      .subscribe((updatedStructureSchemas: StructureSchema[]) => {
        this.currentStructureSchema$.next(updatedStructureSchemas);
      });
  }

  updateStructureSchemaTeamRoleRecursive(
    departmentId: number,
    newRole: structureSchemaRole,
    structureSchemas: StructureSchema[]
  ): void {
    structureSchemas.forEach(structure => {
      if (structure.id === departmentId) {
        structure.userRole = newRole;
      } else {
        this.updateStructureSchemaTeamRoleRecursive(departmentId, newRole, structure.subDepartments);
      }
    });
  }
}
