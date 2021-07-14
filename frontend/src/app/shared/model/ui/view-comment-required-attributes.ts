import { CommentId } from '../id-types';

export interface ViewCommentRequiredAttributes {
  id: (number | any);
  commentIdList: CommentId[];
}
