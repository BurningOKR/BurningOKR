import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmittedTopicDraftEditComponent } from './submitted-topic-draft-edit.component';

describe('SubmittedTopicDraftEditComponent', () => {
  let component: SubmittedTopicDraftEditComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubmittedTopicDraftEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmittedTopicDraftEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
