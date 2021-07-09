import { Component, Input, OnInit } from '@angular/core';
import { status } from '../../model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { I18n } from '@ngx-translate/i18n-polyfill';

@Component({
    selector: 'app-status-dot',
    templateUrl: './status-dot.component.html',
    styleUrls: ['./status-dot.component.css']
})
export class StatusDotComponent implements OnInit {

    statusTooltip: string;
    enumStatus = status;
    @Input() state: status = status.draft;

    constructor(private i18n: I18n) {
    }

    ngOnInit(): void {
        this.statusTooltip = this.geti18nTooltip(this.state);
    }

    geti18nTooltip(currentStatus: status): string {
        switch (currentStatus) {
            case status.submitted:
                return this.i18n({
                    id: 'statusTooltipSubmitted',
                    description: 'statusTooltipSubmittedText',
                    value: 'Eingereicht'
                });
            case status.approved:
                return this.i18n({
                    id: 'statusTooltipApproved',
                    description: 'statusTooltipApprovedText',
                    value: 'Genehmigt'
                });
            case status.rejected:
                return this.i18n({
                    id: 'statusTooltipRejected',
                    description: 'statusTooltipRejectedText',
                    value: 'Abgelehnt'
                });
            default:
                return this.i18n({
                    id: 'statusTooltipDraft',
                    description: 'statusTooltipDraftText',
                    value: 'Entwurf'
                });
        }

    }
}
