import { Component, EventEmitter, OnInit } from '@angular/core';
import { InitStateFormComponent } from '../init-state-form/init-state-form.component';
import { FormBuilder, Validators } from '@angular/forms';
import { InitState } from '../../../../../../../shared/model/api/init-state';
import { InitService } from '../../../../../../services/init.service';
import { OauthClientDetails } from '../../../../../../../shared/model/api/oauth-client-details';
import { FormGroupTyped } from '../../../../../../../../typings';
import { Consts } from '../../../../../../../shared/consts';
import { filter, map, skipWhile, switchMap, take, tap } from 'rxjs/operators';
import { EMPTY, interval, Observable, throwError } from 'rxjs';
import { OAuthFrontendDetailsService } from '../../../../../services/o-auth-frontend-details.service';
import { MatSnackBar } from '@angular/material';
import { I18n } from '@ngx-translate/i18n-polyfill';

@Component({
  selector: 'app-set-oauth-client-details-form',
  templateUrl: './set-oauth-client-details-form.component.html',
  styleUrls: ['./set-oauth-client-details-form.component.css']
})
export class SetOauthClientDetailsFormComponent extends InitStateFormComponent implements OnInit {
  form: FormGroupTyped<OauthClientDetails>;
  eventEmitter: EventEmitter<InitState> = new EventEmitter<InitState>();
  oauthClientDetails: OauthClientDetails = new OauthClientDetails();
  spinnerIsHidden: boolean = true;
  unsuccessfulPingAttempts: number = 0;

  private lastRequestFinished: boolean = true;

  private timeoutErrorMessage: string = this.i18n({
    id: 'initOauthClientDetailsUpdateTimeoutMessage',
    description: 'error text when the timeout is reached for updating the oauthclientdetails',
    value: 'Der Server hat zu lange für die Konfiguration gebraucht. Bitte überprüfen Sie den Server und laden die Seite neu.'
  });

  private timeoutErrorMessageAction: string = this.i18n({
    id: 'initOauthClientDetailsUpdateTimeoutAction',
    description: 'action button for the snackbar',
    value: 'Ok'
  });

  constructor(
    private formBuilder: FormBuilder,
    private initService: InitService,
    private oAuthFrontendDetails: OAuthFrontendDetailsService,
    private snackBar: MatSnackBar,
    private i18n: I18n
  ) {
    super();
  }

  ngOnInit(): void {
    this.generateOauthClientDetailsForm();
  }

  private generateOauthClientDetailsForm(): void {
    this.form = this.formBuilder.group({
      accessTokenValidity: [this.oauthClientDetails.accessTokenValidity, [Validators.required, Validators.min(Consts.MIN_TOKEN_DURATION)]],
      clientId: [this.oauthClientDetails.clientId, [Validators.required]],
      clientSecret: [this.oauthClientDetails.clientSecret, [Validators.required]],
      refreshTokenValidity: [this.oauthClientDetails.refreshTokenValidity,
                             [Validators.required, Validators.min(Consts.MIN_TOKEN_DURATION)]],
      webServerRedirectUri: [this.oauthClientDetails.webServerRedirectUri, [Validators.required]]
    }) as FormGroupTyped<OauthClientDetails>;
    this.form.disable();
  }

  private handleSubmitClick(): void {
    this.unsuccessfulPingAttempts = 0;
    this.toggleLoadingScreen();
    this.changeOauthClientDetailsBasedOnFormData();
    this.initService.postOauthClientDetails$(this.oauthClientDetails)
      .pipe(
        switchMap(initState => this.waitForRestart$(initState))
      )
      .subscribe(initState => {
        this.oAuthFrontendDetails.reloadOAuthFrontendDetails();
        this.eventEmitter.emit(initState);
        this.toggleLoadingScreen();
      }, () => {
        this.toggleLoadingScreen();
        this.snackBar.open(this.timeoutErrorMessage, this.timeoutErrorMessageAction,
          {panelClass: 'api-error-snackbar', verticalPosition: 'top'});
      });
  }

  toggleForm(): void {
    if (this.form.enabled) {
      this.form.disable();
    } else {
      this.form.enable();
    }
  }

  private changeOauthClientDetailsBasedOnFormData(): void {
    const typedForm: FormGroupTyped<OauthClientDetails> = this.form;
    this.oauthClientDetails = typedForm.getRawValue();
  }

  // the server has to restart to save the new oAuthClientDetails.
  // pings the server until it gets a valid response
  private waitForRestart$(initState: InitState): Observable<InitState> {
    if (!!initState) {
      this.lastRequestFinished = true;
      this.unsuccessfulPingAttempts = 0;

      return interval(Consts.MIN_INTERVAL_BETWEEN_PING_ATTEMPTS)
        .pipe(
          filter(() => this.lastRequestFinished),
          switchMap(() => {
            this.lastRequestFinished = false;

            return this.pingServerOnce$(initState);
          }),
          skipWhile((newInitState: InitState) => !newInitState),
          take(1)
        );
    } else {
      return throwError('no init state given');
    }
  }

  private pingServerOnce$(initState: InitState): Observable<InitState> {
    return this.initService.getInitState$(() => this.handleError$())
      .pipe(
        tap(() => this.lastRequestFinished = true),
        map((newInitState: InitState) => {
          if (newInitState.runtimeId !== initState.runtimeId) {
            return newInitState;
          } else {
            return null;
          }
        })
      );
  }

  private handleError$(): Observable<never> {
    this.lastRequestFinished = true;
    this.unsuccessfulPingAttempts = this.unsuccessfulPingAttempts + 1;
    if (this.unsuccessfulPingAttempts > Consts.MAX_UNSUCCESSFUL_PING_ATTEMPTS_FOR_RESTART) {
      return throwError('max ping attempts reached');
    } else {
      return EMPTY;
    }
  }

  private toggleLoadingScreen(): void {
    this.spinnerIsHidden = !this.spinnerIsHidden;
  }
}
