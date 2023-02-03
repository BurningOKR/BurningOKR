import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DemoFooterComponent } from './demo-footer.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';

describe('DemoFooterComponent', () => {
  let component: DemoFooterComponent;
  let fixture: ComponentFixture<DemoFooterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DemoFooterComponent],
      imports: [MaterialTestingModule],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DemoFooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
