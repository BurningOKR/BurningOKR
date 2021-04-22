import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmittedTopicDraftsComponent } from './submitted-topic-drafts.component';

describe('SubmittedTopicDraftsComponent', () => {
  let component: SubmittedTopicDraftsComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubmittedTopicDraftsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmittedTopicDraftsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
