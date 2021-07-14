import { CommentId } from '../id-types';

// This interface needs to be implemented by every ParentType, which want to have comment functions
export interface ViewCommentRequiredAttributes {
  id: (number | any);
  commentIdList: CommentId[];
}
