import { BrowserModule } from '@angular/platform-browser';
import { LOCALE_ID, NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import localeEn from '@angular/common/locales/en';
import localeEnExtra from '@angular/common/locales/extra/en';
import { loggerConfig } from './config-files/logger-config';

import { OAuthInterceptorService } from './core/auth/services/o-auth-interceptor.service';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { OkrUnitModule } from './okr-units/okr-unit.module';
import { SharedModule } from './shared/shared.module';
import { CycleAdminModule } from './cycle-admin/cycle-admin.module';
import { OAuthModule } from 'angular-oauth2-oidc';
import { ErrorInterceptor } from './core/error/error.interceptor';
import { LoggerModule } from 'ngx-logger';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatButtonModule } from '@angular/material/button';
import { CoreModule } from './core/core.module';
import { AdminViewComponent } from './admin/admin-view.component';
import { AdminUserIdsPipe } from './admin/pipes/admin-user-ids.pipe';
import { ErrorModule } from './core/error/error.module';
import { AuthenticationService } from './core/auth/services/authentication.service';
import { LocalAuthTypeHandlerService } from './core/auth/services/auth-type-handler/local-auth-type-handler.service';
import { AzureAuthTypeHandlerService } from './core/auth/services/auth-type-handler/azure-auth-type-handler.service';
import { OAuthFrontendDetailsService } from './core/auth/services/o-auth-frontend-details.service';
import { NoMailInformationComponent } from './information/no-mail-information/no-mail-information.component';
import { SubmittedTopicDraftsComponent } from './submitted-topic-drafts/submitted-topic-drafts.component';
import { MatTableModule } from '@angular/material/table';
import { SubmittedTopicDraftActionButtonComponent } from './submitted-topic-drafts/submitted-topic-draft-action-button/submitted-topic-draft-action-button.component';
import { SubmittedTopicDraftCardComponent } from './submitted-topic-drafts/submitted-topic-draft-card/submitted-topic-draft-card.component';
import { MatGridListModule } from '@angular/material/grid-list';
import { SubmittedTopicDraftCardsWrapperComponent } from './submitted-topic-drafts/submitted-topic-draft-cards-wrapper/submitted-topic-draft-cards-wrapper.component';
import { SubmittedTopicDraftDetailsComponent } from './submitted-topic-drafts/submitted-topic-draft-details/submitted-topic-draft-details.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { SubmittedTopicDraftEditComponent } from './submitted-topic-drafts/submitted-topic-draft-edit/submitted-topic-draft-edit.component';
import { OkrviewModule } from './okrview/okrview.module';
import { NgwWowModule } from 'ngx-wow';
import { DemoModule } from './demo/demo.module';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { ConvertSubmittedTopicDraftToTeam } from './submitted-topic-drafts/submitted-topic-drafts-convert-to-team/convert-submitted-topic-draft-to-team.component';
import {MatTreeModule} from "@angular/material/tree";

// use the require method provided by webpack
declare const require: any;
// we use the webpack raw-loader to return the content as a string
// export const translations: any = require('raw-loader!../locale/messages.en.xlf');

registerLocaleData(localeEn, 'en', localeEnExtra);
registerLocaleData(localeEn, 'de', localeEnExtra);

const currentLanguage: string = 'de';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AdminViewComponent,
    AppComponent,
    AdminUserIdsPipe,
    NoMailInformationComponent,
    SubmittedTopicDraftsComponent,
    SubmittedTopicDraftCardComponent,
    SubmittedTopicDraftActionButtonComponent,
    SubmittedTopicDraftCardsWrapperComponent,
    SubmittedTopicDraftDetailsComponent,
    SubmittedTopicDraftEditComponent,
    ConvertSubmittedTopicDraftToTeam,
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
        OkrUnitModule,
        ErrorModule,
        LoggerModule.forRoot(loggerConfig),
        MatTableModule,
        MatMenuModule,
        MatGridListModule,
        MatExpansionModule,
        MatDialogModule,
        MatSelectModule,
        MatInputModule,
        MatDatepickerModule,
        OkrviewModule,
        NgwWowModule,
        DemoModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: (createTranslateLoader),
                deps: [HttpClient]
            },
            defaultLanguage: 'de'
        }),
        MatTreeModule
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
      provide: LOCALE_ID,
      useValue: currentLanguage
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
