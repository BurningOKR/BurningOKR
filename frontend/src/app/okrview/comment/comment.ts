import { CommentId } from '../../shared/model/id-types';

export interface Comment {
  noteId?: CommentId;
  userId: string;
  noteBody: string;
  date: number[];
  parentKeyResultId?: number;
}
