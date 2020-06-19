import { Injectable } from '@angular/core';
import { ConfigurationManagerService } from '../core/settings/configuration-manager.service';
import { Configuration } from '../shared/model/ui/configuration';

export enum ObjectiveScore {
  OnTrack = 0,
  InDanger = 1,
  OffTrack = 2
}

@Injectable({
  providedIn: 'root'
})
export class ObjectiveScoringService {

  greenYellowThreshold: number;
  yellowRedThreshold: number;

  constructor(private configurationManager: ConfigurationManagerService) {
    this.configurationManager.getObjectiveProgressGreenYellowThreshold$()
      .subscribe((configuration: Configuration) => {
        this.greenYellowThreshold = +configuration.value;
      });

    this.configurationManager.getObjectiveProgressYellowRedThreshold$()
      .subscribe((configuration: Configuration) => {
        this.yellowRedThreshold = +configuration.value;
      });
  }

  getObjectiveScoreForProgress(normalizedObjectiveProgress: number, normalizedCycleProgress: number): ObjectiveScore {
    const progressDifference: number = normalizedObjectiveProgress - normalizedCycleProgress;

    if (progressDifference < this.yellowRedThreshold) {
      return ObjectiveScore.OffTrack;
    } else if (progressDifference < this.greenYellowThreshold) {
      return ObjectiveScore.InDanger;
    } else {
      return ObjectiveScore.OnTrack;
    }
  }
}
