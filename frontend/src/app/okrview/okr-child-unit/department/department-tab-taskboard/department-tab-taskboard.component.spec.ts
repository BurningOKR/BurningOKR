import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DepartmentTabTaskboardComponent } from './department-tab-taskboard.component';

describe('DepartmentTabTaskboardComponent', () => {
  let component: DepartmentTabTaskboardComponent;
  let fixture: ComponentFixture<DepartmentTabTaskboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DepartmentTabTaskboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DepartmentTabTaskboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
