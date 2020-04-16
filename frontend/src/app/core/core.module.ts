import { NgModule, Optional, SkipSelf } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TrackByPropertyPipe } from '../admin/pipes/track-by-property.pipe';
import { FeedbackFormComponent } from './feedback/feedback-form/feedback-form.component';
import { AdminSettingsFormComponent } from './settings/admin-settings/admin-settings-form.component';
import { VersionFormComponent } from './version-form/version-form.component';
import { SharedModule } from '../shared/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import {
  MatCardModule,
  MatDialogModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatRippleModule,
  MatSelectModule,
  MatTabsModule
} from '@angular/material';
import { FeedbackButtonComponent } from './feedback/feedback-button/feedback-button.component';
import { FlexModule } from '@angular/flex-layout';
import { RouterModule } from '@angular/router';
import { LocalAuthModule } from './auth/local-auth/local-auth.module';
import { LandingPageNavigationComponent } from './landing-page-router/landing-page-navigation.component';

@NgModule({
  declarations: [
    FeedbackButtonComponent,
    FeedbackFormComponent,
    AdminSettingsFormComponent,
    TrackByPropertyPipe,
    VersionFormComponent,
    LandingPageNavigationComponent,
  ],
  exports: [
    TrackByPropertyPipe,
    VersionFormComponent,
    FeedbackButtonComponent,
  ],
  entryComponents: [
    FeedbackFormComponent,
    VersionFormComponent,
    AdminSettingsFormComponent,
  ],
  imports: [
    MatInputModule,
    MatIconModule,
    CommonModule,
    SharedModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatDialogModule,
    MatTabsModule,
    MatSelectModule,
    MatCardModule,
    FlexModule,
    RouterModule,
    MatRippleModule,
    LocalAuthModule
  ]
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error('CoreModule has already been loaded. You should only import Core modules in the AppModule only.');
    }
  }
}
