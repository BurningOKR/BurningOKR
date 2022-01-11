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
  language: string;

  private subscriptions: Subscription[] = [];

  constructor(
    public translate: TranslateService,
    private dialogRef: MatDialogRef<PickLanguageComponent>,
  ) { }

  ngOnInit(): void {
    this.subscriptions.push(this.translate.stream('okr-toolbar.menu.language-picker').subscribe(text =>
      this.title = text
    ));
    this.subscriptions.push(this.translate.stream('dialog-component.save').subscribe(text =>
      this.saveAndCloseLabel = text
    ));
    this.language = this.translate.currentLang;
  }

  languageChanged(event: any) {
    this.language = event.value;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  clickedDone(): void {
    this.translate.use(this.language);
    this.dialogRef.close();
  }
}
