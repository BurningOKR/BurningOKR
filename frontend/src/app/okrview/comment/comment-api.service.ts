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

  getCommentsForKeyResult$(keyResultId: KeyResultId): Observable<Comment[]> {
    return this.api.getData$(`keyresults/${keyResultId}/notes`);
  }

  getCommentForParentObject$(viewCommentParentType: ViewCommentParentType, parentObjectId: number): Observable<Comment[]> {
    return this.api.getData$(`notes/${viewCommentParentType.toString()}/${parentObjectId}`);
  }

  postComment$(keyResultId: KeyResultId, comment: Comment): Observable<Comment> {
    return this.api.postData$(`keyresults/${keyResultId}/notes`, comment);
  }

  putComment$(comment: Comment): Observable<Comment> {
    return this.api.putData$(`notes/${comment.noteId}`, comment);
  }

  deleteComment$(comment: Comment): Observable<boolean> {
    return this.api.deleteData$(`notes/${comment.noteId}`);
  }
}
