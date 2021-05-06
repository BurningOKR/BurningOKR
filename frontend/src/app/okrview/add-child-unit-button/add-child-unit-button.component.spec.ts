import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddChildUnitButtonComponent } from './add-child-unit-button.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';

describe('AddChildUnitButtonComponent', () => {
  let component: AddChildUnitButtonComponent;
  let fixture: ComponentFixture<AddChildUnitButtonComponent>;

  const contextRoleMock: any = {
    isAtleastAdmin: jest.fn()
    };

  beforeEach(async(() => {
    contextRoleMock.isAtleastAdmin.mockReset();
    contextRoleMock.isAtleastAdmin.mockReturnValue(true);
    TestBed.configureTestingModule({
      declarations: [ AddChildUnitButtonComponent ],
      imports: [ MaterialTestingModule ],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddChildUnitButtonComponent);
    component = fixture.componentInstance;
    component.userRole = contextRoleMock;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component)
      .toBeTruthy();
  });
});
