import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateDashboardComponent } from './create-dashboard.component';

describe('CreateDashboardComponent', () => {
  let component: CreateDashboardComponent;
  let fixture: ComponentFixture<CreateDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateDashboardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
