import { Injectable } from '@angular/core';
import { FeedbackApiService } from '../../shared/services/api/feedback-api.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FeedbackMapper {

  constructor(private feedbackApiService: FeedbackApiService) {
  }

  postFeedback$(feedbackText: string, name: string): Observable<object> {
    return this.feedbackApiService.postFeedback$(feedbackText, name);
  }
}
