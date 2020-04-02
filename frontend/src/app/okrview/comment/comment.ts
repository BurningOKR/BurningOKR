export type CommentId = number;

export interface Comment {
  noteId?: CommentId;
  userId: string;
  noteBody: string;
  date: number[];
  parentKeyResultId?: number;
}
