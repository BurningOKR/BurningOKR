import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdditionalInfoBarComponent } from './additional-info-bar.component';

describe('AdditionalInfoBarComponent', () => {
  let component: AdditionalInfoBarComponent;
  let fixture: ComponentFixture<AdditionalInfoBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdditionalInfoBarComponent],
    })
      .compileComponents();

    fixture = TestBed.createComponent(AdditionalInfoBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
