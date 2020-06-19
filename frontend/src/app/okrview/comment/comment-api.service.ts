import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment, CommentId } from './comment';
import { ApiHttpService } from '../../core/services/api-http.service';
import { KeyResultId } from '../../shared/model/id-types';

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
