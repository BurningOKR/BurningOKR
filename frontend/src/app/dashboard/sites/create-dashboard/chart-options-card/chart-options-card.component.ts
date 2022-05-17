import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { OkrDepartment } from '../../../../shared/model/ui/OrganizationalUnit/okr-department';
import { ChartCreationOptionsDto, ChartTypeEnumDropDownRecord } from '../../../model/dto/chart-creation-options.dto';

@Component({
  selector: 'app-chart-options-card',
  templateUrl: './chart-options-card.component.html',
  styleUrls: ['./chart-options-card.component.scss']
})
export class ChartOptionsCardComponent {
  @Input() chart: ChartCreationOptionsDto;
  @Input() allTeams$: Observable<OkrDepartment[]>;
  @Output() clickedDelete: EventEmitter<ChartCreationOptionsDto> = new EventEmitter<ChartCreationOptionsDto>();

  chartTypeRecord = ChartTypeEnumDropDownRecord;

  getTeamsOfChart(allTeams: OkrDepartment[]): OkrDepartment[] {
    return allTeams.filter(team => this.chart.teamIds.includes(team.id));
  }
}
