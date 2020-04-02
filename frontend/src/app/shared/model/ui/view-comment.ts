import { UserId } from '../api/user';
import { CommentId } from '../../../okrview/comment/comment';

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
