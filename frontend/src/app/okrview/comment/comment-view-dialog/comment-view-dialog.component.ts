import { Component, Inject, OnInit } from '@angular/core';
import { ViewKeyResult } from '../../../shared/model/ui/view-key-result';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { take } from 'rxjs/operators';
import { ViewComment } from '../../../shared/model/ui/view-comment';
import { CommentMapperService } from '../comment-mapper.service';
import { SubmittedTopicDraftDetailsFormData } from '../../../submitted-topic-drafts/submitted-topic-draft-details/submitted-topic-draft-details.component';
import { CommentId } from '../../../shared/model/id-types';

export interface CommentViewDialogFormData {
  componentType: string;
  componentName: string;
  commentIdList: CommentId[];
}

@Component({
  selector: 'app-comment-view-dialog',
  templateUrl: './comment-view-dialog.component.html',
  styleUrls: ['./comment-view-dialog.component.scss']
})
export class CommentViewDialogComponent implements OnInit, CommentViewDialogFormData {

  commentIdList: CommentId[];

  parentKeyResult: ViewKeyResult;

  commentList: ViewComment[];
  newCommentText: string = '';
  isPostingComment: boolean = false;
  componentType: string;
  componentName: string;

  constructor(
    public dialogRef: MatDialogRef<CommentViewDialogComponent>,
    public commentMapperService: CommentMapperService,
    @Inject(MAT_DIALOG_DATA) private formData: (CommentViewDialogFormData | any)
  ) {
    this.componentType = formData.componentType;
    this.componentName = formData.componentName;
    this.commentIdList = formData.commentIdList;
  }

  ngOnInit(): void {
    console.log(this.commentIdList);
    this.loadCommentList();
  }

  clickedClose(): void {
    this.dialogRef.close();
  }

  loadCommentList(): void {
    if (this.commentIdList.length !== 0) {
      this.commentMapperService
        .getCommentsFromKeyResult$(this.parentKeyResult.id)
        .pipe(take(1))
        .subscribe(commentList => (this.commentList = commentList));
    }
  }

  canPostNewComment(): boolean {
    return !(this.newCommentText.length < 3 || this.newCommentText.length > 255 || this.isPostingComment);
  }

  postNewComment(): void {
    if (this.canPostNewComment()) {
      this.isPostingComment = true;
      const newComment: ViewComment = new ViewComment(1, '', this.newCommentText, new Date());

      this.commentMapperService
        .createComment$(this.parentKeyResult.id, newComment)
        .pipe(take(1))
        .subscribe(createdComment => {
          this.parentKeyResult.commentIdList.push(createdComment.id);
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
    indexOfComment = this.parentKeyResult.commentIdList.indexOf(deletedComment.id);
    this.parentKeyResult.commentIdList.splice(indexOfComment, 1);
  }
}
