import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { filter, map, take } from 'rxjs/operators';
import { Comment } from './comment';
import { ViewComment } from '../../shared/model/ui/view-comment';
import { CommentApiService } from './comment-api.service';
import { UserService } from '../../shared/services/helper/user.service';
import { ViewCommentParentType } from '../../shared/model/ui/view-comment-parent-type';
import { CommentId } from '../../shared/model/id-types';

@Injectable({
  providedIn: 'root'
})
export class CommentMapperService {
  constructor(private commentService: CommentApiService, private userMapperService: UserService) {
  }

  getCommentsFromParentObject$(viewCommentParentType: ViewCommentParentType,
                               parentObjectId: number): Observable<ViewComment[]> {
    return this.commentService
      .getCommentForParentObject$(viewCommentParentType, parentObjectId)
      .pipe(map((commentList: Comment[]) => this.mapCommentListDto(commentList)));
  }

  createComment$(viewCommentParentType: ViewCommentParentType, parentObjectId: number, newComment: ViewComment): Observable<ViewComment> {
    return this.commentService.postCommentForParentObject$(viewCommentParentType, parentObjectId, this.mapViewComment(newComment))
      .pipe(
      filter(val => val !== undefined),
      map((postedComment: Comment) => this.mapCommentDto(postedComment))
    );
  }

  updateComment$(comment: ViewComment): Observable<ViewComment> {
    return this.commentService
      .putComment$(this.mapViewComment(comment))
      .pipe(map((updatedComment: Comment) => this.mapCommentDto(updatedComment)));
  }

  deleteComment$(comment: ViewComment): Observable<boolean> {
    return this.commentService.deleteComment$(this.mapViewComment(comment));
  }

  mapCommentListDto(commentList: Comment[]): ViewComment[] {
    const mappedViewCommentList: ViewComment[] = [];
    if (commentList) {
      // Using manual for loop to decouple from fragile inconsistent JS standards
      for (const comment of commentList) {
        mappedViewCommentList.push(this.mapCommentDto(comment));
      }
    }

    mappedViewCommentList.sort((a, b) => {
      return a.date.getTime() - b.date.getTime();
    });

    return mappedViewCommentList;
  }

  mapCommentDto(comment: Comment): ViewComment {
    const date: Date = new Date(comment.date[0], comment.date[1] - 1, comment.date[2], comment.date[3], comment.date[4]);
    const viewComment: ViewComment = new ViewComment(comment.noteId, comment.userId, comment.noteBody, date);
    this.userMapperService
      .getUserById$(viewComment.userId)
      .pipe(take(1))
      .subscribe(user => {
        viewComment.userName = `${user.givenName} ${user.surname}`;
        viewComment.photo = user.photo;
      });

    return viewComment;
  }

  mapViewComment(viewComment: ViewComment): Comment {
    return {
      noteId: viewComment.id,
      noteBody: viewComment.text,
      userId: viewComment.userId,
      parentKeyResultId: 0,
      date: [
        Number(viewComment.date.getFullYear()),
        Number(viewComment.date.getMonth()) + 1,
        Number(viewComment.date.getDate()),
        Number(viewComment.date.getHours()),
        Number(viewComment.date.getMinutes()),
        Number(viewComment.date.getSeconds()),
        Number(viewComment.date.getMilliseconds())
      ]
    };
  }
}
