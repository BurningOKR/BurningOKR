import { TranslateModule } from '@ngx-translate/core';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthRoutingModule } from './auth-routing.module';
import { LogoutComponent } from './logout/logout.component';
import { MatCardModule } from '@angular/material/card';

@NgModule({
  declarations: [
    LogoutComponent,
  ],
  imports: [TranslateModule,
    CommonModule,
    AuthRoutingModule,
    MatCardModule,
  ],
})
export class AuthModule {
}
