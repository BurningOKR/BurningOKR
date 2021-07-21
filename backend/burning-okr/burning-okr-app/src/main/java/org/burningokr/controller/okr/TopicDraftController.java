package org.burningokr.controller.okr;

import java.util.Collection;
import javax.validation.Valid;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.OkrTopicDraftDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.users.User;
import org.burningokr.service.okr.OkrTopicDraftService;
import org.burningokr.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
public class TopicDraftController {
  private OkrTopicDraftService okrTopicDraftService;
  private DataMapper<OkrTopicDraft, OkrTopicDraftDto> okrTopicDraftMapper;
  private AuthorizationService authorizationService;

  /**
   * Initialize TopicDraftController
   *
   * @param okrTopicDraftService a {@link OkrTopicDraftService} object
   * @param okrTopicDraftMapper {@link DataMapper} object with {@link OkrTopicDraft} and {@link
   *     OkrTopicDraftDto}
   */
  @Autowired
  public TopicDraftController(
      OkrTopicDraftService okrTopicDraftService,
      DataMapper<OkrTopicDraft, OkrTopicDraftDto> okrTopicDraftMapper,
      AuthorizationService authorizationService) {
    this.okrTopicDraftService = okrTopicDraftService;
    this.okrTopicDraftMapper = okrTopicDraftMapper;
    this.authorizationService = authorizationService;
  }

  /**
   * API Endpoint to get all TopicDrafts.
   *
   * @return a {@link ResponseEntity} ok with a {@link Collection} of TopicDrafts
   */
  @GetMapping("/topicDrafts/all")
  public ResponseEntity<Collection<OkrTopicDraftDto>> getAllCompanies() {
    Collection<OkrTopicDraft> topicDrafts = okrTopicDraftService.getAllTopicDrafts();
    return ResponseEntity.ok(okrTopicDraftMapper.mapEntitiesToDtos(topicDrafts));
  }

//  @GetMapping("/topicDrafts/{topicDraftId}/notes")
//  public ResponseEntity<Collection<NoteTopic>>

  /**
   * API Endpoint to update/edit a Topic Draft.
   *
   * @param topicDraftId a long value
   * @param okrTopicDraftDto a {@link OkrTopicDraftDto} object
   * @return a {@link ResponseEntity} ok with a Topic Draft
   */
  @PutMapping("/topicDrafts/{topicDraftId}")
  @PreAuthorize(
      "@authorizationService.isAdmin() "
          + "|| @authorizationService.isTopicDraftInitiator(#topicDraftId)")
  public ResponseEntity updateTopicResultById(
      @PathVariable long topicDraftId, @Valid @RequestBody OkrTopicDraftDto okrTopicDraftDto) {
    OkrTopicDraft okrTopicDraft = okrTopicDraftMapper.mapDtoToEntity(okrTopicDraftDto);
    this.okrTopicDraftService.updateOkrTopicDraft(topicDraftId, okrTopicDraft);
    return ResponseEntity.ok().build();
  }

  /**
   * API Endpoint to update the status of a Topic Draft.
   *
   * @param topicDraftId a long value
   * @param okrTopicDraftDto a {@link OkrTopicDraftDto} object
   * @return a {@link ResponseEntity} ok with a Topic Draft
   */
  // TODO JZ (07.07.2021) authorization is not completed (also auditor should be allowed to
  // approve/reject)
  @PutMapping("/topicDrafts/status/{topicDraftId}")
  @PreAuthorize("@authorizationService.isAdmin()" + "|| @authorizationService.isAuditor()")
  public ResponseEntity updateTopicResultStatusById(
      @PathVariable long topicDraftId, @Valid @RequestBody OkrTopicDraftDto okrTopicDraftDto) {
    OkrTopicDraft okrTopicDraft = okrTopicDraftMapper.mapDtoToEntity(okrTopicDraftDto);
    this.okrTopicDraftService.updateOkrTopicDraftStatus(topicDraftId, okrTopicDraft);
    return ResponseEntity.ok().build();
  }

  /**
   * API Endpoint to delete a Topic Draft.
   *
   * @param topicDraftId a long value
   * @param user a {@link User} object
   * @return a {@link ResponseEntity} ok with a Topic Draft
   */
  @DeleteMapping("/topicDraft/{topicDraftId}")
  @PreAuthorize(
      "@authorizationService.isAdmin() "
          + "|| @authorizationService.isTopicDraftInitiator(#topicDraftId)")
  public ResponseEntity deleteTopicDraftById(@PathVariable Long topicDraftId, User user) {
    okrTopicDraftService.deleteTopicDraftById(topicDraftId, user);
    return ResponseEntity.ok().build();
  }
}
