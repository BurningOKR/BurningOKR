import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateDashboardComponent } from './create-dashboard.component';

describe('NewDashboardComponent', () => {
  let component: CreateDashboardComponent;
  let fixture: ComponentFixture<CreateDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreateDashboardComponent],
    })
      .compileComponents();

    fixture = TestBed.createComponent(CreateDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
