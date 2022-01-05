import {Component, OnDestroy, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Subscription} from 'rxjs';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-pick-language',
  templateUrl: './pick-language.component.html',
  styleUrls: ['./pick-language.component.scss']
})
export class PickLanguageComponent implements OnInit, OnDestroy {

  title: string;
  saveAndCloseLabel: string;

  private subscriptions: Subscription[] = [];

  constructor(
    public translateService: TranslateService,
    private dialogRef: MatDialogRef<PickLanguageComponent>,
  ) { }

  ngOnInit(): void {
    this.subscriptions.push(this.translateService.stream('okr-toolbar.menu.language-picker').subscribe(text =>
      this.title = text
    ));
    this.subscriptions.push(this.translateService.stream('dialog-component.save').subscribe(text =>
      this.saveAndCloseLabel = text
    ));
  }

  languageChanged(event: any) {
    console.log(event);
    this.translateService.use(event.value);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  clickedDone(): void {
    this.dialogRef.close();
  }
}
