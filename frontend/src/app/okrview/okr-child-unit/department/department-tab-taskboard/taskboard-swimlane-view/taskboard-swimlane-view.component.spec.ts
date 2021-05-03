import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskboardSwimlaneViewComponent } from './taskboard-swimlane-view.component';

describe('TaskboardSwimlaneViewComponent', () => {
  let component: TaskboardSwimlaneViewComponent;
  let fixture: ComponentFixture<TaskboardSwimlaneViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TaskboardSwimlaneViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskboardSwimlaneViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
