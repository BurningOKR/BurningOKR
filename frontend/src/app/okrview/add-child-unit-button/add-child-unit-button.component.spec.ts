import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { AddChildUnitButtonComponent } from './add-child-unit-button.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule } from '@angular/material/dialog';

describe('AddChildUnitButtonComponent', () => {
  let component: AddChildUnitButtonComponent;
  let fixture: ComponentFixture<AddChildUnitButtonComponent>;

  const contextRoleMock: any = {
    isAtleastAdmin: jest.fn()
    };

  beforeEach(waitForAsync(() => {
    contextRoleMock.isAtleastAdmin.mockReset();
    contextRoleMock.isAtleastAdmin.mockReturnValue(true);
    TestBed.configureTestingModule({
      declarations: [ AddChildUnitButtonComponent ],
      imports: [ MaterialTestingModule, MatSnackBarModule, MatDialogModule ],
    })
    .compileComponents();
  }));

  it('should create', () => {
    fixture = TestBed.createComponent(AddChildUnitButtonComponent);
    component = fixture.componentInstance;
    component.userRole = contextRoleMock;

    fixture.detectChanges();
    expect(component)
      .toBeTruthy();
  });
});
