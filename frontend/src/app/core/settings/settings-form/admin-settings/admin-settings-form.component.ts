import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material';
import { CurrentUserService } from '../../../services/current-user.service';
import { Observable, of } from 'rxjs';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { ConfigurationManagerService } from '../../configuration-manager.service';
import { UserSettings } from '../../../../shared/model/ui/user-settings';
import { Configuration } from '../../../../shared/model/ui/configuration';
import { filter, map, switchMap, take } from 'rxjs/operators';
import { OAuthFrontendDetailsService } from '../../../auth/services/o-auth-frontend-details.service';
import { I18n } from '@ngx-translate/i18n-polyfill';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData
} from '../../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { SettingsForm } from '../settings-form';

@Component({
  selector: 'app-admin-settings',
  templateUrl: './admin-settings-form.component.html',
  styleUrls: ['./admin-settings-form.component.scss'],
  providers: [{provide: SettingsForm, useExisting: AdminSettingsFormComponent}]
})
export class AdminSettingsFormComponent extends SettingsForm implements OnInit {

  adminSettingsForm: FormGroup;
  isAzure$: Observable<boolean>;

  @Output() valid: EventEmitter<boolean> = new EventEmitter<boolean>();

  private confirmationTitle: string = this.i18n({
    id: '@@deativate_okr_topic_sponsors_title',
    description: 'title of confirmation dialog for deaktivating topic sponsors',
    value: `OKR Themenpaten deaktivieren`
  });

  private confirmationText: string = this.i18n({
    id: '@@deativate_okr_topic_sponsors_text',
    description: 'text of confirmation dialog for deaktivating topic sponsors',
    value: `Durch das deaktivieren aller OKR-Themenpaten, werden diese zu Teammitgliedern in ihrem jeweiligen Team.`
  });

  private configurationNames: { [key: string]: string } = {
    'max-key-results': this.i18n({
      id: '@@settings_form_max_key_results_per_objective',
      description: 'Placeholder for maximal amount of keyresults per objective',
      value: 'Maximale Anzahl von Key Results'
    }),
    'topic-sponsors-activated': this.i18n({
      id: '@@settings_topic_sponsors',
      description: 'Placeholder for the setting, which de-/activates topic sponsors through out the application',
      value: 'Themenpaten aktiviert'
    }),
    'objective-progress-green-yellow-threshold': this.i18n({
      id: '@@settings_form_objective_bar_treshold_green_yellow',
      description: 'Treshold for objective progress bar (Green/Yellow)',
      value: 'Objective Prognose Schwellenwert (Grün/Gelb)'
    }),
    'objective-progress-yellow-red-threshold': this.i18n({
      id: '@@settings_form_objective_bar_treshold_yellow_red',
      description: 'Treshold for objective progress bar (Yellow/Red)',
      value: 'Objective Prognose Schwellenwert (Gelb/Rot)'
    }),
    'general_frontend-base-url': this.i18n({
      id: '@@settings_form_general_frontend_base_url',
      description: 'the domain of this angular application',
      value: 'Frontend-Base-Url'
    }),
    email_from: this.i18n({
      id: '@@settings_form_email_sender',
      description: 'The address, from which emails are sent',
      value: 'Email Adresse des OKR Tools'
    }),
    'email_subject_new-user': this.i18n({
      id: '@@settings_form_email_subject_forgot_new_user',
      description: 'the subject of the email, which is sent to new users',
      value: 'Email Betreff für neue Benutzer'
    }),
    'email_subject_forgot-password': this.i18n({
      id: '@@settings_form_email_subject_forgot_password',
      description: 'the subject of the email, which is sent to users who forgot their password',
      value: 'Email Betreff für die Passwort-Zurücksetzen Email'
    }),
    email_subject_feedback: this.i18n({
      id: '@@settings_form_email_subject_feedback',
      description: 'the subject of the feedback email',
      value: 'Email Betreff für Feedback'
    }),
    feedback_receivers: this.i18n({
      id: '@@settings_form_feedback_receivers',
      description: 'the email adresses of the people who receive feedback',
      value: 'Email Adressen der Feedback Empfänger. (Durch Komma getrennt)'
    })
  };

  constructor(private configurationManagerService: ConfigurationManagerService,
              private currentUserService: CurrentUserService,
              private dialog: MatDialog,
              private i18n: I18n,
              private oAuthDetails: OAuthFrontendDetailsService,
  ) {
    super();
  }

  get settings(): FormArray {
    return this.adminSettingsForm.get('settings') as FormArray;
  }

  ngOnInit(): void {
    this.initAdminSettingsForm();
    this.adminSettingsForm.statusChanges.subscribe(() => {
      this.valid.emit(this.adminSettingsForm.valid);
    });
  }

  createUpdate$(): Observable<UserSettings | Configuration> {
    if (!this.userDeactivatedTopicSponsors()) {
      return this.createUpdateObservable$();
    } else {
      return this.openDeactivateTopicSponsorConfirmationDialog$();
    }
  }

  private createUpdateObservable$(): Observable<Configuration> {
    return this.currentUserService.isCurrentUserAdmin$()
      .pipe(
        take(1),
        switchMap(isAdmin => {
          if (isAdmin) {
            return this.configurationManagerService.updateConfigurations$(this.adminSettingsForm.getRawValue().settings);
          } else {
            return of(null);
          }
        })
      );
  }

  private initAdminSettingsForm(): void {
    this.configurationManagerService.getAllConfigurations$()
      .pipe(
        map((configurations: Configuration[]) => {
          return configurations
            .map((configuration: Configuration) => {
              return new FormGroup({
                id: new FormControl(configuration.id),
                name: new FormControl(configuration.name),
                value: new FormControl(this.mapConfigurationValueToBooleanIfTypeIsCheckbox(configuration), [Validators.required]),
                type: new FormControl(configuration.type)
              });
            })
            .sort((a: FormGroup, b: FormGroup) => +a.controls.id.value - +b.controls.id.value);
        })
      )
      .subscribe((formGroups: FormGroup[]) => {
        this.adminSettingsForm = new FormGroup({
          settings: new FormArray(formGroups)
        });
      });

    this.isAzure$ = this.oAuthDetails.isAzureAuthType$()
      .pipe(take(1));
  }

  private mapConfigurationValueToBooleanIfTypeIsCheckbox(configuration: Configuration): string | boolean {
    if (configuration.type === 'checkbox') {
      return configuration.value === 'true' || configuration.value === true;
    } else {
      return configuration.value;
    }
  }

  private openDeactivateTopicSponsorConfirmationDialog$(): Observable<Configuration> {
    const data: ConfirmationDialogData = {
      title: this.confirmationTitle,
      message: this.confirmationText,
    };

    return this.dialog.open(ConfirmationDialogComponent, {data})
      .afterClosed()
      .pipe(
        filter(v => v),
        switchMap(() => this.createUpdateObservable$())
      );
  }

  private userDeactivatedTopicSponsors(): boolean {
    for (const control of this.settings.controls) {
      if (control.get('name').value === 'topic-sponsors-activated') {
        return !control.get('value').value && control.dirty;
      }
    }

    return false;
  }
}
