import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PickChartTypeModalComponent } from './pick-chart-type-modal.component';

describe('PickChartTypeModalComponent', () => {
  let component: PickChartTypeModalComponent;
  let fixture: ComponentFixture<PickChartTypeModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PickChartTypeModalComponent],
    })
        .compileComponents();

    fixture = TestBed.createComponent(PickChartTypeModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
