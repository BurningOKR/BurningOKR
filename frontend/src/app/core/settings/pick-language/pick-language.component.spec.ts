import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PickLanguageComponent } from './pick-language.component';
import {DialogComponent} from "../../../shared/components/dialog-component/dialog.component";
import {MaterialTestingModule} from "../../../testing/material-testing.module";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

describe('PickLanguageComponent', () => {
  let component: PickLanguageComponent;
  let fixture: ComponentFixture<PickLanguageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        PickLanguageComponent,
        DialogComponent,
      ],
      imports: [
        MaterialTestingModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: {} }
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PickLanguageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
