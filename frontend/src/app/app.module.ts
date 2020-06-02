import { BrowserModule } from '@angular/platform-browser';
import { LOCALE_ID, NgModule, TRANSLATIONS, TRANSLATIONS_FORMAT } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { DateAdapter, MAT_DATE_LOCALE } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import localeEn from '@angular/common/locales/en';
import localeEnExtra from '@angular/common/locales/extra/en';
import { loggerConfig } from './config-files/logger-config';

import { OAuthInterceptorService } from './core/auth/services/o-auth-interceptor.service';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { LogoutComponent } from './core/auth/components/logout/logout.component';
import { StructuresModule } from './structures/structures.module';
import { SharedModule } from './shared/shared.module';
import { CycleAdminModule } from './cycle-admin/cycle-admin.module';
import { OAuthModule } from 'angular-oauth2-oidc';
import { ErrorInterceptor } from './core/error/error.interceptor';
import { RedirectComponent } from './core/auth/components/redirect/redirect.component';
import { LoggerModule } from 'ngx-logger';
import {
  MatCardModule,
  MatIconModule,
  MatListModule,
  MatProgressSpinnerModule,
  MatTooltipModule
} from '@angular/material';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { MatButtonModule } from '@angular/material/button';
import { CoreModule } from './core/core.module';
import { AdminViewComponent } from './admin/admin-view.component';
import { AdminUserIdsPipe } from './admin/pipes/admin-user-ids.pipe';
import { ErrorModule } from './core/error/error.module';
import { DeleteDialogComponent } from './shared/components/delete-dialog/delete-dialog.component';
import { AuthenticationService } from './core/auth/services/authentication.service';
import { LocalAuthTypeHandlerService } from './core/auth/services/auth-type-handler/local-auth-type-handler.service';
import { AzureAuthTypeHandlerService } from './core/auth/services/auth-type-handler/azure-auth-type-handler.service';
import { OAuthFrontendDetailsService } from './core/auth/services/o-auth-frontend-details.service';
import { NoMailInformationComponent } from './information/no-mail-information/no-mail-information.component';

// use the require method provided by webpack
declare const require: any;
// we use the webpack raw-loader to return the content as a string
// export const translations: any = require('raw-loader!../locale/messages.en.xlf');

registerLocaleData(localeEn, 'en', localeEnExtra);
registerLocaleData(localeEn, 'de', localeEnExtra);

const currentLanguage: string = 'de';

@NgModule({
  declarations: [
    AdminViewComponent,
    AppComponent,
    LogoutComponent,
    RedirectComponent,
    AdminUserIdsPipe,
    NoMailInformationComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserAnimationsModule,
    BrowserModule,
    CoreModule,
    CycleAdminModule,
    FormsModule,
    HttpClientModule,
    LoggerModule.forRoot(loggerConfig),
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatListModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    OAuthModule.forRoot(),
    ReactiveFormsModule,
    SharedModule,
    StructuresModule,
    ErrorModule,
    LoggerModule.forRoot(loggerConfig),
  ],
  entryComponents: [
    DeleteDialogComponent,
    LogoutComponent,
  ],
  providers: [
    OAuthFrontendDetailsService,

    AuthenticationService,
    LocalAuthTypeHandlerService,
    AzureAuthTypeHandlerService,

    {provide: MAT_DATE_LOCALE, useValue: 'de-DE'},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: OAuthInterceptorService, multi: true},
    {
      provide: TRANSLATIONS,
      useFactory: locale => {
        return require(`raw-loader!../locale/messages.${locale}.xlf`);
      },
      deps: [LOCALE_ID]
    },
    {
      provide: LOCALE_ID,
      useValue: currentLanguage
    },
    {provide: TRANSLATIONS_FORMAT, useValue: 'xlf'},
    I18n
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
