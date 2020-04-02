import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InitStateViewComponent } from './init-state-view.component';

describe('InitStateViewComponent', () => {
  let component: InitStateViewComponent;
  let fixture: ComponentFixture<InitStateViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InitStateViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InitStateViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
