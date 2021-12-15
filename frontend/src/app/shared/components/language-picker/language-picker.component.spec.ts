import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LanguagePickerComponent } from './language-picker.component';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('LanguagePickerComponent', () => {
  let component: LanguagePickerComponent;
  let fixture: ComponentFixture<LanguagePickerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LanguagePickerComponent ],
      imports: [ MaterialTestingModule, BrowserAnimationsModule ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LanguagePickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
