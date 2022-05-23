import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LineChartOptions } from '../../model/ui/line-chart-options';

import { BasicLineChartComponent } from './basic-line-chart.component';

describe('BasicLineChartComponent', () => {
  let component: BasicLineChartComponent;
  let fixture: ComponentFixture<BasicLineChartComponent>;
  const lineChartOptions: LineChartOptions = {} as LineChartOptions;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
        declarations: [BasicLineChartComponent],
        schemas: [CUSTOM_ELEMENTS_SCHEMA]
      })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BasicLineChartComponent);
    component = fixture.componentInstance;
    component.chartOptions = lineChartOptions;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
