import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NonLoggedInCardComponent } from './non-logged-in-card.component';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';

describe('NonLoggedInCardComponent', () => {
  let component: NonLoggedInCardComponent;
  let fixture: ComponentFixture<NonLoggedInCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NonLoggedInCardComponent],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
    })
      .compileComponents();
    fixture = TestBed.createComponent(NonLoggedInCardComponent);
    component = fixture.componentInstance;
    component.toolbarTitle = 'title';
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
