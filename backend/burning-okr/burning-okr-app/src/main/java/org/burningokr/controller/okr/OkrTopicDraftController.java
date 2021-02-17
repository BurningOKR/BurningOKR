package org.burningokr.controller.okr;

import org.burningokr.annotation.RestApiController;
import org.burningokr.model.okr.OkrTopicDraft;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@RestApiController
public class OkrTopicDraftController {
  @PostMapping("TopicDraft")
  public ResponseEntity<OkrTopicDraft> createOkrTopicDraft(OkrTopicDraft topicDraft){
    return ResponseEntity.ok(topicDraft);
  }
}
