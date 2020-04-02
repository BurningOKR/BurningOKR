import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NonLoggedInCardComponent } from './non-logged-in-card.component';

describe('NonLoggedInCardComponent', () => {
  let component: NonLoggedInCardComponent;
  let fixture: ComponentFixture<NonLoggedInCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NonLoggedInCardComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NonLoggedInCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
