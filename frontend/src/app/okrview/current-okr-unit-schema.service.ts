import { Injectable } from '@angular/core';
import { OkrUnitSchema, OkrUnitRole } from '../shared/model/ui/okr-unit-schema';
import { Observable, ReplaySubject } from 'rxjs';
import { OkrUnitSchemaDto } from '../shared/model/api/OkrUnit/okr-unit-schema-dto';
import { map, take } from 'rxjs/operators';
import { OkrUnitSchemaMapper } from '../shared/services/mapper/okr-unit-schema.mapper';

@Injectable({
  providedIn: 'root'
})
export class CurrentOkrUnitSchemaService {

  private currentUnitSchema$: ReplaySubject<OkrUnitSchema[]> = new ReplaySubject<OkrUnitSchema[]>(1);
  private currentUnitId$: ReplaySubject<number> = new ReplaySubject<number>(1);

  constructor(private okrUnitSchemaMapper: OkrUnitSchemaMapper) {
  }

  getCurrentUnitId$(): Observable<number> {
    return this.currentUnitId$.asObservable();
  }

  setCurrentUnitSchemaByDepartmentId(departmentId: number): void {
    this.currentUnitId$.next(departmentId);
    this.okrUnitSchemaMapper
      .getOkrUnitSchemaByUnitId$(departmentId)
      .pipe(take(1))
      .subscribe(unitSchema => {
        this.currentUnitSchema$.next(unitSchema);
      });
  }

  setCurrentUnitSchemaByCompanyId(companyId: number): void {
    this.okrUnitSchemaMapper
      .getOkrUnitSchemaOfCompany$(companyId)
      .pipe(take(1))
      .subscribe(unitSchema => {
        this.currentUnitSchema$.next(unitSchema);
      });
  }

  getCurrentUnitSchemas$(): Observable<OkrUnitSchema[]> {
    return this.currentUnitSchema$;
  }

  private isDepartmentInUnitSchema(departmentId: number, unitSchemaDtos: OkrUnitSchemaDto[]): boolean {
    if (unitSchemaDtos) {
      for (const schemaDto of unitSchemaDtos) {
        if (schemaDto.id === departmentId || this.isDepartmentInUnitSchema(departmentId, schemaDto.subDepartments)) {
          return true;
        }
      }

      return false;
    }
  }

  getUnitSchemasToReachUnitWithId$(departmentId: number): Observable<OkrUnitSchema[]> {
    const okrUnitSchemas: OkrUnitSchema[] = [];

    return this.currentUnitSchema$
      .pipe(
        map((unitSchemas: OkrUnitSchema[]) => {
            return this.getUnitSchemasToReachUnitWithIdRecursive(
              departmentId,
              unitSchemas,
              okrUnitSchemas
            );
          }
        )
      );
  }

  getUnitIdsToReachUnitWithId$(departmentId: number): Observable<number[]> {
    return this.getUnitSchemasToReachUnitWithId$(departmentId)
      .pipe(
        map((okrUnitSchemas: OkrUnitSchema[]) => {
            return okrUnitSchemas.map((okrUnitSchema: OkrUnitSchema) => {
                return okrUnitSchema.id;
              }
            );
          }
        )
      );
  }

  private getUnitSchemasToReachUnitWithIdRecursive(
    departmentId: number,
    okrUnitSchemas: OkrUnitSchema[],
    unitSchemas: OkrUnitSchema[]
  ): OkrUnitSchema[] {
    if (okrUnitSchemas) {
      for (const subDepartment of okrUnitSchemas) {
        if (this.isDepartmentInUnitSchema(departmentId, subDepartment.subDepartments)) {
          unitSchemas.push(subDepartment);
          this.getUnitSchemasToReachUnitWithIdRecursive(
            departmentId,
            subDepartment.subDepartments,
            unitSchemas
          );
        }
      }
    }

    return unitSchemas;
  }

  updateSchemaTeamRole(departmentId: number, newRole: OkrUnitRole): void {
    this.currentUnitSchema$.pipe(
      take(1),
      map((okrUnitSchemas: OkrUnitSchema[]) => {
        this.updateUnitSchemaTeamRoleRecursive(departmentId, newRole, okrUnitSchemas);

        return okrUnitSchemas;
      })
    )
      .subscribe((okrUnitSchemas: OkrUnitSchema[]) => {
        this.currentUnitSchema$.next(okrUnitSchemas);
      });
  }

  updateUnitSchemaTeamRoleRecursive(
    departmentId: number,
    newRole: OkrUnitRole,
    okrUnitSchemas: OkrUnitSchema[]
  ): void {
    okrUnitSchemas.forEach(schema => {
      if (schema.id === departmentId) {
        schema.userRole = newRole;
      } else {
        this.updateUnitSchemaTeamRoleRecursive(departmentId, newRole, schema.subDepartments);
      }
    });
  }
}
