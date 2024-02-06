import { CommentId } from '../../shared/model/id-types';

export interface Comment {
  noteId?: CommentId;
  userId: string;
  noteBody: string;
  date: Date;
  parentKeyResultId?: number;
}
