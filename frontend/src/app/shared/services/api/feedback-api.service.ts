// istanbul ignore file
import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FeedbackApiService {

  constructor(private api: ApiHttpService) {
  }

  postFeedback$(feedbackText: string, name: string): Observable<object> {
    const feedback: { feedbackText: string; name: string } = {
      feedbackText,
      name
    };

    return this.api.postData$(`feedback`, feedback);
  }
}
