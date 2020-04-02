import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUserInitStateFormComponent } from './create-user-init-state-form.component';

describe('InitStateFormComponent', () => {
  let component: CreateUserInitStateFormComponent;
  let fixture: ComponentFixture<CreateUserInitStateFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateUserInitStateFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateUserInitStateFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
