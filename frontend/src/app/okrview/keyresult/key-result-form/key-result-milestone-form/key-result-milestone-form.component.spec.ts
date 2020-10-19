import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultMilestoneFormComponent } from './key-result-milestone-form.component';
import { MaterialTestingModule } from '../../../../testing/material-testing.module';
import { FormErrorComponent } from '../../../../shared/components/form-error/form-error.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

describe('KeyResultMilestoneFormComponent', () => {
  let component: KeyResultMilestoneFormComponent;
  let fixture: ComponentFixture<KeyResultMilestoneFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KeyResultMilestoneFormComponent, FormErrorComponent ],
      imports: [ MaterialTestingModule, ReactiveFormsModule, FormsModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KeyResultMilestoneFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
