import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskboardColumnComponent } from './taskboard-column.component';

describe('TaskboardColumnComponent', () => {
  let component: TaskboardColumnComponent;
  let fixture: ComponentFixture<TaskboardColumnComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TaskboardColumnComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskboardColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
