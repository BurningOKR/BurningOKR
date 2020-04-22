import { UserId } from '../id-types';

export class AdminUser {
  id: UserId;

  constructor(id: UserId) {
    this.id = id;
  }
}
