import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment } from './comment';
import { ApiHttpService } from '../../core/services/api-http.service';
import { CommentId, KeyResultId } from '../../shared/model/id-types';
import { ViewCommentParentType } from '../../shared/model/ui/view-comment-parent-type';

@Injectable({
  providedIn: 'root'
})
export class CommentApiService {
  constructor(private api: ApiHttpService) {}

  getCommentById$(id: CommentId): Observable<Comment> {
    return this.api.getData$(`notes/${id}`);
  }

  getCommentsForParent$(parentObjectUrlPath: string, keyResultId: KeyResultId): Observable<Comment[]> {
    return this.api.getData$(`${parentObjectUrlPath}/${keyResultId}/notes`);
  }

  getCommentForParentObject$(viewCommentParentType: ViewCommentParentType, parentObjectId: number): Observable<Comment[]> {
    return this.getCommentsForParent$(this.getParentObjectUrlPath(viewCommentParentType), parentObjectId);
  }

  postComment$(parentObjectUrlPath: string, keyResultId: KeyResultId, comment: Comment): Observable<Comment> {
    return this.api.postData$(`${parentObjectUrlPath}/${keyResultId}/notes`, comment);
  }

  postCommentForParentObject$(viewCommentParentType: ViewCommentParentType, parentObjectId: number, comment: Comment):
        Observable<Comment> {
    return this.postComment$(this.getParentObjectUrlPath(viewCommentParentType), parentObjectId, comment);
  }

  putComment$(comment: Comment): Observable<Comment> {
    return this.api.putData$(`notes/${comment.noteId}`, comment);
  }

  deleteComment$(comment: Comment): Observable<boolean> {
    return this.api.deleteData$(`notes/${comment.noteId}`);
  }

  getParentObjectUrlPath(viewCommentParentType: ViewCommentParentType): string {
    switch (viewCommentParentType) {
      case ViewCommentParentType.keyResult:
        return 'keyResults';
        break;
      case ViewCommentParentType.topicDraft:
        return 'topicDrafts';
        break;
      default:
        return '';
        break;
    }
  }
}
