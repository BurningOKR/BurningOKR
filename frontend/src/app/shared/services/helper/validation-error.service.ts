import { AbstractControl, FormControl } from '@angular/forms';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { AbstractValidator, getValidators } from '../../validators/abstract-validator';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ValidationErrorService implements OnInit, OnDestroy{

  subscriptions: Subscription[] = [];
  private defaultErrorMessage: string;

  constructor(private i18n: I18n, private translate: TranslateService) {
  }

  ngOnInit(): void {
    this.subscriptions.push(this.translate.stream('validation-error.default-error-message').subscribe(text => {
      this.defaultErrorMessage = text;
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  getErrorMessage(control: AbstractControl): string {
    const validator: AbstractValidator = getValidators(this.i18n)
      .find(val => control.hasError(val.getErrorCode()));
    if (validator) {
      return validator.getErrorMessage();
    } else {
      return this.defaultErrorMessage;
    }
  }
}
