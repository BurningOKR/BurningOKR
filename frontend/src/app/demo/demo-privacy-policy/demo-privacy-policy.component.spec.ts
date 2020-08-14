import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DemoPrivacyPolicyComponent } from './demo-privacy-policy.component';

describe('DemoPrivacyPolicyComponent', () => {
  let component: DemoPrivacyPolicyComponent;
  let fixture: ComponentFixture<DemoPrivacyPolicyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DemoPrivacyPolicyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DemoPrivacyPolicyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
