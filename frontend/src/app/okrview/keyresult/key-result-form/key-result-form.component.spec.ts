import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { KeyResultFormComponent } from './key-result-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { KeyResultMilestoneFormComponent } from './key-result-milestone-form/key-result-milestone-form.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { KeyResultMapper } from '../../../shared/services/mapper/key-result.mapper';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ViewKeyResult } from '../../../shared/model/ui/view-key-result';
import { Unit } from '../../../shared/model/api/unit.enum';
import { TranslateService } from '@ngx-translate/core';
import { SharedModule } from '../../../shared/shared.module';

const matDialogRefMock: any = {
  close: jest.fn(),
};

const keyResultMapperMock: any = {
  postKeyResult$: jest.fn(),
  putKeyResult$: jest.fn(),
};

const matDialogDataMock: any = {};

let keyResult: ViewKeyResult;

describe('KeyResultFormComponent', () => {
  let component: KeyResultFormComponent;
  let fixture: ComponentFixture<KeyResultFormComponent>;
  let translate: TranslateService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        KeyResultFormComponent,
        KeyResultMilestoneFormComponent,
      ],
      imports: [FormsModule, ReactiveFormsModule, MaterialTestingModule, NoopAnimationsModule, SharedModule],
      providers: [
        { provide: MatDialogRef, useValue: matDialogRefMock },
        { provide: KeyResultMapper, useValue: keyResultMapperMock },
        { provide: MAT_DIALOG_DATA, useValue: matDialogDataMock },
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    keyResult = new ViewKeyResult(
      1,
      0,
      10,
      20,
      Unit.NUMBER,
      'title',
      'desc',
      0,
      [],
      [],
    );

    matDialogRefMock.close.mockReset();
    keyResultMapperMock.postKeyResult$.mockReset();
    keyResultMapperMock.putKeyResult$.mockReset();
  });

  it('should create', () => {
    fixture = TestBed.createComponent(KeyResultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component)
      .toBeTruthy();
  });

  it('should create keyResultForm', () => {
    fixture = TestBed.createComponent(KeyResultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.keyResultForm)
      .toBeTruthy();
    expect(component.keyResultForm.get('keyResult'))
      .toBeTruthy();
    expect(component.keyResultForm.get('current'))
      .toBeTruthy();
    expect(component.keyResultForm.get('end'))
      .toBeTruthy();
    expect(component.keyResultForm.get('start'))
      .toBeTruthy();
    expect(component.keyResultForm.get('unit'))
      .toBeTruthy();
    expect(component.keyResultForm.get('description'))
      .toBeTruthy();
    expect(component.keyResultForm.get('viewKeyResultMilestones'))
      .toBeTruthy();
  });

  it('should not set keyResult, when no keyResult given in formData', () => {
    fixture = TestBed.createComponent(KeyResultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.keyResult)
      .toBeFalsy();
  });

  it('should set keyResult, when keyResult given in formData', () => {
    TestBed.overrideProvider(MAT_DIALOG_DATA, { useValue: { keyResult } });

    fixture = TestBed.createComponent(KeyResultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.keyResult)
      .toBeTruthy();
    expect(component.keyResult)
      .toBe(keyResult);
  });

  it('should patch form value, when keyResult given in formData', () => {
    TestBed.overrideProvider(MAT_DIALOG_DATA, { useValue: { keyResult } });

    fixture = TestBed.createComponent(KeyResultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.keyResultForm)
      .toBeTruthy();
    expect(component.keyResultForm.get('keyResult').value)
      .toBe(keyResult.keyResult);
    expect(component.keyResultForm.get('current').value)
      .toBe(keyResult.current);
    expect(component.keyResultForm.get('end').value)
      .toBe(keyResult.end);
    expect(component.keyResultForm.get('start').value)
      .toBe(keyResult.start);
    expect(component.keyResultForm.get('unit').value)
      .toBe(keyResult.unit);
    expect(component.keyResultForm.get('description').value)
      .toBe(keyResult.description);
    expect(component.keyResultForm.get('viewKeyResultMilestones').value)
      .toEqual(keyResult.viewKeyResultMilestones);
  });

  it('getViewUnitKeys returns keys of Unit enum', () => {
    fixture = TestBed.createComponent(KeyResultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.getViewUnitKeys())
      .toEqual(Object.keys(Unit));
  });

  it('getViewUnit returns € for EURO', () => {
    fixture = TestBed.createComponent(KeyResultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.getViewUnit('EURO'))
      .toBe('€');
  });

  it('getViewUnit returns % for PERCENT', () => {
    fixture = TestBed.createComponent(KeyResultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.getViewUnit('PERCENT'))
      .toBe('%');
  });

  it('getViewUnit returns translation for NUMBER', () => {
    fixture = TestBed.createComponent(KeyResultFormComponent);
    component = fixture.componentInstance;
    translate = TestBed.inject(TranslateService);
    spyOn(translate, 'instant').and.returnValue('translated string');
    fixture.detectChanges();

    expect(component.getViewUnit('NUMBER'))
      .toBe('translated string');
  });

  it('getViewUnit returns undefined for non existing string', () => {
    fixture = TestBed.createComponent(KeyResultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.getViewUnit('NOT_EXISTING'))
      .toBeUndefined();
  });

  it('getViewUnit returns undefined for null', () => {
    fixture = TestBed.createComponent(KeyResultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.getViewUnit(null))
      .toBeUndefined();
  });

  it('saveKeyResult posts keyresult when keyresult in formdata is null', () => {
    fixture = TestBed.createComponent(KeyResultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.keyResultForm.get('keyResult')
      .setValue('test');
    component.saveKeyResult();

    expect(keyResultMapperMock.postKeyResult$)
      .toHaveBeenCalled();
    expect(matDialogRefMock.close)
      .toHaveBeenCalled();
  });

  it('saveKeyResult puts keyresult when keyresult in formdata is given', () => {
    TestBed.overrideProvider(MAT_DIALOG_DATA, { useValue: { keyResult } });

    fixture = TestBed.createComponent(KeyResultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.keyResultForm.get('keyResult')
      .setValue('test');
    component.saveKeyResult();

    expect(keyResultMapperMock.putKeyResult$)
      .toHaveBeenCalled();
    expect(matDialogRefMock.close)
      .toHaveBeenCalled();
  });
});
