import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { OkrChildUnit } from '../../../shared/model/ui/OrganizationalUnit/okr-child-unit';
import { OkrUnitService } from '../../../shared/services/mapper/okr-unit.service';
import { OkrBranch } from '../../../shared/model/ui/OrganizationalUnit/okr-branch';

@Component({
  selector: 'app-okr-child-unit-preview-button',
  templateUrl: './okr-child-unit-preview-button.component.html',
  styleUrls: ['./okr-child-unit-preview-button.component.scss']
})
export class OkrChildUnitPreviewButtonComponent implements OnInit {
  @Input() unitId: number;
  @Input() title: string = 'Department';

  childUnit$: Observable<OkrChildUnit>;

  constructor(private okrUnitService: OkrUnitService) {
  }

  ngOnInit(): void {
    this.childUnit$ = this.okrUnitService.getOkrChildUnitById$(this.unitId);
  }

  isOkrBranch(okrChildUnit: OkrChildUnit): boolean {
    return okrChildUnit instanceof OkrBranch;
  }
}
