import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSettingsComponent } from './user-settings.component';
import { MaterialTestingModule } from '../../../../testing/material-testing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CompanyMapper } from '../../../../shared/services/mapper/company.mapper';
import { UserSettingsManagerService } from '../../../services/user-settings-manager.service';
import { DepartmentMapper } from '../../../../shared/services/mapper/department.mapper';
import { of } from 'rxjs';
import { UserSettings } from '../../../../shared/model/ui/user-settings';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

const companyService: any = {
  getActiveCompanies$: jest.fn()
};

const userSettingsManager: any = {
  getUserSettings$: jest.fn()
};

const departmentService: any = {};

const userSettings: UserSettings = {
  defaultCompanyId: 0,
  defaultTeamId: 0,
  id: 1,
  userId: ''
};

describe('UserSettingsComponent', () => {
  let component: UserSettingsComponent;
  let fixture: ComponentFixture<UserSettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserSettingsComponent ],
      imports: [ MaterialTestingModule, FormsModule, ReactiveFormsModule, BrowserAnimationsModule ],
      providers: [
        { provide: CompanyMapper, useValue: companyService },
        { provide: UserSettingsManagerService, useValue: userSettingsManager },
        { provide: DepartmentMapper, useValue: departmentService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    userSettingsManager.getUserSettings$.mockReset();
    userSettingsManager.getUserSettings$.mockReturnValue(of(userSettings));
    companyService.getActiveCompanies$.mockReset();
    companyService.getActiveCompanies$.mockReturnValue(of(null));

    fixture = TestBed.createComponent(UserSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
