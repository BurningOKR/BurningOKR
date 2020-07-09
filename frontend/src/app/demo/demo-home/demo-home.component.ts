import { Component } from '@angular/core';
import { MatDialog } from '@angular/material';
import { DemoWarningComponent } from '../demo-warning/demo-warning.component';

@Component({
  selector: 'app-demo-home',
  templateUrl: './demo-home.component.html',
  styleUrls: ['./demo-home.component.scss']
})
export class DemoHomeComponent {

  constructor(private dialog: MatDialog) {
  }

  openPlayground(): void {
    this.dialog.open(DemoWarningComponent, {});
  }
}
