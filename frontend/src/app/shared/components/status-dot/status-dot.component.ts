import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { status } from '../../model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-status-dot',
  templateUrl: './status-dot.component.html',
  styleUrls: ['./status-dot.component.scss'],
})
export class StatusDotComponent implements OnInit, OnChanges {

  @Input() state: status = status.draft;
  enumStatus = status;
  statusTooltip$: Observable<string>;

  constructor(private translate: TranslateService) {
  }

  ngOnInit(): void {
    this.statusTooltip$ = this.getTranslateTooltip$(this.state);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.statusTooltip$ = this.getTranslateTooltip$(changes.state.currentValue);
  }

  getTranslateTooltip$(currentStatus: status): Observable<string> {
    switch (currentStatus) {
      case status.submitted:
        return this.translate.stream('status-dot.submitted');
      case status.approved:
        return this.translate.stream('status-dot.approved');
      case status.rejected:
        return this.translate.stream('status-dot.rejected');
      default:
        return this.translate.stream('status-dot.draft');
    }

  }
}
