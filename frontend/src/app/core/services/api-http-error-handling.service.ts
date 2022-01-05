import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, Observable, ObservableInput, Subject, Subscription, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { switchMap } from 'rxjs/operators';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { AuthenticationService } from '../auth/services/authentication.service';
import { Consts } from '../../shared/consts';
import { NGXLogger } from 'ngx-logger';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

export type ErrorObservable<T> = Observable<Observable<T> extends ObservableInput<infer D> ? T : never>;
export type ErrorHandlingFunction<T> = (error: HttpErrorResponse) => ErrorObservable<T>;
export type RetryFunction<T> = () => Observable<T>;

@Injectable({
  providedIn: 'root'
})
export class ApiHttpErrorHandlingService implements OnInit, OnDestroy{

  private subscriptions: Subscription[] = [];
  private errorSubjects: Subject<boolean>[] = [];
  private errors$ = new BehaviorSubject<HttpErrorResponse[]>([]);

  private singleErrorMsg: string;
  private oneOrMoreErrorMsg: string;
  private moreDetailsMsg: string;
  private retryMsg: string;
  private requestAppendix: string;

  constructor(private authenticationService: AuthenticationService,
              private translate: TranslateService,
              private snackbar: MatSnackBar,
              private logger: NGXLogger,
              private router: Router) {
  }

  ngOnInit(): void {
    this.subscriptions.push(this.translate.stream('api-http-error-handling.error-message.single-error').subscribe(text => {
      this.singleErrorMsg = text;
    }));
    this.subscriptions.push(this.translate.stream('api-http-error-handling.error-message.one-or-more-errors').subscribe(text => {
      this.oneOrMoreErrorMsg = text;
    }));
    this.subscriptions.push(this.translate.stream('api-http-error-handling.error-message.more-detail').subscribe(text => {
      this.moreDetailsMsg = text;
    }));
    this.subscriptions.push(this.translate.stream('api-http-error-handling.error-message.retry').subscribe(text => {
      this.retryMsg = text;
    }));
    this.subscriptions.push(this.translate.stream('api-http-error-handling.error-message.appendix').subscribe(text => {
      this.requestAppendix = text;
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
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
    this.authenticationService.redirectToLoginProvider();

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
    const errorCountMsg: string = this.translate.instant('api-http-error-handling.error-counting', {
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
