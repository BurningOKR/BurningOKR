import { CycleCreationFormComponent } from './cycle-creation-form.component';
import moment from 'moment';

describe('CycleCreationFormComponent', () => {
  let component: CycleCreationFormComponent;

  const eventMock: any = { value: null };

  beforeEach(() => {
    component = new CycleCreationFormComponent(null, null, null, null, null);
  });

  it('should set FirstAvailableDate to the correct Date after Changing the Date', () => {
    eventMock.value = moment('2022-01-01T00:00:00+00:00');
    component.dateChangeHandler(eventMock);
    const expected: Date = new Date('2022-01-04');
    expect(component.firstAvailableDate).toEqual(expected);
  });
});
