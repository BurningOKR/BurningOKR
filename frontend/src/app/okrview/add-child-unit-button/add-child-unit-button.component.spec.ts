import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { AddChildUnitButtonComponent } from './add-child-unit-button.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule } from '@angular/material/dialog';

describe('AddChildUnitButtonComponent', () => {
  let component: AddChildUnitButtonComponent;
  let fixture: ComponentFixture<AddChildUnitButtonComponent>;
  const i18n: any = {};

  const contextRoleMock: any = {
    isAtleastAdmin: jest.fn()
    };

  beforeEach(waitForAsync(() => {
    contextRoleMock.isAtleastAdmin.mockReset();
    contextRoleMock.isAtleastAdmin.mockReturnValue(true);
    TestBed.configureTestingModule({
      declarations: [ AddChildUnitButtonComponent ],
      imports: [ MaterialTestingModule, MatSnackBarModule, MatDialogModule ],
      providers: [
          { provide: I18n, useValue: i18n },
      ]
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
