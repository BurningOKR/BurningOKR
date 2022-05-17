import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChartOptionsCardComponent } from './chart-options-card.component';

describe('ChartOptionsCardComponent', () => {
  let component: ChartOptionsCardComponent;
  let fixture: ComponentFixture<ChartOptionsCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChartOptionsCardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChartOptionsCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
