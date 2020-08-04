import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { DemoWarningComponent } from '../demo-warning/demo-warning.component';
import { Router, NavigationEnd } from '@angular/router';
import { NgwWowService } from 'ngx-wow';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-demo-home',
  templateUrl: './demo-home.component.html',
  styleUrls: ['./demo-home.component.scss']
})
export class DemoHomeComponent implements OnInit, OnDestroy {

  private wowSubscription: Subscription;

  constructor(
    private router: Router,
    private dialog: MatDialog,
    private wowService: NgwWowService
  ) {
    this.router.events.
    pipe(
      filter(event => event instanceof NavigationEnd)
    )
      .subscribe(event => {
      // Reload WoW animations when done navigating to page,
      // but you are free to call it whenever/wherever you like
      this.wowService.init();
    });
  }

  ngOnInit(): void {
    // you can subscribe to WOW observable to react when an element is revealed
    this.wowSubscription = this.wowService.itemRevealed$.subscribe(
      (item: HTMLElement) => {
        // do whatever you want with revealed element
      });
  }

  ngOnDestroy(): void {
    // unsubscribe (if necessary) to WOW observable to prevent memory leaks
    this.wowSubscription.unsubscribe();
  }

  openPlayground(): void {
    this.dialog.open(DemoWarningComponent, {});
  }

}
