import { TestBed } from '@angular/core/testing';
import { CompanyApiService } from '../../shared/services/api/company-api.service';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { StructureCardComponent } from './structure-card.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { CycleMapper } from '../../shared/services/mapper/cycle.mapper';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { MatDialog } from '@angular/material';
import { CurrentUserService } from '../../core/services/current-user.service';
import { RouterTestingModule } from '@angular/router/testing';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { DeleteDialogData } from '../../shared/model/ui/delete-dialog-data';
import { i18nMock } from '../../shared/mocks/i18n-mock';

describe('StructureCardComponent', () => {

  let fixture: any;
  let component: {
    getDataForCompanyDeletionDialog: () => {data: DeleteDialogData},
    company: CompanyUnit,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ReactiveFormsModule,
        FormsModule,
        RouterTestingModule,
      ],
      declarations: [
        StructureCardComponent,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        {provide: CycleMapper, useValue: {}},
        {provide: CompanyMapper, useValue: {}},
        {provide: MatDialog, useValue: {}},
        {provide: CompanyApiService, useValue: {}},
        {provide: RouterTestingModule, useValue: {}},
        {provide: CurrentUserService, useValue: {}},
        {provide: I18n, useValue: i18nMock},
      ]
    })
      .overrideComponent(StructureCardComponent, {})
      .compileComponents();
    fixture = TestBed.createComponent(StructureCardComponent);
    component = fixture.debugElement.componentInstance;
  });

  afterEach(() => {
    fixture.destroy();
  });

  it('should run #constructor()', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should return fitting data object #getDataForCompanyDeletionDialog without error warning', () => {
    component.company = new CompanyUnit(7331, '', [], [], 0, '');

    const data: {data: DeleteDialogData} = component.getDataForCompanyDeletionDialog();

    expect(data)
      .toEqual(
        {
          data:
            {
              title: 'correctGeneralDeleteDialogTitle',
              objectNameWithArticle: 'correctCompanyWithArticle',
              dangerContent: ''
            }
        }
      );
  });

  it('should return fitting data object #getDataForCompanyDeletionDialog with error warning', () => {
    component.company = new CompanyUnit(7331, '', [123, 234, 345, 45], [], 0, '');

    const data: {data: DeleteDialogData} = component.getDataForCompanyDeletionDialog();

    expect(data)
      .toEqual(
        {
          data:
            {
              title: 'correctGeneralDeleteDialogTitle',
              objectNameWithArticle: 'correctCompanyWithArticle',
              dangerContent: 'correctDeleteCompanyHasSubstructuresWarning'
            }
        }
      );
  });
});
