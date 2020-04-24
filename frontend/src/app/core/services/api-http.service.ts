import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { NGXLogger } from 'ngx-logger';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { Consts } from '../../shared/consts';
import { ApiHttpErrorHandlingService, ErrorHandlingFunction } from './api-http-error-handling.service';

@Injectable({
  providedIn: 'root'
})
export class ApiHttpService {

  private httpOptions: object;

  constructor(
    private http: HttpClient,
    private snackbar: MatSnackBar,
    private router: Router,
    private logger: NGXLogger,
    private i18n: I18n,
    private errorHandlerService: ApiHttpErrorHandlingService
  ) {
    this.httpOptions = {
      headers: new HttpHeaders({})
    };
  }

  getErrors$(): Observable<HttpErrorResponse[]> {
    return this.errorHandlerService.getErrors$();
  }

  getData$<T>(path: string, customErrorHandler?: ErrorHandlingFunction<T>): Observable<T> {

    return this.http.get<T>(Consts.API_URL + path, this.httpOptions)
      .pipe(
        catchError(
          this.errorHandlerService.getErrorHandler(() => this.getData$<T>(path), customErrorHandler)
        )
      );
  }

  postData$<T>(path: string, value: object, customErrorHandler?: ErrorHandlingFunction<T>): Observable<T> {

    return this.http.post<T>(Consts.API_URL + path, value, this.httpOptions)
      .pipe(
        catchError(
          this.errorHandlerService.getErrorHandler(() => this.postData$<T>(path, value), customErrorHandler)
        )
      );
  }

  putData$<T>(path: string, value: object, customErrorHandler?: ErrorHandlingFunction<T>): Observable<T> {
    return this.http.put<T>(Consts.API_URL + path, value, this.httpOptions)
      .pipe(
        catchError(
          this.errorHandlerService.getErrorHandler(() => this.putData$<T>(path, value), customErrorHandler)
        )
      );
  }

  deleteData$(path: string, customErrorHandler?: ErrorHandlingFunction<boolean>): Observable<boolean> {
    return this.http.delete(Consts.API_URL + path, this.httpOptions)
      .pipe(
        map((res: string) => res === 'deleted'),
        catchError(
          this.errorHandlerService.getErrorHandler(() => this.deleteData$(path), customErrorHandler)
        )
      );
  }

  patchData$<T>(path: string, value: T, customErrorHandler?: ErrorHandlingFunction<T>): Observable<T> {

    return this.http.patch<T>(Consts.API_URL + path, value, this.httpOptions)
      .pipe(
        catchError(this.errorHandlerService.getErrorHandler(() => {
            return this.patchData$<T>(path, value);
          }, customErrorHandler
        ))
      );
  }
}
