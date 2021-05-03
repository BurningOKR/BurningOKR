import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DepartmentTabTaskCardComponent } from './department-tab-task-card.component';

describe('DepartmentTabTaskCardComponent', () => {
  let component: DepartmentTabTaskCardComponent;
  let fixture: ComponentFixture<DepartmentTabTaskCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DepartmentTabTaskCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DepartmentTabTaskCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
