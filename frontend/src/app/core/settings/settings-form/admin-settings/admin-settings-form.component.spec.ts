import { AdminSettingsFormComponent } from './admin-settings-form.component';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConfigurationManagerService } from '../../configuration-manager.service';
import { CurrentUserService } from '../../../services/current-user.service';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { OAuthFrontendDetailsService } from '../../../auth/services/o-auth-frontend-details.service';
import { of } from 'rxjs';
import { Configuration } from '../../../../shared/model/ui/configuration';
import { AbstractControl } from '@angular/forms';
import { MaterialTestingModule } from '../../../../testing/material-testing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

const configurationManagerServiceStub: any = {
  getAllConfigurations$: jest.fn(),
  updateConfigurations$: jest.fn()
};

const currentUserServiceStub: any = {
  isCurrentUserAdmin$: jest.fn(),
};

const oAuthFrontendDetailsServiceStub: any = {
  getAuthType$: jest.fn(),
  isAzureAuthType$: jest.fn()
};

const dialog: any = {
  open: jest.fn()
};

const configuration: Configuration = {
  id: 0,
  name: 'config_name',
  value: 'true',
  type: 'text'
};

let configurationCheckBox: Configuration;

describe('AdminSettingsForm', () => {

  let fixture: ComponentFixture<AdminSettingsFormComponent>;
  let component: AdminSettingsFormComponent;

  beforeEach(() => {
    configurationCheckBox = {
      id: 1,
      name: 'topic-sponsors-activated',
      value: 'true',
      type: 'checkbox'
    };

    configurationManagerServiceStub.getAllConfigurations$.mockReset();
    configurationManagerServiceStub.updateConfigurations$.mockReset();
    currentUserServiceStub.isCurrentUserAdmin$.mockReset();
    oAuthFrontendDetailsServiceStub.isAzureAuthType$.mockReset();
    oAuthFrontendDetailsServiceStub.getAuthType$.mockReset();
    dialog.open.mockReset();

    configurationManagerServiceStub.getAllConfigurations$.mockReturnValue(of([configuration, configurationCheckBox]));
    configurationManagerServiceStub.updateConfigurations$.mockReturnValue(of(null));
    currentUserServiceStub.isCurrentUserAdmin$.mockReturnValue(of(true));
    oAuthFrontendDetailsServiceStub.isAzureAuthType$.mockReturnValue(of('false'));
    oAuthFrontendDetailsServiceStub.getAuthType$.mockReturnValue(of('local'));
    /* eslint-disable  */
    dialog.open.mockReturnValue({ afterClosed: jest.fn()
        .mockReturnValue(of(true)) });
    /* eslint-enable */

    TestBed.configureTestingModule(
      {
        declarations: [AdminSettingsFormComponent],
        imports: [MaterialTestingModule, BrowserAnimationsModule],
        providers: [
          {provide: ConfigurationManagerService, useValue: configurationManagerServiceStub},
          {provide: CurrentUserService, useValue: currentUserServiceStub},
          {provide: MatDialogRef, useValue: {}},
          {provide: OAuthFrontendDetailsService, useValue: oAuthFrontendDetailsServiceStub},
          {provide: MatDialog, useValue: dialog},
        ],
        schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      })
      .compileComponents();
    fixture = TestBed.createComponent(AdminSettingsFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    fixture.destroy();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should create adminSettingsForm on init', done => {
    setTimeout(() => {
      expect(component.settings)
        .toBeTruthy();
      done();
    }, 1000); // wait a second for the getAllConfigurations$ Observable to emit.
  });

  it('adminSettingsForm should have right length', done => {
    setTimeout(() => {
      expect(component.settings.length)
        .toBe(2);
      done();
    }, 1000); // wait a second for the getAllConfigurations$ Observable to emit.
  });

  it('adminSettingsForm should have right formGroup', done => {
    setTimeout(() => {
      const formGroup: AbstractControl = component.settings.at(0);

      expect(formGroup.get('id').value)
        .toBe(configuration.id);
      expect(formGroup.get('name').value)
        .toBe(configuration.name);
      expect(formGroup.get('value').value)
        .toBe(configuration.value);
      expect(formGroup.get('type').value)
        .toBe(configuration.type);
      done();
    }, 1000); // wait a second for the getAllConfigurations$ Observable to emit.
  });

  it('adminSettingsForm should map strings to boolean when using a checkbox', done => {
    setTimeout(() => {
      const formGroup: AbstractControl = component.settings.at(1);

      expect(formGroup.get('value').value)
        .toBe(true);
      expect(formGroup.get('value').value)
        .not
        .toBe('true');
      done();
    }, 1000); // wait a second for the getAllConfigurations$ Observable to emit.
  });

  it('adminSettingsForm should not map strings to boolean when not using a checkbox', done => {
    setTimeout(() => {
      const formGroup: AbstractControl = component.settings.at(0);

      expect(formGroup.get('value').value)
        .toBe('true');
      expect(formGroup.get('value').value)
        .not
        .toBe(true);
      done();
    }, 1000); // wait a second for the getAllConfigurations$ Observable to emit.
  });

  it('createUpdate$ should call updateConfigurations$', done => {
    component.createUpdate$()
      .subscribe(() => {
        configurationCheckBox.value = true;
        expect(configurationManagerServiceStub.updateConfigurations$)
          .toHaveBeenCalledWith([configuration, configurationCheckBox]);
        done();
      });
  });

  it('createUpdate$ should not call updateConfigurations$ when user is not admin', done => {
    currentUserServiceStub.isCurrentUserAdmin$.mockReturnValue(of(false));

    component.createUpdate$()
      .subscribe(() => {
        expect(configurationManagerServiceStub.updateConfigurations$)
          .toHaveBeenCalledTimes(0);
        done();
      });
  });

  it('canClose$ opens warning dialog, when topic_sponsors_activated is set to false', done => {
    component.settings.at(1)
      .get('value')
      .setValue(false);
    component.settings.at(1)
      .markAsDirty();

    component.canClose$()
      .subscribe(() => {
        expect(dialog.open)
          .toHaveBeenCalled();
        done();
      });
  });

  it('canClose$ does not open warning dialog, when topic_sponsors_activated is set to true', done => {
    component.canClose$()
      .subscribe(() => {
        expect(dialog.open)
          .toHaveBeenCalledTimes(0);
        done();
      });
  });

  it('canClose$ does not open warning dialog, when topic_sponsors_activated is set to false but pristine', done => {
    component.settings.at(1)
      .get('value')
      .setValue(false);
    component.settings.at(1)
      .markAsPristine();

    component.canClose$()
      .subscribe(() => {
        expect(dialog.open)
          .toHaveBeenCalledTimes(0);
        done();
      });
  });
});
