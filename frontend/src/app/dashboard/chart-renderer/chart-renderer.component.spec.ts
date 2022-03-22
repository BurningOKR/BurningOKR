import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChartRendererComponent } from './chart-renderer.component';

describe('GraphRendererComponent', () => {
  let component: ChartRendererComponent;
  let fixture: ComponentFixture<ChartRendererComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChartRendererComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChartRendererComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
