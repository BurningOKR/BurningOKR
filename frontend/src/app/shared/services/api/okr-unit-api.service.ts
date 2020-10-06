import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { Observable, throwError } from 'rxjs';
import { OkrChildUnitDto } from '../../model/api/OkrUnit/okr-child-unit.dto';
import { OkrUnitSchema } from '../../model/ui/okr-unit-schema';
import { map } from 'rxjs/operators';
import { plainToClass } from 'class-transformer';
import { OkrDepartmentDto } from '../../model/api/OkrUnit/okr-department.dto';
import { OkrBranchDto } from '../../model/api/OkrUnit/okr-branch.dto';
import { CompanyDto } from '../../model/api/OkrUnit/company.dto';
import { OkrUnitId } from '../../model/id-types';
import { ErrorHandlingFunction } from '../../../core/services/api-http-error-handling.service';

@Injectable({
  providedIn: 'root'
})
export class OkrUnitApiService {

  constructor(private http: ApiHttpService) { }

  getOkrChildUnitById$(unitId: OkrUnitId, handleErrors?: boolean): Observable<OkrChildUnitDto> {
    const errorHandlingFunction: ErrorHandlingFunction<any> = this.getErrorHandlingFunction(handleErrors);

    return this.http.getData$<OkrChildUnitDto>(`units/${unitId}`, errorHandlingFunction)
      .pipe(
        map((value: OkrChildUnitDto) => {
          if (value.__okrUnitType === 'DEPARTMENT') {
            return plainToClass(OkrDepartmentDto, value);
          } else if (value.__okrUnitType === 'OKR_BRANCH') {
            return plainToClass(OkrBranchDto, value);
          }
        })
      );
  }

  putOkrChildUnit$(okrUnitId: OkrUnitId, okrChildUnit: OkrChildUnitDto, handleErrors?: boolean): Observable<OkrChildUnitDto> {
    const errorHandlingFunction: ErrorHandlingFunction<any> = this.getErrorHandlingFunction(handleErrors);

    return this.http.putData$<OkrChildUnitDto>(`units/${okrUnitId}`, okrChildUnit, errorHandlingFunction)
      .pipe(
        map((value: OkrChildUnitDto) => {
          if (value.__okrUnitType === 'DEPARTMENT') {
            return plainToClass(OkrDepartmentDto, value);
          } else if (value.__okrUnitType === 'OKR_BRANCH') {
            return plainToClass(OkrBranchDto, value);
          }
        })
      );
  }

  deleteOkrChildUnit$(okrUnitId: OkrUnitId, handleErrors?: boolean): Observable<boolean> {
    const errorHandlingFunction: ErrorHandlingFunction<any> = this.getErrorHandlingFunction(handleErrors);

    return this.http.deleteData$(`units/${okrUnitId}`, errorHandlingFunction);
  }

  getOkrUnitSchemaByUnitId$(okrUnitId: OkrUnitId, handleErrors?: boolean): Observable<OkrUnitSchema[]> {
    const errorHandlingFunction: ErrorHandlingFunction<any> = this.getErrorHandlingFunction(handleErrors);

    return this.http.getData$<OkrUnitSchema[]>(`units/${okrUnitId}/schema`, errorHandlingFunction);
  }

  getParentCompanyOfOkrUnit$(okrUnitId: OkrUnitId, handleErrors?: boolean): Observable<CompanyDto> {
    const errorHandlingFunction: ErrorHandlingFunction<any> = this.getErrorHandlingFunction(handleErrors);

    return this.http.getData$(`units/${okrUnitId}/company`, errorHandlingFunction);
  }

  putOkrUnitObjectiveSequence$(departmentId: number, sequenceList: number[], handleErrors?: boolean): Observable<number[]> {
    const errorHandlingFunction: ErrorHandlingFunction<any> = this.getErrorHandlingFunction(handleErrors);

    return this.http.putData$(`units/${departmentId}/objectivesequence`, sequenceList, errorHandlingFunction);
  }

  private getErrorHandlingFunction(handleErrors: boolean = true): ErrorHandlingFunction<any> {
    let errorHandlingFunction: ErrorHandlingFunction<any>;
    if (!handleErrors) {
      errorHandlingFunction = throwError;
    }

    return errorHandlingFunction;
  }
}
