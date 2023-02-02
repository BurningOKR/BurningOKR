import {
  Component,
  ComponentFactory,
  ComponentFactoryResolver,
  ComponentRef,
  OnInit,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';
import { INIT_STATE_NAME, InitState } from '../../../../shared/model/api/init-state';
import { CreateUserInitStateFormComponent } from './init-state-forms/create-user-init-state-form/create-user-init-state-form.component';
import { CompleteInitStateFormComponent } from './init-state-forms/complete-init-state-form/complete-init-state-form.component';
import { InitStateFormComponent } from './init-state-forms/init-state-form/init-state-form.component';
import { TypeOf } from '../../../../../typings';
import { InitService } from '../../../services/init.service';
import { SetOauthClientDetailsFormComponent } from './init-state-forms/set-oauth-client-details-form/set-oauth-client-details-form.component';
import { SetAzureAdminInitStateFormComponent } from './init-state-forms/set-azure-admin-init-state-form/set-azure-admin-init-state-form.component';

@Component({
  selector: 'app-init-state-view',
  templateUrl: './init-state-view.component.html',
  styleUrls: ['./init-state-view.component.scss']
})
export class InitStateViewComponent implements OnInit {

  @ViewChild('currentForm', {read: ViewContainerRef, static: true}) currentForm: ViewContainerRef;
  initState: InitState;
  componentRef: ComponentRef<InitStateFormComponent>;

  constructor(
    private componentFactoryResolver: ComponentFactoryResolver,
    private initService: InitService,
    ) {
  }

  ngOnInit(): void {
    this.initService.getInitState$()
      .subscribe(initState => {
      this.initState = initState;
      this.generateCurrentFormComponent();

    });
  }

  private generateCurrentFormComponent(): void {
    const componentFactory: ComponentFactory<InitStateFormComponent>
      = this.componentFactoryResolver.resolveComponentFactory(this.getComponentBasedOnCurrentInitState());

    this.componentRef = this.currentForm.createComponent(componentFactory);
    this.componentRef.instance.eventEmitter
      .subscribe(newInitState => {
      this.initState = newInitState;
      this.componentRef.destroy();
      this.generateCurrentFormComponent();
    });
  }

  private getComponentBasedOnCurrentInitState(): TypeOf<InitStateFormComponent> {
    switch (this.initState.initState) {
      case INIT_STATE_NAME.INITIALIZED:
        return CompleteInitStateFormComponent;
      case INIT_STATE_NAME.SET_OAUTH_CLIENT_DETAILS:
        return SetOauthClientDetailsFormComponent;
      case INIT_STATE_NAME.CREATE_USER:
        return CreateUserInitStateFormComponent;
      case INIT_STATE_NAME.NO_AZURE_ADMIN_USER:
        return SetAzureAdminInitStateFormComponent;
      default:
        return null;
    }
  }
}
