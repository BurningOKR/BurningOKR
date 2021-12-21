import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { StatusDotComponent } from './status-dot.component';
import { status } from '../../model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { TranslateService } from '@ngx-translate/core';

describe('StatusDotComponent', () => {
  let component: StatusDotComponent;
  let fixture: ComponentFixture<StatusDotComponent>;
  let translate: TranslateService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [StatusDotComponent],
      imports: [MaterialTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatusDotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    translate = TestBed.inject(TranslateService);
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should return correct string for tooltip submitted', () => {
    const expected: string = 'correctStatusTooltipSubmitted';
    spyOn(translate, 'instant').and.returnValue('correctStatusTooltipSubmitted')

    const actual: string = component.geti18nTooltip(status.submitted);

    expect(actual)
        .toEqual(expected);
  });

  it('should return correct string for tooltip approved', () => {
    const expected: string = 'correctStatusTooltipApproved';
    spyOn(translate, 'instant').and.returnValue('correctStatusTooltipApproved')

    const actual: string = component.geti18nTooltip(status.approved);

    expect(actual)
        .toEqual(expected);
  });

  it('should return correct string for tooltip rejected', () => {
    const expected: string = 'correctStatusTooltipRejected';
    spyOn(translate, 'instant').and.returnValue('correctStatusTooltipRejected')

    const actual: string = component.geti18nTooltip(status.rejected);

    expect(actual)
        .toEqual(expected);
  });

  it('should return correct string for tooltip draft', () => {
    const expected: string = 'correctStatusTooltipDraft';
    spyOn(translate, 'instant').and.returnValue('correctStatusTooltipDraft')

    const actual: string = component.geti18nTooltip(status.draft);

    expect(actual)
        .toEqual(expected);
  });
});
