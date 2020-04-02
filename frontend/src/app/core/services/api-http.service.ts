import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, ObservableInput, Subject, throwError } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { NGXLogger } from 'ngx-logger';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { AuthenticationService } from '../auth/services/authentication.service';
import { Consts } from '../../shared/consts';

const clientResolvableErrors: number[] = [
  0,
  407,
  408,
  409,
  412,
  421,
  423,
  424,
  429,
  444,
  499,
  501,
  502,
  503,
  505,
  507,
  509,
  510,
  511
];

const unauthorizedError: number = 401;

@Injectable({
  providedIn: 'root'
})
export class ApiHttpService {

  private httpOptions: object;
  private errorSubjects: Subject<boolean>[] = [];
  private errors = new BehaviorSubject<HttpErrorResponse[]>([]);

  private singleErrorMsg: string = this.i18n({
    id: 'apiSingleErrorMsg',
    description: 'error text when a single api call fails',
    value: 'Fehler beim Ausführen der Anfrage.'
  });
  private oneOrMoreErrorMsg: string = this.i18n({
    id: 'apiOneOrMoreErrorMsg',
    description: 'error text when a single or more api call fails',
    value: 'Fehler beim Ausführen einer oder mehrerer Anfragen.'
  });
  private moreDetailsMsg: string = this.i18n({
    id: 'apiMoreDetailsMsg',
    value: 'Mehr Details'
  });
  private retryMsg: string = this.i18n({
    id: 'apiRetryMsg',
    value: 'Erneut versuchen'
  });
  private requestAppendix: string = this.i18n({
    id: 'apiRequestAppendix',
    description: 'Appendix for pluralization of the word request if there are multiple failed requests.',
    value: 'n'
  });

  constructor(
    private http: HttpClient,
    private snackbar: MatSnackBar,
    private router: Router,
    private logger: NGXLogger,
    private i18n: I18n,
    private authorizationService: AuthenticationService,
  ) {
    this.httpOptions = {
      headers: new HttpHeaders({})
    };
  }

  getErrors(): BehaviorSubject<HttpErrorResponse[]> {
    return this.errors;
  }

  getData$<T>(path: string): Observable<T> {

    return this.http.get<T>(Consts.API_URL + path, this.httpOptions)
      .pipe(catchError(this.errorHandler(() => this.getData$<T>(path))));
  }

  postData$<T>(path: string, value: object): Observable<T> {

    return this.http.post<T>(Consts.API_URL + path, value, this.httpOptions)
      .pipe(catchError(this.errorHandler(() => this.postData$<T>(path, value))));
  }

  putData$<T>(path: string, value: object): Observable<T> {
    return this.http.put<T>(Consts.API_URL + path, value, this.httpOptions)
      .pipe(catchError(this.errorHandler(() => this.putData$<T>(path, value))));
  }

  deleteData$(path: string): Observable<boolean> {
    return this.http.delete(Consts.API_URL + path, this.httpOptions)
      .pipe(map((res: string) => res === 'deleted'),
        catchError(this.errorHandler(() => this.deleteData$(path))));
  }

  patchData<T>(path: string, value: T): Observable<T> {

    return this.http.patch<T>(Consts.API_URL + path, value, this.httpOptions)
      .pipe(
        catchError(this.errorHandler(() => {
            return this.patchData<T>(path, value);
          }
        )));
  }

  private errorHandler<T>(retryFunction: () => Observable<T>):
    (error: HttpErrorResponse) => (Observable<Observable<T> extends ObservableInput<infer D> ? T : never>) {
    return (error: HttpErrorResponse) => {
      if (this.isClientResolvableError(error)) {
        const errorSubject: Subject<boolean> = new Subject<boolean>();
        this.errorSubjects.push(errorSubject);
        this.showRetryErrorSnackbar();

        return errorSubject.pipe(switchMap(retryFunction));
      } else if (this.isUnauthorizedError(error)) {
        this.authorizationService.logout();
        this.authorizationService.startLoginProcedure();

        return throwError(error);
      } else {
        this.errors.value.push(error);
        this.errors.next(this.errors.value);
        this.showErrorSnackbar();

        this.logErrorIfUserIsSignedIn(error);

        return throwError(error);
      }
    };
  }

  private showErrorSnackbar(): void {
    const snackbar: MatSnackBarRef<SimpleSnackBar> = this.snackbar
      .open(this.oneOrMoreErrorMsg, this.moreDetailsMsg, {
        panelClass: 'api-error-snackbar',
        verticalPosition: 'top',
        duration: 20000
      });
    snackbar.onAction()
      .subscribe(() => {
        this.router.navigate(['error'])
          .catch(err => this.logErrorIfUserIsSignedIn(err));
      });
  }

  private showRetryErrorSnackbar(): void {
    const errorCountMsg: string = this.i18n({
      id: 'apiErrorCountMsg',
      value: '{{errorCount}} fehlgeschlagene Anfrage{{pluralAppendix}}'
    }, {
      errorCount: this.errorSubjects.length,
      pluralAppendix: this.errorSubjects.length > 1 ? this.requestAppendix : ''
    });

    const snackbar: MatSnackBarRef<SimpleSnackBar> = this.snackbar
      .open(`${this.singleErrorMsg} ${errorCountMsg}.`,
        this.retryMsg,
        {
          panelClass: 'api-error-snackbar',
          verticalPosition: 'top',
          duration: 20000
        });
    snackbar.onAction()
      .subscribe(() => {
        this.retryErrors();
      });
  }

  private retryErrors(): void {
    this.errorSubjects.forEach((errSubject: Subject<boolean>) => {
      errSubject.next(true);
      errSubject.unsubscribe();
    });
    this.errorSubjects = [];
  }

  private isClientResolvableError(error: HttpErrorResponse): boolean {
    return clientResolvableErrors.indexOf(error.status) > -1;
  }

  private isUnauthorizedError(error: HttpErrorResponse): boolean {
    return error.status === unauthorizedError;
  }

  private logErrorIfUserIsSignedIn(error: HttpErrorResponse): void {
    if (this.authorizationService.hasValidAccessToken()) {
      this.logger.error(error);
    }
  }
}
