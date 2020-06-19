import { Injectable } from '@angular/core';
import { ConfigurationMapper } from '../../shared/services/mapper/configuration.mapper';
import { forkJoin, Observable, ReplaySubject } from 'rxjs';
import { Configuration } from '../../shared/model/ui/configuration';
import { Fetchable } from '../../shared/decorators/fetchable.decorator';
import { map, take } from 'rxjs/operators';

@Fetchable()
@Injectable({
  providedIn: 'root'
})
export class ConfigurationManagerService implements Fetchable {

  private configuration$: ReplaySubject<Configuration[]> = new ReplaySubject<Configuration[]>();

  constructor(private configurationMapperService: ConfigurationMapper) {
  }

  getAllConfigurations$(): Observable<Configuration[]> {
    return this.configuration$.asObservable();
  }

  getMaxKeyResults$(): Observable<Configuration> {
    return this.getConfigurationByName$('max-key-results');
  }

  getObjectiveProgressGreenYellowThreshold$(): Observable<Configuration> {
    return this.getConfigurationByName$('objective-progress-green-yellow-threshold');
  }

  getObjectiveProgressYellowRedThreshold$(): Observable<Configuration> {
    return this.getConfigurationByName$('objective-progress-yellow-red-threshold');
  }

  fetchData(): void {
    this.configurationMapperService.getConfigurations$()
      .pipe(take(1))
      .subscribe((configurations: Configuration[]) => {
        this.configuration$.next(configurations);
      });
  }

  updateConfigurations$(configurations: Configuration[]): Observable<any> {
    const updateObservables: Observable<Configuration>[] = configurations.map((configuration: Configuration) => {
      return this.configurationMapperService.putConfiguration$(configuration);
    });
    this.configuration$.next(configurations);

    return forkJoin(updateObservables);
  }

  private getConfigurationByName$(name: string): Observable<Configuration> {
    return this.configuration$
      .pipe(
        map((configurations: Configuration[]) => {
          return configurations.find((configuration: Configuration) => configuration.name === name);
        })
      );
  }
}
