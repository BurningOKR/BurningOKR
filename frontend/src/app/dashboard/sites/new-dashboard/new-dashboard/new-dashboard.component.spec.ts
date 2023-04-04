import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewDashboardComponent } from './new-dashboard.component';

describe('NewDashboardComponent', () => {
  let component: NewDashboardComponent;
  let fixture: ComponentFixture<NewDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NewDashboardComponent],
    })
      .compileComponents();

    fixture = TestBed.createComponent(NewDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
