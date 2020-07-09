import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WelcomeInitStateFormComponent } from './welcome-init-state-form.component';

describe('WelcomeInitStateFormComponent', () => {
  let component: WelcomeInitStateFormComponent;
  let fixture: ComponentFixture<WelcomeInitStateFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WelcomeInitStateFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WelcomeInitStateFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
