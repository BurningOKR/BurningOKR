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
        'BurningOKR is now also available in English!',
        'POST Requests to log errors in the frontend no longer return a 401 error.',
        'BurningOKR can now be configured to not use Topic Sponsors.',
        'Other minor improvements and bug fixes.',
        'Our demo website is now online: http://burningokr.org'
      ]
    },
    {
      version: '1.0.2 (05.10.2020)',
      changes: [
        'Newly created users can now be added to a team without having to reload the page first.',
        'After updating your username, it will now be displayed correctly in the top right corner of the navigation bar.',
        'Creating a cycle when there are Structures (Branches) in the company, will no longer cause errors.'
      ]
    }
  ];

  constructor(private dialogRef: MatDialogRef<VersionFormComponent>) {
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

}
