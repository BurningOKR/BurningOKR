import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { status } from '../../model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'app-status-dot',
    templateUrl: './status-dot.component.html',
    styleUrls: ['./status-dot.component.css']
})
export class StatusDotComponent implements OnInit, OnChanges {

  @Input() state: status = status.draft;
  statusTooltip: string;
  enumStatus = status;

    constructor(private translate: TranslateService) {
    }

    ngOnInit(): void {
        this.statusTooltip = this.geti18nTooltip(this.state);
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.statusTooltip = this.geti18nTooltip(changes.state.currentValue);
    }

    geti18nTooltip(currentStatus: status): string {
        switch (currentStatus) {
            case status.submitted:
                return this.translate.instant('status-dot.submitted');
            case status.approved:
                return this.translate.instant('status-dot.approved');
            case status.rejected:
                return this.translate.instant('status-dot.rejected');
            default:
                return this.translate.instant('status-dot.draft');
        }

    }
}
