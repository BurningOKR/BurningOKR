import { Pipe, PipeTransform } from '@angular/core';
import { User } from '../../shared/model/api/user';

@Pipe({
  name: 'adminUserIds',
})
export class AdminUserIdsPipe implements PipeTransform {

  transform(users: User[], shouldUpdate: any): string[] {
    return users.Select(user => user.id);
  }

}
