import { AfterContentInit, Component, ElementRef, HostListener, Input, ViewChild } from '@angular/core';
import { ViewKeyResultMilestone } from '../../model/ui/view-key-result-milestone';
import { ViewKeyResult } from '../../model/ui/view-key-result';

@Component({
  selector: 'app-milestone-slider-wrapper',
  templateUrl: './milestone-slider-wrapper.component.html',
  styleUrls: ['./milestone-slider-wrapper.component.scss']
})
export class MilestoneSliderWrapperComponent implements AfterContentInit {

  @Input() keyResult: ViewKeyResult;

  @ViewChild('wrapper', { static: true }) wrapper: ElementRef;

  private readonly SLIDER_BORDER = 8; // The border width of the slider in px
  private readonly MILESTONE_TICK_OFFSET = 12; // The offset of the ticks so that they fit on the slider
  private readonly MILESTONE_TICK_WIDTH = 6; // The width of a tick

  private width: number = 0;

  @HostListener('window:resize', ['$event'])
  onResize(): void {
    this.updateWidth();
  }

  ngAfterContentInit(): void {
    this.updateWidth();
  }

  getMilestoneLeftPxValue(milestone: ViewKeyResultMilestone): number {
    return Math.ceil(
      ((milestone.value - this.keyResult.start) / (this.keyResult.end - this.keyResult.start))
      * this.width
      + this.MILESTONE_TICK_OFFSET
      - this.MILESTONE_TICK_WIDTH / 2
    );
  }

  private updateWidth(): void {
    this.width = this.wrapper.nativeElement.clientWidth - this.SLIDER_BORDER * 2;
  }
}
