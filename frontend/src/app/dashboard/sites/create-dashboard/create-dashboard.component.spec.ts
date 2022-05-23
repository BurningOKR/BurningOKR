import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import 'linq4js';
import { NGXLogger } from 'ngx-logger';
import { AuthenticationService } from '../../../core/auth/services/authentication.service';
import { MaterialTestingModule } from '../../../testing/material-testing.module';

import { CreateDashboardComponent } from './create-dashboard.component';

describe('CreateDashboardComponent', () => {
  let component: CreateDashboardComponent;
  let fixture: ComponentFixture<CreateDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
        imports: [HttpClientTestingModule, RouterTestingModule, MaterialTestingModule, FormsModule, BrowserAnimationsModule],
        providers: [
          { provide: AuthenticationService, useValue: {} },
          {
            provide: NGXLogger, useValue: {}
          }
        ],
        declarations: [CreateDashboardComponent],
        schemas: [CUSTOM_ELEMENTS_SCHEMA],
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
