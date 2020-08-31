import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { combineLatest, forkJoin, NEVER, Observable } from 'rxjs';
import { CurrentUserService } from '../../services/current-user.service';
import { SettingsForm } from './settings-form';
import { map } from 'rxjs/operators';

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
    this.canClose$()
      .subscribe((canClose: boolean) => {
        if (canClose) {
          this.dialogRef.close(
            forkJoin(this.children.map((form: SettingsForm) => form.createUpdate$()))
          );
        }
      });
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

  private canClose$(): Observable<boolean> {
    return combineLatest(this.children.map((form: SettingsForm) => form.canClose$()))
      .pipe(
        map((values: boolean[]) => {
          return values.All(value => value); // check if all forms returned true
        })
      );
  }

}
