import { Component, OnInit } from '@angular/core';
import { CountdownTimestampApiService } from '../../services/api/countdown-timestamp-api.service';
import { Router } from '@angular/router';
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-reset-countdown',
  templateUrl: './reset-countdown.component.html',
  styleUrls: ['./reset-countdown.component.css']
})
export class ResetCountdownComponent implements OnInit {

  nextReset: string;
  isPlayground: boolean = environment.playground;

  constructor(private countdownService: CountdownTimestampApiService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.countdownService.getDateForNextReset$()
      .subscribe((nextReset: Date) => {
        this.nextReset = `${nextReset.getFullYear()}-${nextReset.getMonth() + 1}-${nextReset.getDate()} `
          + `${nextReset.getHours()}:${nextReset.getMinutes()}:${nextReset.getSeconds()}`;
      });
  }

  reload(): void {

    if (this.isPlayground)
      this.router.navigate(['/landingpage'])
        .then(() => location.reload());
  }
}
