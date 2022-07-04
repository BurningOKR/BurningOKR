import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskboardUsersComponent } from './taskboard-users.component';
import {AvatarComponent} from "ngx-avatar";

describe('TaskboardUsersComponent', () => {
  let component: TaskboardUsersComponent;
  let fixture: ComponentFixture<TaskboardUsersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TaskboardUsersComponent, AvatarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskboardUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
