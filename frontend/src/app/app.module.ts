import 'reflect-metadata';
import { registerLocaleData } from '@angular/common';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import localeDe from '@angular/common/locales/de';
import localeEn from '@angular/common/locales/en';
import localeDeExtra from '@angular/common/locales/extra/de';
import localeEnExtra from '@angular/common/locales/extra/en';
import { LOCALE_ID, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
  MAT_MOMENT_DATE_ADAPTER_OPTIONS,
  MAT_MOMENT_DATE_FORMATS,
  MomentDateAdapter,
} from '@angular/material-moment-adapter';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTreeModule } from '@angular/material/tree';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { OAuthModule } from 'angular-oauth2-oidc';
import { LoggerModule } from 'ngx-logger';
import { NgwWowModule } from 'ngx-wow';
import { AdminViewComponent } from './admin/admin-view.component';
import { AdminUserIdsPipe } from './admin/pipes/admin-user-ids.pipe';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { loggerConfig } from './config-files/logger-config';
import { AzureAuthTypeHandlerService } from './core/auth/services/auth-type-handler/azure-auth-type-handler.service';
import { LocalAuthTypeHandlerService } from './core/auth/services/auth-type-handler/local-auth-type-handler.service';
import { AuthenticationService } from './core/auth/services/authentication.service';
import { OAuthFrontendDetailsService } from './core/auth/services/o-auth-frontend-details.service';
import { OAuthInterceptorService } from './core/auth/services/o-auth-interceptor.service';
import { CoreModule } from './core/core.module';
import { ErrorInterceptor } from './core/error/error.interceptor';
import { ErrorModule } from './core/error/error.module';
import { CycleAdminModule } from './cycle-admin/cycle-admin.module';
import { DashboardModule } from './dashboard/dashboard.module';
import { DemoModule } from './demo/demo.module';
import { NoMailInformationComponent } from './information/no-mail-information/no-mail-information.component';
import { OkrUnitModule } from './okr-units/okr-unit.module';
import { OkrviewModule } from './okrview/okrview.module';
import { SharedModule } from './shared/shared.module';
import { TopicDraftsModule } from './topic-drafts/topic-drafts.module';
import { AvatarModule } from 'ngx-avatars';

registerLocaleData(localeEn, 'en', localeEnExtra);
registerLocaleData(localeDe, 'de', localeDeExtra);

const currentLanguage: string = 'de';

// (window as any).retain = [ CustomChartComponent ];

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

// @ts-ignore
@NgModule({
  declarations: [
    AdminViewComponent,
    AppComponent,
    AdminUserIdsPipe,
    NoMailInformationComponent,
  ],
  imports: [
    AppRoutingModule,
    AvatarModule,
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
    TopicDraftsModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient],
      },
      defaultLanguage: 'de',
    }),
    MatTreeModule,
    DashboardModule,
  ],
  providers: [
    OAuthFrontendDetailsService,

    AuthenticationService,
    LocalAuthTypeHandlerService,
    AzureAuthTypeHandlerService,

    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: OAuthInterceptorService, multi: true },
    {
      provide: LOCALE_ID,
      useValue: currentLanguage,
    },
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS],
    },
    { provide: MAT_DATE_FORMATS, useValue: MAT_MOMENT_DATE_FORMATS },

  ],
  bootstrap: [AppComponent],
  // entryComponents: [
  //   CustomChartComponent
  // ]
})

export class AppModule {
}
