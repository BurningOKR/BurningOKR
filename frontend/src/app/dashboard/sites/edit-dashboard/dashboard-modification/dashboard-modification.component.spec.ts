import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardModificationComponent } from './dashboard-modification.component';

describe('DashboardModificationComponent', () => {
  let component: DashboardModificationComponent;
  let fixture: ComponentFixture<DashboardModificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DashboardModificationComponent],
    })
      .compileComponents();

    fixture = TestBed.createComponent(DashboardModificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
