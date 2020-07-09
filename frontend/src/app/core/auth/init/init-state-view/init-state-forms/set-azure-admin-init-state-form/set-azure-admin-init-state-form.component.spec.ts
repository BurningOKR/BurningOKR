import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SetAzureAdminInitStateFormComponent } from './set-azure-admin-init-state-form.component';

describe('SetAzureAdminInitStateFormComponent', () => {
  let component: SetAzureAdminInitStateFormComponent;
  let fixture: ComponentFixture<SetAzureAdminInitStateFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SetAzureAdminInitStateFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SetAzureAdminInitStateFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
