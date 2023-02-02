import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { CompleteInitStateFormComponent } from './complete-init-state-form.component';
import { MaterialTestingModule } from '../../../../../../testing/material-testing.module';

describe('CompleteInitStateFormComponent', () => {
  let component: CompleteInitStateFormComponent;
  let fixture: ComponentFixture<CompleteInitStateFormComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [CompleteInitStateFormComponent],
      imports: [MaterialTestingModule],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompleteInitStateFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
