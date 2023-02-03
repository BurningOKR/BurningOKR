import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DemoPrivacyPolicyComponent } from './demo-privacy-policy.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';

describe('DemoPrivacyPolicyComponent', () => {
  let component: DemoPrivacyPolicyComponent;
  let fixture: ComponentFixture<DemoPrivacyPolicyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DemoPrivacyPolicyComponent],
      imports: [MaterialTestingModule],
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
