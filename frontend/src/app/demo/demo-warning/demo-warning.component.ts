import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-demo-warning',
  templateUrl: './demo-warning.component.html',
  styleUrls: ['./demo-warning.component.scss']
})
export class DemoWarningComponent {

  constructor(private dialogRef: MatDialogRef<DemoWarningComponent>,
              private router: Router) { }

  redirectToPlayground(): void {
    this.router.navigate(['/landingpage']);
    this.dialogRef.close();
  }

  abort(): void {
    this.dialogRef.close();
  }

}
