import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { TranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';
import { take } from 'rxjs/operators';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { status } from '../../model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { StatusDotComponent } from './status-dot.component';

describe('StatusDotComponent', () => {
  let component: StatusDotComponent;
  let fixture: ComponentFixture<StatusDotComponent>;
  let translate: TranslateService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [StatusDotComponent],
      imports: [MaterialTestingModule],
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

  it('should return correct string for tooltip submitted', done => {
    const expected: string = 'correctStatusTooltipSubmitted';
    spyOn(translate, 'stream').and.returnValue(of('correctStatusTooltipSubmitted'));

    component.getTranslateTooltip$(status.submitted).pipe(take(1))
      .subscribe(actual => {
        expect(actual)
          .toEqual(expected);
        done();
      });
  });

  it('should return correct string for tooltip approved', done => {
    const expected: string = 'correctStatusTooltipApproved';
    spyOn(translate, 'stream').and.returnValue(of('correctStatusTooltipApproved'));

    component.getTranslateTooltip$(status.approved).pipe(take(1))
      .subscribe(actual => {
        expect(actual)
          .toEqual(expected);
        done();
      });
  });

  it('should return correct string for tooltip rejected', done => {
    const expected: string = 'correctStatusTooltipRejected';
    spyOn(translate, 'stream').and.returnValue(of('correctStatusTooltipRejected'));

    component.getTranslateTooltip$(status.rejected).pipe(take(1))
      .subscribe(actual => {
        expect(actual)
          .toEqual(expected);
        done();
      });
  });

  it('should return correct string for tooltip draft', done => {
    const expected: string = 'correctStatusTooltipDraft';
    spyOn(translate, 'stream').and.returnValue(of('correctStatusTooltipDraft'));

    component.getTranslateTooltip$(status.draft).pipe(take(1))
      .subscribe(actual => {
        expect(actual)
          .toEqual(expected);
        done();
      });
  });
});
