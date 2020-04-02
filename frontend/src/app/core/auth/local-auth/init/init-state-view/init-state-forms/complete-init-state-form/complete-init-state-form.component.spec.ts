import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompleteInitStateFormComponent } from './complete-init-state-form.component';

describe('CompleteInitStateFormComponent', () => {
  let component: CompleteInitStateFormComponent;
  let fixture: ComponentFixture<CompleteInitStateFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompleteInitStateFormComponent ]
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
