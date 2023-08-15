import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

// (window as any).retain = [ CustomChartComponent ];

if (environment.production) {
  enableProdMode();
}

platformBrowserDynamic()
  .bootstrapModule(AppModule, {
    providers: [],
  })
  // eslint-disable-next-line no-console
  .catch(err => console.log(err));
