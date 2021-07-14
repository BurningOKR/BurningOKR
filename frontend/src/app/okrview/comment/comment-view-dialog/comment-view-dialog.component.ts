import { Component, Inject, OnInit } from '@angular/core';
import { ViewKeyResult } from '../../../shared/model/ui/view-key-result';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { take } from 'rxjs/operators';
import { ViewComment } from '../../../shared/model/ui/view-comment';
import { CommentMapperService } from '../comment-mapper.service';
import { CommentId } from '../../../shared/model/id-types';
import { ViewCommentParentType } from '../../../shared/model/ui/view-comment-parent-type';
import { ViewCommentRequiredAttributes } from '../../../shared/model/ui/view-comment-required-attributes';

export interface CommentViewDialogFormData {
  componentTypeTitle: string;
  componentName: string;
  viewCommentParentType: ViewCommentParentType;
  parentObject: ViewCommentRequiredAttributes;
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
  parentObject: ViewCommentRequiredAttributes;

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
    this.parentObject = formData.parentObject;
  }

  ngOnInit(): void {
    console.log(this.parentObject.commentIdList);
    this.loadCommentList();
  }

  clickedClose(): void {
    this.dialogRef.close();
  }

  loadCommentList(): void {
    if (this.parentObject.commentIdList.length !== 0) {
      this.commentMapperService
        .getCommentsFromKeyResult$(this.parentObject.id)
        .pipe(take(1))
        .subscribe(commentList => (this.commentList = commentList));
    }
  }

  canPostNewComment(): boolean {
    return !(this.newCommentText.length < 3 || this.isPostingComment);
  }

  postNewComment(): void {
    if (this.canPostNewComment()) {
      this.isPostingComment = true;
      const newComment: ViewComment = new ViewComment(1, '', this.newCommentText, new Date());

      this.commentMapperService
        .createComment$(this.parentObject.id, newComment)
        .pipe(take(1))
        .subscribe(createdComment => {
          this.parentObject.commentIdList.push(createdComment.id);
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
    let indexOfComment: number = this.commentList.indexOf(deletedComment);
    this.commentList.splice(indexOfComment, 1);
    indexOfComment = this.parentObject.commentIdList.indexOf(deletedComment.id);
    this.parentObject.commentIdList.splice(indexOfComment, 1);
  }
}
