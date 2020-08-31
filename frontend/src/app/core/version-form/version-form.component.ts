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
      version: '1.0.0 (18.06.2020)',
      changes: [
        'Initial Release.'
      ]
    },
    {
      version: '1.0.1 (01.09.2020)',
      changes: [
        'POST Requests to log errors in the frontend no longer return a 401 error.',
        'BurningOKR can now be configured to not use Topic Sponsors.',
        'Other minor improvements and bug fixes.',
        'Our Demowebsite is now online: http://burningokr.org'
      ]
    }
  ];

  constructor(private dialogRef: MatDialogRef<VersionFormComponent>) {
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

}
