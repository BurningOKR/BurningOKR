import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { forkJoin, NEVER, Observable } from 'rxjs';
import { CurrentUserService } from '../../services/current-user.service';
import { SettingsForm } from './settings-form';

@Component({
  selector: 'app-settings-form',
  templateUrl: './settings-form.component.html',
  styleUrls: ['./settings-form.component.css']
})
export class SettingsFormComponent implements OnInit {

  isCurrentUserAdmin$: Observable<boolean>;

  adminSettingsValid: boolean = true;
  userSettingsValid: boolean = true;

  @ViewChildren(SettingsForm)
  children?: QueryList<SettingsForm>;

  constructor(private dialogRef: MatDialogRef<SettingsFormComponent>,
              private currentUserService: CurrentUserService) { }

  ngOnInit(): void {
    this.isCurrentUserAdmin$ = this.currentUserService.isCurrentUserAdmin$();
  }

  onSave(): void {
    this.dialogRef.close(
      forkJoin(this.children.map((form: SettingsForm) => form.createUpdate$()))
    );
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

}
