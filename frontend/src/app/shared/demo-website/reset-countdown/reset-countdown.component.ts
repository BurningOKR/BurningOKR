import { Component, OnInit } from '@angular/core';
import { CountdownTimestampApiService } from '../../services/api/countdown-timestamp-api.service';
import { Router } from '@angular/router';
import {environment} from '../../../../environments/environment';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-reset-countdown',
  templateUrl: './reset-countdown.component.html',
  styleUrls: ['./reset-countdown.component.css']
})
export class ResetCountdownComponent implements OnInit {

  nextReset: string;
  isPlayground: boolean = environment.playground;
  dateForNextReset$: Observable<Date>;

  constructor(private countdownService: CountdownTimestampApiService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.dateForNextReset$ = this.countdownService.getDateForNextReset$();
  }

  reload(): void {

    if (this.isPlayground) {
      this.router.navigate(['/landingpage'])
        .then(() => location.reload());
    }
  }
}
