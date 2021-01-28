import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskboardStateColumnViewComponent } from './taskboard-state-column-view.component';

describe('TaskboardStateColumnViewComponent', () => {
  let component: TaskboardStateColumnViewComponent;
  let fixture: ComponentFixture<TaskboardStateColumnViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TaskboardStateColumnViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskboardStateColumnViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
