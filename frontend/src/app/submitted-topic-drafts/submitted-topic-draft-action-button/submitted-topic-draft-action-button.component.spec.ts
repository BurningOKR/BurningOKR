import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmittedTopicDraftActionButtonComponent } from './submitted-topic-draft-action-button.component';

describe('SubmittedTopicDraftActionButtonComponent', () => {
  let component: SubmittedTopicDraftActionButtonComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftActionButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubmittedTopicDraftActionButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmittedTopicDraftActionButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
