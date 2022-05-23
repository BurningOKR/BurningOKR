import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MaterialTestingModule } from '../../../../testing/material-testing.module';
import { ChartCreationOptionsDto } from '../../../model/dto/chart-creation-options.dto';

import { ChartOptionsCardComponent } from './chart-options-card.component';

describe('ChartOptionsCardComponent', () => {
  let component: ChartOptionsCardComponent;
  let fixture: ComponentFixture<ChartOptionsCardComponent>;
  const chart: ChartCreationOptionsDto = {} as ChartCreationOptionsDto;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
        imports: [MaterialTestingModule],
        declarations: [ChartOptionsCardComponent],
      })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChartOptionsCardComponent);
    component = fixture.componentInstance;
    component.chart = chart;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
