import { Component, EventEmitter, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { FormGroupTyped } from '../../../../../../../typings';
import { Consts } from '../../../../../../shared/consts';
import { InitState } from '../../../../../../shared/model/api/init-state';
import { OauthClientDetails } from '../../../../../../shared/model/api/oauth-client-details';
import { InitService } from '../../../../../services/init.service';
import { OAuthFrontendDetailsService } from '../../../../services/o-auth-frontend-details.service';
import { InitStateFormComponent } from '../init-state-form/init-state-form.component';

@Component({
  selector: 'app-set-oauth-client-details-form',
  templateUrl: './set-oauth-client-details-form.component.html',
  styleUrls: ['./set-oauth-client-details-form.component.css'],
})
export class SetOauthClientDetailsFormComponent extends InitStateFormComponent implements OnInit, OnDestroy {

  subscriptions: Subscription[] = [];
  form; // TODO: Typed Forms are now a build-in part of Angular. Add explicit typing for this attribute. TGohlisch, 19.09.2022
  eventEmitter: EventEmitter<InitState> = new EventEmitter<InitState>();
  oauthClientDetails: OauthClientDetails = new OauthClientDetails();
  spinnerIsHidden: boolean = true;

  private timeoutErrorMessage: string;
  private timeoutErrorMessageAction: string;

  constructor(
    private formBuilder: FormBuilder,
    private initService: InitService,
    private oAuthFrontendDetails: OAuthFrontendDetailsService,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
  ) {
    super();
  }

  ngOnInit(): void {
    this.generateOauthClientDetailsForm();
    this.timeoutErrorMessage = this.translate.instant('set-oauth-client-details-form.timeout.error-message');
    this.timeoutErrorMessageAction = this.translate.instant('set-oauth-client-details-form.timeout.error-message-action');
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  handleSubmitClick(): void {
    this.toggleLoadingScreen();
    this.changeOauthClientDetailsBasedOnFormData();
    this.subscriptions.push(this.initService.postOauthClientDetails$(this.oauthClientDetails)
      .subscribe(initState => {
        this.oAuthFrontendDetails.reloadOAuthFrontendDetails();
        this.eventEmitter.emit(initState);
        this.toggleLoadingScreen();
      }, () => {
        this.toggleLoadingScreen();
        this.snackBar.open(this.timeoutErrorMessage, this.timeoutErrorMessageAction,
          { panelClass: 'api-error-snackbar', verticalPosition: 'top' },
        );
      }));
  }

  toggleForm(): void {
    if (this.form.enabled) {
      this.form.disable();
    } else {
      this.form.enable();
    }
  }

  private generateOauthClientDetailsForm(): void {
    this.form = this.formBuilder.group({
      accessTokenValidity: [this.oauthClientDetails.accessTokenValidity, [Validators.required, Validators.min(Consts.MIN_TOKEN_DURATION)]],
      clientId: [this.oauthClientDetails.clientId, [Validators.required]],
      clientSecret: [this.oauthClientDetails.clientSecret, [Validators.required]],
      refreshTokenValidity: [this.oauthClientDetails.refreshTokenValidity,
        [Validators.required, Validators.min(Consts.MIN_TOKEN_DURATION)]],
      webServerRedirectUri: [this.oauthClientDetails.webServerRedirectUri, [Validators.required]],
    });
    this.form.disable();
  }

  private changeOauthClientDetailsBasedOnFormData(): void {
    const typedForm: FormGroupTyped<OauthClientDetails> = this.form;
    this.oauthClientDetails = typedForm.getRawValue();
  }

  private toggleLoadingScreen(): void {
    this.spinnerIsHidden = !this.spinnerIsHidden;
  }
}
