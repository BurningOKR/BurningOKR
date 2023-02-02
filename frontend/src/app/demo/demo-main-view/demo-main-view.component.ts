import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DemoWarningComponent } from '../demo-warning/demo-warning.component';

@Component({
  selector: 'app-demo-main-view',
  templateUrl: './demo-main-view.component.html',
  styleUrls: ['./demo-main-view.component.scss'],
})
export class DemoMainViewComponent {

  constructor(private dialog: MatDialog) {
  }

  openPlayground(): void {
    this.dialog.open(DemoWarningComponent, {});
  }

}
