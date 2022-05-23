import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BaseChartOptions } from '../../model/ui/base-chart-options';

import { BaseChartComponent } from './base-chart.component';

describe('BaseChartComponent', () => {
  let component: BaseChartComponent<BaseChartOptions>;
  let fixture: ComponentFixture<BaseChartComponent<BaseChartOptions>>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BaseChartComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BaseChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
