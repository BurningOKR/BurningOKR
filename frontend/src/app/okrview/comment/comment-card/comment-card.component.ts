import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { User } from '../../../shared/model/api/user';
import { ViewComment } from '../../../shared/model/ui/view-comment';
import { CurrentUserService } from '../../../core/services/current-user.service';
import { CommentMapperService } from '../comment-mapper.service';

@Component({
  selector: 'app-comment-card',
  templateUrl: './comment-card.component.html',
  styleUrls: ['./comment-card.component.scss']
})
export class CommentCardComponent implements OnInit {
  @Input() comment: ViewComment;

  @Output() deleteComment = new EventEmitter<ViewComment>();

  isEditingComment: boolean = false;
  isSavingEdit: boolean = false;
  isOwnerOfNote$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  editedCommentText: string;

  constructor(private currentUserService: CurrentUserService, private commentMapperService: CommentMapperService) {
  }

  ngOnInit(): void {
    this.currentUserService.getCurrentUser$()
      .pipe(
        map((user: User) => {
          return user.id === this.comment.userId;
        }),
        take(1)
      )
      .subscribe((isOwner: boolean) => {
        this.isOwnerOfNote$.next(isOwner);
      });
  }

  clickedDeleteComment(): void {
    this.deleteComment.emit(this.comment);
  }

  clickedEditComment(): void {
    this.isEditingComment = true;
    this.editedCommentText = this.comment.text;
  }

  clickedCancelEditComment(): void {
    this.isEditingComment = false;
  }

  clickedSaveComment(): void {
    this.comment.text = this.editedCommentText;
    this.isSavingEdit = true;
    this.isEditingComment = true;

    this.commentMapperService
      .updateComment$(this.comment)
      .pipe(take(1))
      .subscribe(editedComment => {
        this.comment.text = editedComment.text;
        this.isSavingEdit = false;
        this.isEditingComment = false;
      });
  }
}
