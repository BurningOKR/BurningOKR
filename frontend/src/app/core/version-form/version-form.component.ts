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
      version: '1.3.7 (12.08.2021)',
      changes: [
          'Refactoring and cleanup',
      ]
    },
    {
      version: '1.3.6 (09.08.2021)',
      changes: [
          'Topicdrafts with comments can now be deleted',
          'Optimization',
      ]
    },
    {
      version: '1.3.5 (06.08.2021)',
      changes: [
        'Fix: fixed migration scripts',
        'Comment objectives: all users can now comment on objectives',
      ]
    },
    {
      version: '1.3.4 (03.08.2021)',
      changes: [
          'Topic-Draft Creation: Initiator is now prefilled with the logged in User on creation.',
          'Objective comments: User can now comment on objectives',
          'Comment window: is now unified',
      ]
    },
    {
      version: '1.3.3 (15.07.2021)',
      changes: [
        'renamed acceptance criteria to description',
        'added tooltip for topic-draft status'
      ]
    },
    {
      version: '1.3.2 (25.06.2021)',
      changes: [
        'some minor CI changes'
      ]
    },
    {
      version: '1.3.1 (24.06.2021)',
      changes: [
        'Non-implemented features from submitted-topic-draft-action-buttons are disabled'
      ]
    },
    {
      version: '1.3.0 (23.06.2021)',
      changes: [
        'New Feature: display the details of a topic draft',
        'You can now view the details of a topic draft in a new pop-up window',
        'The edit button is only enabled if you are an admin or the initiator of the topic draft'
      ]
    },
    {
      version: '1.2.0 (22.06.2021)',
      changes: [
        'New Feature: Topic-draft',
        'It is now possible to create topic drafts',
        'You can create a topic draft in the Add menu of the structure',
        'Via the main menu in the upper right corner it is possible to get an overview of the topic drafts',
        'Submitting and editing is not yet possible'
      ]
    },
    {
      version: '1.1.2 (02.06.2021)',
      changes: [
        'Fixed an issue with the docker installation (Issue #20, JDK).'
      ]
    },
    {
      version: '1.1.1 (31.05.2021)',
      changes: [
        'Proper handling of active directory credentials.'
      ]
    },
    {
      version: '1.1.0 (10.05.2021)',
      changes: [
        'BurningOKR now allows teams to manage tasks on a task board to further improve productivity and is offering a greater overview.'
      ]
    },
    {
      version: '1.0.4 (08.02.2021)',
      changes: [
        'Descriptions can now be added to OKR Teams',
        'Minor UI fixes'
      ]
    },
    {
      version: '1.0.3 (12.11.2020)',
      changes: [
        'BurningOKR is now responsive and mobile-ready!',
        'Key Results can now contain milestones',
        'Objectives can now have a contact person',
        'Importing users via CSV checks for validity',
        'Added better error handling for 404 errors',
        'Minor UI fixes'
      ]
    },
    {
      version: '1.0.2 (05.10.2020)',
      changes: [
        'Newly created users can now be added to a team without having to reload the page first.',
        'After updating your username, it will now be displayed correctly in the top right corner of the navigation bar.',
        'Creating a cycle when there are Structures (Branches) in the company, will no longer cause errors.'
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
      version: '1.0.0 (18.06.2020)',
      changes: [
        'Initial Release.'
      ]
    }
  ];

  constructor(private dialogRef: MatDialogRef<VersionFormComponent>) {
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

}
