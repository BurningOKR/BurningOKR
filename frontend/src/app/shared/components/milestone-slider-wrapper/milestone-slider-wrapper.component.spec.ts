import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MilestoneSliderWrapperComponent } from './milestone-slider-wrapper.component';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { ViewKeyResult } from '../../model/ui/view-key-result';
import { Unit } from '../../model/api/unit.enum';
import { ViewKeyResultMilestone } from '../../model/ui/view-key-result-milestone';

let milestone: ViewKeyResultMilestone;

describe('MilestoneSliderWrapperComponent', () => {
  let componentFixture: ComponentFixture<MilestoneSliderWrapperComponent>;
  let component: MilestoneSliderWrapperComponent;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MilestoneSliderWrapperComponent],
      imports: [MaterialTestingModule],
    })
      .compileComponents();
  });

  beforeEach(() => {
    componentFixture = TestBed.createComponent(MilestoneSliderWrapperComponent);
    component = componentFixture.componentInstance;

    component.keyResult = new ViewKeyResult(1, 10, 15, 50, Unit.NUMBER, 'test', 'test', 0, [], []);
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('width: 443, value: 20, range: 10 - 50 should return 116', () => {
    milestone = new ViewKeyResultMilestone(2, 'test', 1, 20);

    component.wrapper = { nativeElement: { clientWidth: 443 } };

    componentFixture.detectChanges();

    const expected: number = 116;
    const actual: number = component.getMilestoneLeftPxValue(milestone);

    expect(actual)
      .toBe(expected);
  });

  it('width: 291,75, value: 20, range: 10 - 50 should return 78', () => {
    milestone = new ViewKeyResultMilestone(2, 'test', 1, 20);

    component.wrapper = { nativeElement: { clientWidth: 291.75 } };

    componentFixture.detectChanges();

    const expected: number = 78;
    const actual: number = component.getMilestoneLeftPxValue(milestone);

    expect(actual)
      .toBe(expected);
  });

  it('width: 462, value: 20, range: 10 - 50 should return 121', () => {
    milestone = new ViewKeyResultMilestone(2, 'test', 1, 20);

    component.wrapper = { nativeElement: { clientWidth: 462 } };

    componentFixture.detectChanges();

    const expected: number = 121;
    const actual: number = component.getMilestoneLeftPxValue(milestone);

    expect(actual)
      .toBe(expected);
  });

  it('width: 443, value: 50, range: 10 - 50 should return 436', () => {
    milestone = new ViewKeyResultMilestone(2, 'test', 1, 50);

    component.wrapper = { nativeElement: { clientWidth: 443 } };

    componentFixture.detectChanges();

    const expected: number = 436;
    const actual: number = component.getMilestoneLeftPxValue(milestone);

    expect(actual)
      .toBe(expected);
  });

  it('width: 253.61, value: 50, range: 10 - 50 should return 247', () => {
    milestone = new ViewKeyResultMilestone(2, 'test', 1, 50);

    component.wrapper = { nativeElement: { clientWidth: 253.61 } };

    componentFixture.detectChanges();

    const expected: number = 247;
    const actual: number = component.getMilestoneLeftPxValue(milestone);

    expect(actual)
      .toBe(expected);
  });

  it('width: 464, value: 50, range: 10 - 50 should return 457', () => {
    milestone = new ViewKeyResultMilestone(2, 'test', 1, 50);

    component.wrapper = { nativeElement: { clientWidth: 464 } };

    componentFixture.detectChanges();

    const expected: number = 457;
    const actual: number = component.getMilestoneLeftPxValue(milestone);

    expect(actual)
      .toBe(expected);
  });

  it('width: 443, value: 10, range: 10 - 50 should return 9', () => {
    milestone = new ViewKeyResultMilestone(2, 'test', 1, 10);

    component.wrapper = { nativeElement: { clientWidth: 443 } };

    componentFixture.detectChanges();

    const expected: number = 9;
    const actual: number = component.getMilestoneLeftPxValue(milestone);

    expect(actual)
      .toBe(expected);
  });

  it('width: 253.61, value: 10, range: 10 - 50 should return 9', () => {
    milestone = new ViewKeyResultMilestone(2, 'test', 1, 10);

    component.wrapper = { nativeElement: { clientWidth: 253.61 } };

    componentFixture.detectChanges();

    const expected: number = 9;
    const actual: number = component.getMilestoneLeftPxValue(milestone);

    expect(actual)
      .toBe(expected);
  });

  it('width: 464, value: 10, range: 10 - 50 should return 9', () => {
    milestone = new ViewKeyResultMilestone(2, 'test', 1, 10);

    component.wrapper = { nativeElement: { clientWidth: 464 } };

    componentFixture.detectChanges();

    const expected: number = 9;
    const actual: number = component.getMilestoneLeftPxValue(milestone);

    expect(actual)
      .toBe(expected);
  });
});
