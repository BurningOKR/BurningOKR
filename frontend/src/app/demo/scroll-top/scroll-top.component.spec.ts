import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScrollTopComponent } from './scroll-top.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';

describe('ScrollTopComponent', () => {
  let component: ScrollTopComponent;
  let fixture: ComponentFixture<ScrollTopComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ScrollTopComponent],
      imports: [MaterialTestingModule],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScrollTopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
