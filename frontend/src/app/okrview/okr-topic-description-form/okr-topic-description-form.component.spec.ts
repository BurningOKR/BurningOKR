import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OkrTopicDescriptionFormComponent } from './okr-topic-description-form.component';

describe('OkrTopicDescriptionFormComponent', () => {
  let component: OkrTopicDescriptionFormComponent;
  let fixture: ComponentFixture<OkrTopicDescriptionFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OkrTopicDescriptionFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OkrTopicDescriptionFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
