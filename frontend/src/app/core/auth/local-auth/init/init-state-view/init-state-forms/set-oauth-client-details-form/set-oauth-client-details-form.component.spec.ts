import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SetOauthClientDetailsFormComponent } from './set-oauth-client-details-form.component';

describe('SetOauthClientDetailsFormComponent', () => {
  let component: SetOauthClientDetailsFormComponent;
  let fixture: ComponentFixture<SetOauthClientDetailsFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SetOauthClientDetailsFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SetOauthClientDetailsFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
