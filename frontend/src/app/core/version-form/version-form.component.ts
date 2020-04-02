import { Component } from '@angular/core';
import { NEVER } from 'rxjs';
import { MatDialogRef } from '@angular/material/dialog';
import { ChangeLog } from '../../shared/model/ui/change-log';

@Component({
  selector: 'app-version-form',
  templateUrl: './version-form.component.html',
  styleUrls: ['./version-form.component.scss']
})
export class VersionFormComponent {
  versionChanges: ChangeLog[] = [
    {
      version: '1.0 (01.03.2020)',
      changes: [
        'Inititaler Release.'
      ]
    }
  ];

  constructor(private dialogRef: MatDialogRef<VersionFormComponent>) {
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

}
