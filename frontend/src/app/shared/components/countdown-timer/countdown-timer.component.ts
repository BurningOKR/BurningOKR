import {
  Component,
  EventEmitter,
  Input, OnChanges,
  Output,
} from '@angular/core';
import { Observable,  timer} from 'rxjs';
import { map} from 'rxjs/operators';

@Component({
  selector: 'app-countdown-timer',
  templateUrl: './countdown-timer.component.html',
  styleUrls: ['./countdown-timer.component.scss']
})
export class CountdownTimerComponent implements OnChanges {

  @Input() endTime: Date;
  @Output() zeroTrigger: EventEmitter<void> = new EventEmitter<void>();

  remainingTimeString$: Observable<String>;

  ngOnChanges(): void {

    this.remainingTimeString$ = timer(0, 1000)
      .pipe(
        map(() => {

          const remainingTime: Date = this.getRemainingTime(this.endTime);

          if(remainingTime.getTime() <= 0) {

            this.zeroTrigger.emit();
          }

          return this.getRemainingTimeString(remainingTime);
        })
      );
  }

  getRemainingTimeString(remainingTime: Date): String {

    const temporaryDate: Date = new Date(remainingTime.getTime() + 1000 * 60 * remainingTime.getTimezoneOffset());

    return temporaryDate.toLocaleTimeString();
  }

  getRemainingTime(endTime: Date): Date {

    return new Date(endTime?.getTime() - new Date().getTime());
  }
}
