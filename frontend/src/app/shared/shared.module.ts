import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { ConfirmationDialogComponent } from './components/confirmation-dialog/confirmation-dialog.component';
import { UserAutocompleteInputComponent } from './components/user-autocomplete-input/user-autocomplete-input.component';
import { UserMinibuttonComponent } from './components/user-minibutton/user-minibutton.component';
import { LoadingSpinnerComponent } from './components/loading-spinner/loading-spinner.component';
import { MatSnackBarModule } from '@angular/material';
import { DeleteDialogComponent } from './components/delete-dialog/delete-dialog.component';
import { DialogComponent } from './components/dialog-component/dialog.component';
import { UserAvatarComponent } from './components/user-avatar/user-avatar.component';
import { AvatarModule } from 'ngx-avatar';
import { RouterModule } from '@angular/router';
import { OkrToolbarBareComponent } from './components/okr-toolbar-bare/okr-toolbar-bare.component';
import { OkrToolbarComponent } from './components/okr-toolbar/okr-toolbar.component';
import { CapsLockDirective } from './directives/caps-lock.directive';
import { NonLoggedInCardComponent } from './components/non-logged-in-card/non-logged-in-card.component';
import { FormErrorComponent } from './components/form-error/form-error.component';
import { MilestoneSliderWrapperComponent } from './components/milestone-slider-wrapper/milestone-slider-wrapper.component';
import { StatusDotComponent } from './components/status-dot/status-dot.component';
import { CallbackFilterPipe } from './pipes/callback-filter.pipe';
import { UserSelectorMultiComponent } from './components/user-selector-multi/user-selector-multi.component';

@NgModule({
  declarations: [
    UserAutocompleteInputComponent,
    ConfirmationDialogComponent,
    UserMinibuttonComponent,
    LoadingSpinnerComponent,
    DeleteDialogComponent,
    DialogComponent,
    UserAvatarComponent,
    OkrToolbarBareComponent,
    OkrToolbarComponent,
    CapsLockDirective,
    NonLoggedInCardComponent,
    FormErrorComponent,
    MilestoneSliderWrapperComponent,
    StatusDotComponent,
    CallbackFilterPipe,
    UserSelectorMultiComponent,
  ],
  entryComponents: [
    ConfirmationDialogComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    MatIconModule,
    MatSelectModule,
    MatButtonModule,
    MatDialogModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    MatTooltipModule,
    MatMenuModule,
    MatCardModule,
    MatToolbarModule,
    MatSnackBarModule,
    AvatarModule,
    RouterModule,
    NgxMatSelectSearchModule
  ],
    exports: [
        UserAutocompleteInputComponent,
        UserMinibuttonComponent,
        LoadingSpinnerComponent,
        MatButtonModule,
        DialogComponent,
        UserAvatarComponent,
        OkrToolbarBareComponent,
        OkrToolbarComponent,
        CapsLockDirective,
        NonLoggedInCardComponent,
        FormErrorComponent,
        MilestoneSliderWrapperComponent,
        StatusDotComponent,
        CallbackFilterPipe,
        UserSelectorMultiComponent
    ],
})
export class SharedModule {
}
