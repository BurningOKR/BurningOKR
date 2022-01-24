import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { AbstractControl, FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { Observable, of, Subscription } from 'rxjs';
import { map, switchMap, take } from 'rxjs/operators';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData,
} from '../../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { Configuration } from '../../../../shared/model/ui/configuration';
import { OAuthFrontendDetailsService } from '../../../auth/services/o-auth-frontend-details.service';
import { CurrentUserService } from '../../../services/current-user.service';
import { ConfigurationManagerService } from '../../configuration-manager.service';
import { SettingsForm } from '../settings-form';

@Component({
  selector: 'app-admin-settings',
  templateUrl: './admin-settings-form.component.html',
  styleUrls: ['./admin-settings-form.component.scss'],
  providers: [{ provide: SettingsForm, useExisting: AdminSettingsFormComponent }],
})
export class AdminSettingsFormComponent extends SettingsForm implements OnInit, OnDestroy {

  @Output() valid: EventEmitter<boolean> = new EventEmitter<boolean>();

  subscriptions: Subscription[] = [];
  adminSettingsForm: FormGroup;
  isAzure$: Observable<boolean>;
  configurationNames: { [key: string]: string };

  private confirmationTitle: string;
  private confirmationText: string;
  private configurationNamesTranslationKeys: string[] = [
    'admin-settings-form.config-names.max-key-results',
    'admin-settings-form.config-names.topic-sponsors-activated',
    'admin-settings-form.config-names.threshold.green-yellow',
    'admin-settings-form.config-names.threshold.yellow-red',
    'admin-settings-form.config-names.frontend-base-url',
    'admin-settings-form.config-names.email-sender',
    'admin-settings-form.config-names.subject.new-user',
    'admin-settings-form.config-names.subject.reset-password',
    'admin-settings-form.config-names.subject.feedback',
    'admin-settings-form.config-names.email-feedabck-receiver',
  ];

  constructor(private configurationManagerService: ConfigurationManagerService,
              private currentUserService: CurrentUserService,
              private dialog: MatDialog,
              private translate: TranslateService,
              private oAuthDetails: OAuthFrontendDetailsService,
  ) {
    super();
  }

  get settings(): FormArray {
    return this.adminSettingsForm.get('settings') as FormArray;
  }

  ngOnInit(): void {
    this.initAdminSettingsForm();
    this.subscriptions.push(this.adminSettingsForm.statusChanges.subscribe(() => {
      this.valid.emit(this.adminSettingsForm.valid);
    }));

    this.confirmationTitle = this.translate.instant('admin-settings-form.confirmation-title');
    this.confirmationText =this.translate.instant('admin-settings-form.confirmation-text');

    this.translate.stream(this.configurationNamesTranslationKeys).pipe(take(1))
      .subscribe((translations: { [key: string]: string }) => {
        this.configurationNames = {
          'max-key-results': translations[this.configurationNamesTranslationKeys[0]],
          'topic-sponsors-activated': translations[this.configurationNamesTranslationKeys[1]],
          'objective-progress-green-yellow-threshold': translations[this.configurationNamesTranslationKeys[2]],
          'objective-progress-yellow-red-threshold': translations[this.configurationNamesTranslationKeys[3]],
          'general_frontend-base-url': translations[this.configurationNamesTranslationKeys[4]],
          email_from: translations[this.configurationNamesTranslationKeys[5]],
          'email_subject_new-user': translations[this.configurationNamesTranslationKeys[6]],
          'email_subject_forgot-password': translations[this.configurationNamesTranslationKeys[7]],
          email_subject_feedback: translations[this.configurationNamesTranslationKeys[8]],
          feedback_receivers: translations[this.configurationNamesTranslationKeys[9]],
        };
      });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  createUpdate$(): Observable<any> {
    return this.currentUserService.isCurrentUserAdmin$()
      .pipe(
        take(1),
        switchMap(isAdmin => {
          if (isAdmin) {
            return this.configurationManagerService.updateConfigurations$(this.adminSettingsForm.getRawValue().settings);
          } else {
            return of(null);
          }
        }),
      );
  }

  canClose$(): Observable<boolean> {
    if (!this.userDeactivatedTopicSponsors()) {
      return of(true);
    } else {
      return this.promptTopicSponsorConfirmation$();
    }
  }

  private initAdminSettingsForm(): void {
    this.subscriptions.push(this.configurationManagerService.getAllConfigurations$()
      .pipe(
        map((configurations: Configuration[]) => {
          return configurations
            .map((configuration: Configuration) => {
              return new FormGroup({
                id: new FormControl(configuration.id),
                name: new FormControl(configuration.name),
                value: new FormControl(this.mapConfigurationValueToBooleanIfTypeIsCheckbox(configuration), [Validators.required]),
                type: new FormControl(configuration.type),
              });
            })
            .sort((a: FormGroup, b: FormGroup) => +a.controls.id.value - +b.controls.id.value);
        }),
      )
      .subscribe((formGroups: FormGroup[]) => {
        this.adminSettingsForm = new FormGroup({
          settings: new FormArray(formGroups),
        });
      }));

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

  private promptTopicSponsorConfirmation$(): Observable<boolean> {
    const data: ConfirmationDialogData = {
      title: this.confirmationTitle,
      message: this.confirmationText,
    };

    return this.dialog.open(ConfirmationDialogComponent, { data })
      .afterClosed();
  }

  private userDeactivatedTopicSponsors(): boolean {
    const control: AbstractControl = this.getConfigurationFromFormByName('topic-sponsors-activated');

    return !!control && !control.get('value').value && control.dirty;
  }

  private getConfigurationFromFormByName(name: string): AbstractControl {
    for (const control of this.settings.controls) {
      if (control.get('name').value === name) {
        return control;
      }
    }

    return null;
  }
}
