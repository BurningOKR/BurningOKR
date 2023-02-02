/* eslint-disable */
import { TestBed } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA, Directive, Input, NO_ERRORS_SCHEMA, Pipe, PipeTransform } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { OkrToolbarBareComponent } from './okr-toolbar-bare.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DialogComponent } from '../dialog-component/dialog.component';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@Directive({ selector: '[oneviewPermitted]' })
class OneviewPermittedDirective {
  @Input() oneviewPermitted;
}

@Pipe({ name: 'translate' })
class TranslatePipe implements PipeTransform {
  transform(value) {
    return value;
  }
}

@Pipe({ name: 'phoneNumber' })
class PhoneNumberPipe implements PipeTransform {
  transform(value) {
    return value;
  }
}

@Pipe({ name: 'safeHtml' })
class SafeHtmlPipe implements PipeTransform {
  transform(value) {
    return value;
  }
}

describe('OkrToolbarBareComponent', () => {
  let fixture;
  let component;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        FormsModule,
        ReactiveFormsModule,
        MaterialTestingModule,
        BrowserAnimationsModule],
      declarations: [
        OkrToolbarBareComponent,
        TranslatePipe, PhoneNumberPipe, SafeHtmlPipe,
        OneviewPermittedDirective, DialogComponent,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: {} },
      ],
    }).overrideComponent(OkrToolbarBareComponent, {}).compileComponents();
    fixture = TestBed.createComponent(OkrToolbarBareComponent);
    component = fixture.debugElement.componentInstance;
  });

  afterEach(() => {
    component.ngOnDestroy = function() {
    };
    fixture.destroy();
  });

  it('should run #constructor()', async () => {
    expect(component).toBeTruthy();
  });

});
