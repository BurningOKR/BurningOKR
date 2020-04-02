import { TestBed } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CycleAdminCardComponent } from './cycle-admin-card.component';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { i18nMock } from '../../shared/mocks/i18n-mock';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { DeleteDialogData } from '../../shared/model/ui/delete-dialog-data';
import { MatDialog } from '@angular/material/dialog';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { CycleMapper } from '../../shared/services/mapper/cycle.mapper';

describe('CycleAdminCardComponent', () => {
  let fixture: any;
  let component: any;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ReactiveFormsModule,
        FormsModule,
        RouterTestingModule,
      ],
      declarations: [CycleAdminCardComponent],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
      providers: [
        { provide: I18n, useValue: i18nMock},
        { provide: MatDialog, useValue: {}},
        { provide: CycleMapper, useValue: {}}
      ]
    })
      .overrideComponent(CycleAdminCardComponent, {})
      .compileComponents();
    fixture = TestBed.createComponent(CycleAdminCardComponent);
    component = fixture.debugElement.componentInstance;
    component.cycle = new CycleUnit(1111, '', [], new Date(111111), new Date(111111), undefined, false);
  });

  afterEach(() => {
    fixture.destroy();
  });

  it('should run #constructor()', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should return fitting data object #getDataForCycleDeletionDialog without error warning', () => {
    const data: {data: DeleteDialogData} = component.getDataForCycleDeletionDialog();

    expect(data)
      .toEqual(
        {
          data:
            {
              title: 'correctCycleDeletionTitle',
              objectNameWithArticle: 'correctCycleDeletionContent',
            }
        }
      );
  });
  it('should return correct string with getCycleTranslation', () => {
    const expected: string = 'correctCycleDeletionTitle';

    const actual: string = component.getCycleTranslation();

    expect(actual)
      .toEqual(expected);
  });

  it('should return correct string with getGeneralDeleteDialogContentTranslation', () => {
    const expected: string = 'correctCycleDeletionContent';

    const actual: string = component.getGeneralDeleteDialogContentTranslation();

    expect(actual)
      .toEqual(expected);
  });
});
