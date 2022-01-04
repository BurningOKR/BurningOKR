import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PickLanguageComponent } from './pick-language.component';

describe('PickLanguageComponent', () => {
  let component: PickLanguageComponent;
  let fixture: ComponentFixture<PickLanguageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PickLanguageComponent ]
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
