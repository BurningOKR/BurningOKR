import {Component, Input, OnInit} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-language-picker',
  templateUrl: './language-picker.component.html',
  styleUrls: ['./language-picker.component.scss']
})
export class LanguagePickerComponent implements OnInit {

  constructor(
    public translateService: TranslateService,
  ) { }

  ngOnInit(): void {
  }

  languageChanged(event: any) {
    console.log(event);
    this.translateService.use(event.value);
  }

}
