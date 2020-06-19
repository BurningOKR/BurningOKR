import { CommentId } from '../../../okrview/comment/comment';
import { UserId } from '../id-types';

export class ViewComment {
  id: CommentId;
  userId: UserId;
  userName: string;
  date: Date;
  text: string;
  photo: string;

  constructor(id: number, userId: string, text: string, date: Date) {
    this.id = id;
    this.userId = userId;
    this.text = text;
    this.photo = undefined;
    this.userName = 'Lade Nutzer';
    this.date = date;
  }
}
