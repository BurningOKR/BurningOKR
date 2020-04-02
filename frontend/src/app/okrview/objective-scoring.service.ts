import { Injectable } from '@angular/core';
import { ConfigurationManagerService } from '../core/settings/configuration-manager.service';

export enum ObjectiveScore {
  OnTrack = 0,
  InDanger = 1,
  OffTrack = 2
}

@Injectable({
  providedIn: 'root'
})
export class ObjectiveScoringService {

  scoringValues: number[] = [];

  constructor(private configurationManager: ConfigurationManagerService) {
    this.scoringValues.push(
      +this.configurationManager.objectiveProgressGreenYellowThreshold.value,
      +this.configurationManager.objectiveProgressYellowRedThreshold.value);
  }

  getObjectiveScoreForProgress(normalizedObjectiveProgress: number, normalizedCycleProgress: number): ObjectiveScore {
    const progressDifference: number = normalizedObjectiveProgress - normalizedCycleProgress;

    if (progressDifference < this.scoringValues[1]) {
      return ObjectiveScore.OffTrack;
    } else if (progressDifference < this.scoringValues[0]) {
      return ObjectiveScore.InDanger;
    } else {
      return ObjectiveScore.OnTrack;
    }
  }
}
