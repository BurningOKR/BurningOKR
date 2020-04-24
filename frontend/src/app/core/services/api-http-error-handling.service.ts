import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, ObservableInput, Subject, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { switchMap } from 'rxjs/operators';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { AuthenticationService } from '../auth/services/authentication.service';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { Consts } from '../../shared/consts';
import { NGXLogger } from 'ngx-logger';
import { Router } from '@angular/router';

export type ErrorObservable<T> = Observable<Observable<T> extends ObservableInput<infer D> ? T : never>;
export type ErrorHandlingFunction<T> = (error: HttpErrorResponse) => ErrorObservable<T>;
export type RetryFunction<T> = () => Observable<T>;

@Injectable({
  providedIn: 'root'
})
export class ApiHttpErrorHandlingService {

  private errorSubjects: Subject<boolean>[] = [];
  private errors$ = new BehaviorSubject<HttpErrorResponse[]>([]);

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

  constructor(private authenticationService: AuthenticationService,
              private i18n: I18n,
              private snackbar: MatSnackBar,
              private logger: NGXLogger,
              private router: Router) {
  }

  getErrors$(): Observable<HttpErrorResponse[]> {
    return this.errors$.asObservable();
  }

  getErrorHandler<T>(retryFunction: RetryFunction<T>, customHandlerFunction?: (error: HttpErrorResponse) => Observable<T>):
    ErrorHandlingFunction<T> {

    return (error: HttpErrorResponse) => {
      if (!!customHandlerFunction) {
        return customHandlerFunction(error);
      } else if (this.isClientResolvableError(error)) {
        return this.getClientResolvableErrorHandler$(retryFunction);
      } else if (this.isUnauthorizedError(error)) {
        return this.getUnauthorizedErrorHandler$(error);
      } else {
        return this.getNonClientResolvableErrorHandler$(error);
      }
    };
  }

  private getClientResolvableErrorHandler$<T>(retryFunction: RetryFunction<T>): ErrorObservable<T> {
    const errorSubject$: Subject<boolean> = new Subject<boolean>();
    this.errorSubjects.push(errorSubject$);
    this.showRetryErrorSnackbar();

    return errorSubject$.pipe(switchMap(retryFunction));
  }

  private getNonClientResolvableErrorHandler$<T>(error: HttpErrorResponse): ErrorObservable<T> {
    this.errors$.value.push(error);
    this.errors$.next(this.errors$.value);
    this.showErrorSnackbar();

    this.logErrorIfUserIsSignedIn(error);

    return throwError(error);
  }

  private getUnauthorizedErrorHandler$<T>(error: HttpErrorResponse): ErrorObservable<T> {
    this.authenticationService.logout();
    this.authenticationService.startLoginProcedure();

    return throwError(error);
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
    this.errorSubjects.forEach((errSubject$: Subject<boolean>) => {
      errSubject$.next(true);
      errSubject$.unsubscribe();
    });
    this.errorSubjects = [];
  }

  private isClientResolvableError(error: HttpErrorResponse): boolean {
    return Consts.CLIENT_RESOLVABLE_ERRORS.indexOf(error.status) > -1;
  }

  private isUnauthorizedError(error: HttpErrorResponse): boolean {
    return error.status === Consts.UNAUTHORIZED_ERROR;
  }

  private logErrorIfUserIsSignedIn(error: HttpErrorResponse): void {
    if (this.authenticationService.hasValidAccessToken()) {
      this.logger.error(error);
    }
  }
}
