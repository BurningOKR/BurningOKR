import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { User } from '../../../../shared/model/api/user';
import { MaterialTestingModule } from '../../../../testing/material-testing.module';
import { Dashboard } from '../../../model/ui/dashboard';

import { DashboardCardComponent } from './dashboard-card.component';

describe('DashboardCardComponent', () => {
  let component: DashboardCardComponent;
  let fixture: ComponentFixture<DashboardCardComponent>;
  const dashboard: Dashboard = {} as Dashboard;
  const creator: User = {} as User;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
        imports: [MaterialTestingModule],
        declarations: [DashboardCardComponent],
        schemas: [CUSTOM_ELEMENTS_SCHEMA],
      })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardCardComponent);
    component = fixture.componentInstance;
    dashboard.creator = creator;
    component.dashboard = dashboard;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
