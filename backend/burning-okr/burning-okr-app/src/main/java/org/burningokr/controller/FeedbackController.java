package org.burningokr.controller;

import org.burningokr.annotation.RestApiController;
import org.burningokr.annotation.TurnOff;
import org.burningokr.dto.FeedbackDto;
import org.burningokr.service.okr.feedback.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
public class FeedbackController {

  private FeedbackService feedbackService;

  @Autowired
  public FeedbackController(FeedbackService feedbackService) {
    this.feedbackService = feedbackService;
  }

  @PostMapping("/feedback")
  @TurnOff
  public ResponseEntity<FeedbackDto> postFeedback(
    @RequestBody FeedbackDto feedbackDto
  ) {
    feedbackService.sendFeedbackMail(feedbackDto.getName(), feedbackDto.getFeedbackText());
    return ResponseEntity.ok(feedbackDto);
  }
}
