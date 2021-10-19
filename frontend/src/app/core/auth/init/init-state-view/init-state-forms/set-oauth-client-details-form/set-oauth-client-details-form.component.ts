import { Component, EventEmitter, OnInit } from '@angular/core';
import { InitStateFormComponent } from '../init-state-form/init-state-form.component';
import { FormBuilder, Validators } from '@angular/forms';
import { InitState } from '../../../../../../shared/model/api/init-state';
import { InitService } from '../../../../../services/init.service';
import { OauthClientDetails } from '../../../../../../shared/model/api/oauth-client-details';
import { FormGroupTyped } from '../../../../../../../typings';
import { Consts } from '../../../../../../shared/consts';
import { OAuthFrontendDetailsService } from '../../../../services/o-auth-frontend-details.service';
import { MatSnackBar } from '@angular/material/snack-bar';
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

  private timeoutErrorMessage: string = this.i18n({
    id: 'initOauthClientDetailsUpdateTimeoutMessage',
    description: 'error text when the timeout is reached for updating the oauthclientdetails',
    value: 'Der Server hat zu lange für die Konfiguration gebraucht. Bitte überprüfen Sie den Server und laden die Seite neu.'
  });

  private timeoutErrorMessageAction: string = this.i18n({
    id: 'short_okay',
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

  handleSubmitClick(): void {
    this.toggleLoadingScreen();
    this.changeOauthClientDetailsBasedOnFormData();
    this.initService.postOauthClientDetails$(this.oauthClientDetails)
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

  private toggleLoadingScreen(): void {
    this.spinnerIsHidden = !this.spinnerIsHidden;
  }
}
