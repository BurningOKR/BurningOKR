import { Component, Inject, OnInit } from '@angular/core';
import { ViewKeyResult } from '../../../shared/model/ui/view-key-result';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { take } from 'rxjs/operators';
import { ViewComment } from '../../../shared/model/ui/view-comment';
import { CommentMapperService } from '../comment-mapper.service';
import { ViewCommentParentType } from '../../../shared/model/ui/view-comment-parent-type';

export interface CommentViewDialogFormData {
  componentTypeTitle: string;
  componentName: string;
  viewCommentParentType: ViewCommentParentType;
  parentId: number;
  onUpdateCommentIdList?: number[];
}

@Component({
  selector: 'app-comment-view-dialog',
  templateUrl: './comment-view-dialog.component.html',
  styleUrls: ['./comment-view-dialog.component.scss']
})
export class CommentViewDialogComponent implements OnInit, CommentViewDialogFormData {

  componentTypeTitle: string;
  componentName: string;
  viewCommentParentType: ViewCommentParentType;
  parentId: number;
  onUpdateCommentIdList: number[];

  parentKeyResult: ViewKeyResult;

  commentList: ViewComment[];
  newCommentText: string = '';
  isPostingComment: boolean = false;

  constructor(
    public dialogRef: MatDialogRef<CommentViewDialogComponent>,
    public commentMapperService: CommentMapperService,
    @Inject(MAT_DIALOG_DATA) private formData: (CommentViewDialogFormData | any)
  ) {
    this.componentTypeTitle = formData.componentTypeTitle;
    this.componentName = formData.componentName;
    this.viewCommentParentType = formData.viewCommentParentType;
    this.parentId = formData.parentId;
    this.commentList = [];
    this.onUpdateCommentIdList = formData.hasOwnProperty('onUpdateCommentIdList') ? formData.onUpdateCommentIdList : [];
  }

  ngOnInit(): void {
    this.loadCommentList();
  }

  clickedClose(): void {
    this.dialogRef.close();
  }

  // TODO Subscription überarbeiten
  loadCommentList(): void {
    this.commentMapperService
      .getCommentsFromParentObject$(this.viewCommentParentType, this.parentId)
      .pipe(take(1))
      .subscribe(commentList => (this.commentList = commentList));
  }

  canPostNewComment(): boolean {
    return !(this.newCommentText.length < 3 || this.isPostingComment);
  }

  postNewComment(): void {
    if (this.canPostNewComment()) {
      this.isPostingComment = true;
      const newComment: ViewComment = new ViewComment(1, '', this.newCommentText, new Date());

      this.commentMapperService
        .createComment$(this.viewCommentParentType, this.parentId, newComment)
        .pipe(take(1))
        .subscribe(createdComment => {
          this.onUpdateCommentIdList.push(createdComment.id);
          this.isPostingComment = false;
          this.newCommentText = '';
          this.loadCommentList();
        });
    }
  }

  queryDeleteComment(commentToDelete: ViewComment): void {
    this.commentMapperService.deleteComment$(commentToDelete)
      .pipe(take(1))
      .subscribe(commentStillExists => { if (!commentStillExists) {
        this.commentDeleted(commentToDelete); }});
  }

  commentDeleted(deletedComment: ViewComment): void {
    const indexOfComment: number = this.commentList.indexOf(deletedComment);
    this.commentList.splice(indexOfComment, 1);
    const indexOfCommentId: number = this.onUpdateCommentIdList.indexOf(deletedComment.id);
    this.onUpdateCommentIdList.splice(indexOfCommentId, 1);
  }
}
