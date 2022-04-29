import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs/internal/Observable';
import {OkrTranslationHelperService} from '../../../shared/services/helper/okr-translation-helper.service';

@Component({
  selector: 'app-pick-language',
  templateUrl: './pick-language.component.html',
  styleUrls: ['./pick-language.component.scss'],
})
export class PickLanguageComponent implements OnInit {
  title$: Observable<string>;
  saveAndCloseLabel$: Observable<string>;

  language: string;

  constructor(
    public translate: TranslateService,
    private okrTranslationHelper: OkrTranslationHelperService,
    private dialogRef: MatDialogRef<PickLanguageComponent>,
  ) {
  }

  ngOnInit(): void {
    this.title$ = this.translate.stream('okr-toolbar.menu.language-picker');
    this.saveAndCloseLabel$ = this.translate.stream('okr-toolbar.menu.language-picker');
    this.language = this.translate.currentLang;
  }

  languageChanged(event: any) {
    this.language = event.value;
  }

  clickedDone(): void {
    this.okrTranslationHelper.changeCurrentLanguageTo(this.language);
    this.dialogRef.close();
  }
}
