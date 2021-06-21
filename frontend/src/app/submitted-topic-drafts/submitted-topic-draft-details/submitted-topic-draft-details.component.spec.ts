import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmittedTopicDraftDetailsComponent } from './submitted-topic-draft-details.component';

describe('SubmittedTopicDraftDetailsComponent', () => {
  let component: SubmittedTopicDraftDetailsComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubmittedTopicDraftDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmittedTopicDraftDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
