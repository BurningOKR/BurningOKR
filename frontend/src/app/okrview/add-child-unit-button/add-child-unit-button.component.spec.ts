import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddChildUnitButtonComponent } from './add-child-unit-button.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';

describe('AddChildUnitButtonComponent', () => {
  let component: AddChildUnitButtonComponent;
  let fixture: ComponentFixture<AddChildUnitButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddChildUnitButtonComponent ],
      imports: [ MaterialTestingModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddChildUnitButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
