import { Observable, of } from 'rxjs';
import { Configuration } from '../model/ui/configuration';

export class ConfigurationManagerServiceMock {
  getAllConfigurations$(): Observable<Configuration[]> {
    return of();
  }

  getMaxKeyResults$(): Observable<Configuration> {
    return of();
  }

  getObjectiveProgressGreenYellowThreshold$(): Observable<Configuration> {
    return of();
  }

  getObjectiveProgressYellowRedThreshold$(): Observable<Configuration> {
    return of();
  }

  fetchData(): void {
    return;
  }

  updateConfigurations$(configurations: Configuration[]): Observable<any> {
    return of();
  }

  private getConfigurationByName$(name: string): Observable<Configuration> {
    return of();
  }
}
