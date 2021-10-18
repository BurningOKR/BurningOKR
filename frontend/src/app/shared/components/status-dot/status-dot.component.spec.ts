import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatusDotComponent } from './status-dot.component';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { i18nMock } from '../../mocks/i18n-mock';
import { status } from '../../model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';

describe('StatusDotComponent', () => {
  let component: StatusDotComponent;
  let fixture: ComponentFixture<StatusDotComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [StatusDotComponent],
      providers: [
        {provide: I18n, useValue: i18nMock}
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatusDotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should return correct string for tooltip submitted', () => {
    const expected: string = 'correctStatusTooltipSubmitted';

    const actual: string = component.geti18nTooltip(status.submitted);

    expect(actual)
        .toEqual(expected);
  });

  it('should return correct string for tooltip approved', () => {
    const expected: string = 'correctStatusTooltipApproved';

    const actual: string = component.geti18nTooltip(status.approved);

    expect(actual)
        .toEqual(expected);
  });

  it('should return correct string for tooltip rejected', () => {
    const expected: string = 'correctStatusTooltipRejected';

    const actual: string = component.geti18nTooltip(status.rejected);

    expect(actual)
        .toEqual(expected);
  });

  it('should return correct string for tooltip draft', () => {
    const expected: string = 'correctStatusTooltipDraft';

    const actual: string = component.geti18nTooltip(status.draft);

    expect(actual)
        .toEqual(expected);
  });
});
