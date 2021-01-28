import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskboardSwimlaneComponent } from './taskboard-swimlane.component';

describe('TaskboardSwimlaneComponent', () => {
  let component: TaskboardSwimlaneComponent;
  let fixture: ComponentFixture<TaskboardSwimlaneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TaskboardSwimlaneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskboardSwimlaneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
