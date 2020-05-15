import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserAvatarComponent } from './user-avatar.component';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { User } from '../../model/api/user';

describe('UserAvatarComponent', () => {
  let component: UserAvatarComponent;
  let fixture: ComponentFixture<UserAvatarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserAvatarComponent],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
    })
      .compileComponents();
    fixture = TestBed.createComponent(UserAvatarComponent);
    component = fixture.componentInstance;
    component.user = new User();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
