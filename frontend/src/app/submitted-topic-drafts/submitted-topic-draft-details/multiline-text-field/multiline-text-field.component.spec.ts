import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultilineTextFieldComponent } from './multiline-text-field.component';

describe('MultilineTextFieldComponent', () => {
  let component: MultilineTextFieldComponent;
  let fixture: ComponentFixture<MultilineTextFieldComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MultilineTextFieldComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultilineTextFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
