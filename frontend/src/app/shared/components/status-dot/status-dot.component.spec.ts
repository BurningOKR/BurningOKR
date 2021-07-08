import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatusDotComponent } from './status-dot.component';
import { I18n } from '@ngx-translate/i18n-polyfill';

describe('StatusDotComponent', () => {
  let component: StatusDotComponent;
  let fixture: ComponentFixture<StatusDotComponent>;

  const i18nMock: any = jest.fn();

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [StatusDotComponent],
      providers: [
        {provide: I18n, useValue: i18nMock}
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatusDotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
