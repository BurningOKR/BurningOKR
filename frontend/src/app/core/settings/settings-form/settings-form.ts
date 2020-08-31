import { Observable, of } from 'rxjs';

export abstract class SettingsForm {
  /**
   * creates an observable to subscribe to, which sends the update of the configuration to the server.
   */
  abstract createUpdate$(): Observable<any>;

  /**
   * This method is called before the dialog is being closed.
   * returns true when the form can be closed, false otherwise
   */
  canClose$(): Observable<boolean> {
    return of(true);
  }
}
