import { Injectable } from '@angular/core';
import { ConfigurationMapper } from '../../shared/services/mapper/configuration.mapper';
import { BehaviorSubject, Observable } from 'rxjs';
import { Configuration } from '../../shared/model/ui/configuration';
import { Fetchable } from '../../shared/decorators/fetchable.decorator';

@Fetchable()
@Injectable({
  providedIn: 'root'
})
export class ConfigurationManagerService implements Fetchable {

  private _maxKeyResults$: BehaviorSubject<Configuration> = new BehaviorSubject<Configuration>(null);
  private _objectiveProgressGreenYellowThreshold$: BehaviorSubject<Configuration> = new BehaviorSubject<Configuration>(null);
  private _objectiveProgressYellowRedThreshold$: BehaviorSubject<Configuration> = new BehaviorSubject<Configuration>(null);
  private _generalFrontendBaseUrl$: BehaviorSubject<Configuration> = new BehaviorSubject<Configuration>(null);
  private _emailFrom$: BehaviorSubject<Configuration> = new BehaviorSubject<Configuration>(null);
  private _emailSubjectNewUser$: BehaviorSubject<Configuration> = new BehaviorSubject<Configuration>(null);
  private _emailSubjectForgotPassword$: BehaviorSubject<Configuration> = new BehaviorSubject<Configuration>(null);

  constructor(private configurationMapperService: ConfigurationMapper) {
  }

  get maxKeyResult(): Configuration {
    return this._maxKeyResults$.getValue();
  }

  get maxKeyResults(): Configuration {
    return this._maxKeyResults$.getValue();
  }

  get objectiveProgressGreenYellowThreshold(): Configuration {
    return this._objectiveProgressGreenYellowThreshold$.getValue();
  }

  get objectiveProgressYellowRedThreshold(): Configuration {
    return this._objectiveProgressYellowRedThreshold$.getValue();
  }

  get generalFrontendBaseUrl(): Configuration {
    return this._generalFrontendBaseUrl$.getValue();
  }

  get emailFrom(): Configuration {
    return this._emailFrom$.getValue();
  }

  get emailSubjectNewUser(): Configuration {
    return this._emailSubjectNewUser$.getValue();
  }

  get emailSubjectForgotPassword(): Configuration {
    return this._emailSubjectForgotPassword$.getValue();
  }

  fetchData(): void {
    this.fetchMaxKeyResultsConfiguration();
    this.fetchObjectiveProgressGreenYellowThreshold();
    this.fetchObjectiveProgressYellowRedThreshold();
    this.fetchGeneralFrontendBaseUrl();
    this.fetchEmailFrom();
    this.fetchEmailSubjectNewUser();
    this.fetchEmailSubjectForgotPassword();
  }

  fetchMaxKeyResultsConfiguration(): void {
    this.configurationMapperService.getConfigurationByName$('max-key-results')
      .subscribe((maxKeyResultConfig: Configuration) => {
        this._maxKeyResults$.next(maxKeyResultConfig);
      });
  }

  fetchObjectiveProgressGreenYellowThreshold(): void {
    this.configurationMapperService.getConfigurationByName$('objective-progress-green-yellow-threshold')
      .subscribe((config: Configuration) => {
        this._objectiveProgressGreenYellowThreshold$.next(config);
      });
  }

  fetchObjectiveProgressYellowRedThreshold(): void {
    this.configurationMapperService.getConfigurationByName$('objective-progress-yellow-red-threshold')
      .subscribe((config: Configuration) => {
        this._objectiveProgressYellowRedThreshold$.next(config);
      });
  }

  fetchGeneralFrontendBaseUrl(): void {
    this.configurationMapperService.getConfigurationByName$('general_frontend-base-url')
      .subscribe((config: Configuration) => {
        this._generalFrontendBaseUrl$.next(config);
      });
  }

  fetchEmailFrom(): void {
    this.configurationMapperService.getConfigurationByName$('email_from')
      .subscribe((config: Configuration) => {
        this._emailFrom$.next(config);
      });
  }

  fetchEmailSubjectNewUser(): void {
    this.configurationMapperService.getConfigurationByName$('email_subject_new-user')
      .subscribe((config: Configuration) => {
        this._emailSubjectNewUser$.next(config);
      });
  }

  fetchEmailSubjectForgotPassword(): void {
    this.configurationMapperService.getConfigurationByName$('email_subject_forgot-password')
      .subscribe((config: Configuration) => {
        this._emailSubjectForgotPassword$.next(config);
      });
  }

  updateMaxKeyResults$(maxKeyResults: number): Observable<Configuration> {
    const maxKeyResultsConfig: Configuration = this._maxKeyResults$.getValue();
    maxKeyResultsConfig.value = `${maxKeyResults}`;
    this._maxKeyResults$.next(maxKeyResultsConfig);

    return this.configurationMapperService.putConfiguration$(maxKeyResultsConfig);
  }

  updateObjectiveProgressGreenYellowThreshold(threshold: number): Observable<Configuration> {
    const config: Configuration = this._objectiveProgressGreenYellowThreshold$.getValue();
    config.value = `${threshold}`;
    this._objectiveProgressGreenYellowThreshold$.next(config);

    return this.configurationMapperService.putConfiguration$(config);
  }

  updateObjectiveProgressYellowRedThreshold(threshold: number): Observable<Configuration> {
    const config: Configuration = this._objectiveProgressYellowRedThreshold$.getValue();
    config.value = `${threshold}`;
    this._objectiveProgressYellowRedThreshold$.next(config);

    return this.configurationMapperService.putConfiguration$(config);
  }

  updateGeneralFrontendBaseUrl(url: string): Observable<Configuration> {
    const config: Configuration = this._generalFrontendBaseUrl$.getValue();
    config.value = url;
    this._generalFrontendBaseUrl$.next(config);

    return this.configurationMapperService.putConfiguration$(config);
  }

  updateEmailFrom(email: string): Observable<Configuration> {
    const config: Configuration = this._emailFrom$.getValue();
    config.value = email;
    this._emailFrom$.next(config);

    return this.configurationMapperService.putConfiguration$(config);
  }

  updateEmailSubjectNewUser(subject: string): Observable<Configuration> {
    const config: Configuration = this._emailSubjectNewUser$.getValue();
    config.value = subject;
    this._emailSubjectNewUser$.next(config);

    return this.configurationMapperService.putConfiguration$(config);
  }

  updateEmailSubjectForgotPassword(subject: string): Observable<Configuration> {
    const config: Configuration = this._emailSubjectForgotPassword$.getValue();
    config.value = subject;
    this._emailSubjectForgotPassword$.next(config);

    return this.configurationMapperService.putConfiguration$(config);
  }
}
