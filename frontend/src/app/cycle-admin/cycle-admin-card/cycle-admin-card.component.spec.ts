import { TestBed } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CycleAdminCardComponent } from './cycle-admin-card.component';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { DeleteDialogData } from '../../shared/model/ui/delete-dialog-data';
import { MatDialog } from '@angular/material/dialog';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { CycleMapper } from '../../shared/services/mapper/cycle.mapper';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { TranslateService } from '@ngx-translate/core';
import { DateFormatPipe } from '../../shared/pipes/date-format.pipe';

describe('CycleAdminCardComponent', () => {
  let fixture: any;
  let component: any;
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
        CycleAdminCardComponent,
        DateFormatPipe,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        { provide: MatDialog, useValue: {} },
        { provide: CycleMapper, useValue: {} },
      ],
    })
      .overrideComponent(CycleAdminCardComponent, {})
      .compileComponents();
    fixture = TestBed.createComponent(CycleAdminCardComponent);
    component = fixture.debugElement.componentInstance;
    component.cycle = new CycleUnit(1111, '', [], new Date(111111), new Date(111111), undefined, false);
    translate = TestBed.inject(TranslateService);
  });

  afterEach(() => {
    fixture.destroy();
  });

  it('should run #constructor()', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should return fitting data object #getDataForCycleDeletionDialog without error warning', () => {
    spyOn(translate, 'instant').and.callFake(function(arg) {
      if (arg === 'cycle-admin-card.deletion-dialog.content') {
        return 'correctCycleDeletionContent';
      } else if (arg === 'cycle-admin-card.deletion-dialog.title') {
        return 'correctCycleDeletionTitle';
      }
    });
    const data: { data: DeleteDialogData } = component.getDataForCycleDeletionDialog();

    expect(data)
      .toEqual(
        {
          data:
            {
              title: 'correctCycleDeletionTitle',
              objectNameWithArticle: 'correctCycleDeletionContent',
            },
        },
      );
  });
  it('should return correct string with getCycleTranslation', () => {
    const expected: string = 'correctCycleDeletionTitle';
    spyOn(translate, 'instant').and.returnValue('correctCycleDeletionTitle');

    const actual: string = component.getCycleTranslation();

    expect(actual)
      .toEqual(expected);
  });

  it('should return correct string with getGeneralDeleteDialogContentTranslation', () => {
    const expected: string = 'correctCycleDeletionContent';
    spyOn(translate, 'instant').and.returnValue('correctCycleDeletionContent');

    const actual: string = component.getGeneralDeleteDialogContentTranslation();

    expect(actual)
      .toEqual(expected);
  });
});
