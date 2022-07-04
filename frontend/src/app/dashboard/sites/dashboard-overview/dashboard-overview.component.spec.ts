import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { NGXLogger } from 'ngx-logger';
import { AuthenticationService } from '../../../core/auth/services/authentication.service';
import { MaterialTestingModule } from '../../../testing/material-testing.module';

import { DashboardOverviewComponent } from './dashboard-overview.component';

describe('DashboardOverviewComponent', () => {
  let component: DashboardOverviewComponent;
  let fixture: ComponentFixture<DashboardOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
        imports: [HttpClientTestingModule, RouterTestingModule, MaterialTestingModule],
        declarations: [DashboardOverviewComponent],
        schemas: [CUSTOM_ELEMENTS_SCHEMA],
        providers: [
          { provide: AuthenticationService, useValue: {} },
          { provide: NGXLogger, useValue: {} },
        ],
      })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
