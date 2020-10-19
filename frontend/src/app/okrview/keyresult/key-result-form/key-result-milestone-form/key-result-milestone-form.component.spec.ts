import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultMilestoneFormComponent } from './key-result-milestone-form.component';

describe('KeyResultMilestoneFormComponent', () => {
  let component: KeyResultMilestoneFormComponent;
  let fixture: ComponentFixture<KeyResultMilestoneFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KeyResultMilestoneFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KeyResultMilestoneFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
