import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NoMailInformationComponent } from './no-mail-information.component';

describe('NoMailInformationComponent', () => {
  let component: NoMailInformationComponent;
  let fixture: ComponentFixture<NoMailInformationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NoMailInformationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NoMailInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
