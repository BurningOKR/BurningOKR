import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmittedTopicDraftActionButtonComponent } from './submitted-topic-draft-action-button.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { MatDialog } from '@angular/material';

describe('SubmittedTopicDraftActionButtonComponent', () => {
  let component: SubmittedTopicDraftActionButtonComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftActionButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubmittedTopicDraftActionButtonComponent ],
      imports: [ MaterialTestingModule ],
      providers: [
        {provide: MatDialog, useValue: {}}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmittedTopicDraftActionButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
        .toBeTruthy();
  });
});
