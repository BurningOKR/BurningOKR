import { Observable } from 'rxjs';
import { UserSettings } from '../../../shared/model/ui/user-settings';
import { Configuration } from '../../../shared/model/ui/configuration';

export abstract class SettingsForm {
  abstract createUpdate$(): Observable<UserSettings | Configuration>;
}
