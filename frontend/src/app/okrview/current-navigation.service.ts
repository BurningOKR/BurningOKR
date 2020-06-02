import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { CurrentStructureSchemeService } from './current-structure-scheme.service';
import { map, switchMap } from 'rxjs/operators';
import { DepartmentNavigationInformation } from '../shared/model/ui/department-navigation-information';

@Injectable({
  providedIn: 'root'
})
export class CurrentNavigationService {

  private currentDepartmentNavigationInformation$ = new BehaviorSubject<DepartmentNavigationInformation>(
    new DepartmentNavigationInformation(-1, [])
  );

  constructor(private currentstructureSchemaService: CurrentStructureSchemeService) {
  }

  getCurrentDepartmentNavigationInformation$(): Observable<DepartmentNavigationInformation> {
    return this.currentDepartmentNavigationInformation$.asObservable();
  }

  refreshDepartmentNavigationInformation(): void {
    this.currentstructureSchemaService.getCurrentStructureId$()
      .pipe(
        switchMap((structureSchemaId: number) => {
            return this.currentstructureSchemaService.getStructureIdsToReachStructureWithId$(structureSchemaId)
              .pipe(
                map((departmentIdList: number[]) => {
                    return new DepartmentNavigationInformation(structureSchemaId, departmentIdList);
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
