import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { CurrentOkrUnitSchemaService } from './current-okr-unit-schema.service';
import { map, switchMap } from 'rxjs/operators';
import { DepartmentNavigationInformation } from '../shared/model/ui/department-navigation-information';

@Injectable({
  providedIn: 'root'
})
export class CurrentNavigationService {

  private currentDepartmentNavigationInformation$ = new BehaviorSubject<DepartmentNavigationInformation>(
    new DepartmentNavigationInformation(-1, [])
  );

  constructor(private okrUnitSchemaService: CurrentOkrUnitSchemaService) {
  }

  getCurrentDepartmentNavigationInformation$(): Observable<DepartmentNavigationInformation> {
    return this.currentDepartmentNavigationInformation$.asObservable();
  }

  refreshDepartmentNavigationInformation(): void {
    this.okrUnitSchemaService.getCurrentUnitId$()
      .pipe(
        switchMap((unitSchemaId: number) => {
            return this.okrUnitSchemaService.getUnitIdsToReachUnitWithId$(unitSchemaId)
              .pipe(
                map((departmentIdList: number[]) => {
                    return new DepartmentNavigationInformation(unitSchemaId, departmentIdList);
                  }
                )
              );
          }
        )
      )
      .subscribe((departmentNavigationInformation: DepartmentNavigationInformation) => {
          this.currentDepartmentNavigationInformation$.next(departmentNavigationInformation);
        }
      );
  }

  clearDepartmentNavigationInformation(): void {
    this.currentDepartmentNavigationInformation$.next(new DepartmentNavigationInformation(-1, []));
  }

}
