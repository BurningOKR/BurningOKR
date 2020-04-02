import { Component, Input, OnDestroy } from '@angular/core';
import { ViewObjective } from '../../shared/model/ui/view-objective';
import { ObjectiveViewMapper } from '../../shared/services/mapper/objective-view.mapper';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.scss']
})
export class ReviewComponent implements OnDestroy {

  @Input() objective: ViewObjective;

  inEditMode = false;
  private subscriptions: Subscription[] = [];

  constructor(private objectiveMapperService: ObjectiveViewMapper) {
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub: Subscription) => sub.unsubscribe());
    this.subscriptions = [];
  }

  toggleEditMode(): void {
    if (this.inEditMode === true) {
      this.subscriptions.push(
        this.objectiveMapperService.putObjective$(this.objective)
          .subscribe());
    }
    this.inEditMode = !this.inEditMode;
  }

}
