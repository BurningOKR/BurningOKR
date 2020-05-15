import { Observable, of } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorHandlingFunction } from '../../core/services/api-http-error-handling.service';

export class ApiHttpServiceMock {

  getErrors$(): Observable<HttpErrorResponse[]> { return of(); }

  getData$<T>(path: string, customErrorHandler?: ErrorHandlingFunction<T>): Observable<T> { return of(); }

  postData$<T>(path: string, value: object, customErrorHandler?: ErrorHandlingFunction<T>): Observable<T> { return of(); }

  putData$<T>(path: string, value: object, customErrorHandler?: ErrorHandlingFunction<T>): Observable<T> { return of(); }

  deleteData$(path: string, customErrorHandler?: ErrorHandlingFunction<boolean>): Observable<boolean> { return of(); }

  patchData$<T>(path: string, value: T, customErrorHandler?: ErrorHandlingFunction<T>): Observable<T> { return of(); }
}
