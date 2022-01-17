import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CountdownTimerComponent } from './countdown-timer.component';
import {BehaviorSubject} from 'rxjs';

describe('CountdownTimerComponent', () => {
  let component: CountdownTimerComponent;
  let fixture: ComponentFixture<CountdownTimerComponent>;

  beforeEach(() => {

    TestBed.configureTestingModule({
      declarations: [ CountdownTimerComponent ]
    }).compileComponents();

    fixture = TestBed.createComponent(CountdownTimerComponent);
    component = fixture.componentInstance;
    component.endTime$ = new BehaviorSubject<Date>(new Date());
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
