import {
  Component,
  EventEmitter,
  Input, OnChanges,
  Output,
} from '@angular/core';
import { Observable,  timer} from 'rxjs';
import { map} from 'rxjs/operators';
import {CountdownTimerService} from '../countdown-timer.service';

@Component({
  selector: 'app-countdown-timer',
  templateUrl: './countdown-timer.component.html',
  styleUrls: ['./countdown-timer.component.scss']
})
export class CountdownTimerComponent implements OnChanges {

  @Input() endTime: Date;
  @Output() zeroTrigger: EventEmitter<void> = new EventEmitter<void>();

  remainingTimeString$: Observable<String>;

  constructor(private timerService: CountdownTimerService) { }

  ngOnChanges(): void {

    this.remainingTimeString$ = timer(0, 1000)
      .pipe(
        map(() => {

          const remainingTime: Date = this.timerService.getRemainingTimeUntil(this.endTime);

          if(remainingTime.getTime() <= 0) {

            this.zeroTrigger.emit();
          }

          return this.timerService.getLocalTimeString(remainingTime);
        })
      );
  }

}
