import { TestBed } from '@angular/core/testing';
import { CompanyApiService } from '../../shared/services/api/company-api.service';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { OkrUnitCardComponent } from './okr-unit-card.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CycleMapper } from '../../shared/services/mapper/cycle.mapper';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { MatDialog } from '@angular/material/dialog';
import { CurrentUserService } from '../../core/services/current-user.service';
import { RouterTestingModule } from '@angular/router/testing';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { DeleteDialogData } from '../../shared/model/ui/delete-dialog-data';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { TranslateService } from '@ngx-translate/core';
import { DateFormatPipe } from '../../shared/pipes/date-format.pipe';

describe('OkrUnitCardComponent', () => {

  let fixture: any;
  let component: {
    getDataForCompanyDeletionDialog: () => { data: DeleteDialogData };
    company: CompanyUnit;
  };
  let translate: TranslateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ReactiveFormsModule,
        FormsModule,
        RouterTestingModule,
        MaterialTestingModule,
      ],
      declarations: [
        OkrUnitCardComponent,
        DateFormatPipe,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        { provide: CycleMapper, useValue: {} },
        { provide: CompanyMapper, useValue: {} },
        { provide: MatDialog, useValue: {} },
        { provide: CompanyApiService, useValue: {} },
        { provide: RouterTestingModule, useValue: {} },
        { provide: CurrentUserService, useValue: {} },
      ],
    })
      .overrideComponent(OkrUnitCardComponent, {})
      .compileComponents();
    fixture = TestBed.createComponent(OkrUnitCardComponent);
    component = fixture.debugElement.componentInstance;
    translate = TestBed.inject(TranslateService);
  });

  afterEach(() => {
    fixture.destroy();
  });

  it('should run #constructor()', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should return fitting data object #getDataForCompanyDeletionDialog without error warning', () => {
    component.company = new CompanyUnit(7331, 'correctCompanyWithArticle', [], [], 0, '');
    spyOn(translate, 'instant').and.callFake(function(arg) {
      if (arg === 'okr-unit-card.label') {
        return 'correctGeneralDeleteDialogTitle';
      } else if (arg === 'okr-unit-card.general-delete-dialog-title') {
        return 'correctCompanyWithArticle';
      } else if (arg === 'okr-unit-card.delete-company-has-child-unit-warning') {
        return '';
      }
    });
    const data: { data: DeleteDialogData } = component.getDataForCompanyDeletionDialog();

    expect(data)
      .toEqual(
        {
          data:
            {
              title: 'correctGeneralDeleteDialogTitle',
              objectNameWithArticle: 'correctCompanyWithArticle',
              dangerContent: '',
            },
        },
      );
  });

  it('should return fitting data object #getDataForCompanyDeletionDialog with error warning', () => {
    component.company = new CompanyUnit(7331, 'correctCompanyWithArticle', [123, 234, 345, 45], [], 0, '');
    spyOn(translate, 'instant').and.callFake(function(arg) {
      if (arg === 'okr-unit-card.label') {
        return 'correctGeneralDeleteDialogTitle';
      } else if (arg === 'okr-unit-card.general-delete-dialog-title') {
        return 'correctCompanyWithArticle';
      } else if (arg === 'okr-unit-card.delete-company-has-child-unit-warning') {
        return 'correctDeleteCompanyHasChildUnitWarning';
      }
    });
    const data: { data: DeleteDialogData } = component.getDataForCompanyDeletionDialog();

    expect(data)
      .toEqual(
        {
          data:
            {
              title: 'correctGeneralDeleteDialogTitle',
              objectNameWithArticle: 'correctCompanyWithArticle',
              dangerContent: 'correctDeleteCompanyHasChildUnitWarning',
            },
        },
      );
  });
});
