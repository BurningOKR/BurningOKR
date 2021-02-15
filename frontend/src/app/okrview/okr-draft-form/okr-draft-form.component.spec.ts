import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OkrDraftFormComponent } from './okr-draft-form.component';

describe('OkrDraftFormComponent', () => {
  let component: OkrDraftFormComponent;
  let fixture: ComponentFixture<OkrDraftFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OkrDraftFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OkrDraftFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
