import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { CurrentDepartmentStructureService } from './current-department-structure.service';
import { map, switchMap } from 'rxjs/operators';
import { DepartmentNavigationInformation } from '../shared/model/ui/department-navigation-information';

@Injectable({
  providedIn: 'root'
})
export class CurrentNavigationService {

  private currentDepartmentNavigationInformation$ = new BehaviorSubject<DepartmentNavigationInformation>(
    new DepartmentNavigationInformation(-1, [])
  );

  constructor(private currentDepartmentStructureService: CurrentDepartmentStructureService) {
  }

  getCurrentDepartmentNavigationInformation$(): Observable<DepartmentNavigationInformation> {
    return this.currentDepartmentNavigationInformation$.asObservable();
  }

  refreshDepartmentNavigationInformation(): void {
    this.currentDepartmentStructureService.getCurrentDepartmentId$()
      .pipe(
        switchMap((departmentStructureId: number) => {
            return this.currentDepartmentStructureService.getDepartmentIdListToReachDepartmentWithId$(departmentStructureId)
              .pipe(
                map((departmentIdList: number[]) => {
                    return new DepartmentNavigationInformation(departmentStructureId, departmentIdList);
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
