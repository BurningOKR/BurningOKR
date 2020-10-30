import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DemoCreditsComponent } from './demo-credits.component';

describe('DemoCreditsComponent', () => {
  let component: DemoCreditsComponent;
  let fixture: ComponentFixture<DemoCreditsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DemoCreditsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DemoCreditsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
