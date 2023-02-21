import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskboardUsersComponent } from './taskboard-users.component';
import { AvatarComponent } from 'ngx-avatars';
import { OAuthModule } from 'angular-oauth2-oidc';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('TaskboardUsersComponent', () => {
  let component: TaskboardUsersComponent;
  let fixture: ComponentFixture<TaskboardUsersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TaskboardUsersComponent, AvatarComponent],
      imports: [OAuthModule.forRoot(), HttpClientTestingModule],
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskboardUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  //TODO - fix test
  /*it('should create', () => {
   expect(component).toBeTruthy();
   });
   */
});
