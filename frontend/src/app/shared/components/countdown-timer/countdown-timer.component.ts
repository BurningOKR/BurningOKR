import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { Observable,  timer} from "rxjs";
import { map, switchMap} from "rxjs/operators";

@Component({
  selector: 'app-countdown-timer',
  templateUrl: './countdown-timer.component.html',
  styleUrls: ['./countdown-timer.component.scss']
})
export class CountdownTimerComponent implements OnInit {

  @Input() endTime$: Observable<Date>;
  @Output() zeroTrigger: EventEmitter<void> = new EventEmitter<void>();

  remainingTimeString$: Observable<String>;

  ngOnInit() {

    this.remainingTimeString$ = this.endTime$.pipe(
      switchMap((endTime) => {
        return timer(0, 1000)
          .pipe(
            map(() => this.getRemainingTimeString(endTime))
          )
      })
    );
  }

  getRemainingTimeString(endTime: Date): String {
    // Shaving off one Hour because of TimeZone differences
    let remainingTime: Date = new Date(endTime?.getTime() - new Date().getTime() - 1000*60*60);
    return remainingTime.toLocaleTimeString();
  }
}
