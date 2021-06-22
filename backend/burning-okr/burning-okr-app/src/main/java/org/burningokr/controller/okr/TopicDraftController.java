package org.burningokr.controller.okr;

import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.OkrTopicDraftDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.users.User;
import org.burningokr.service.okr.OkrTopicDraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;

@RestApiController
public class TopicDraftController {
    private OkrTopicDraftService okrTopicDraftService;
    private DataMapper<OkrTopicDraft, OkrTopicDraftDto> okrTopicDraftMapper;

    /**
     * Initialize TopicDraftController
     *
     * @param okrTopicDraftService a {@Link OkrTopicDraftService} object
     * @param okrTopicDraftMapper {@link DataMapper} object with {@link OkrTopicDraft} and {@link
     *      OkrTopicDraftDto}
     */
    @Autowired
    public TopicDraftController(
            OkrTopicDraftService okrTopicDraftService,
            DataMapper<OkrTopicDraft, OkrTopicDraftDto> okrTopicDraftMapper){
        this.okrTopicDraftService = okrTopicDraftService;
        this.okrTopicDraftMapper = okrTopicDraftMapper;
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

    @DeleteMapping("/topicDraft/{topicDraftId}")
    public ResponseEntity deleteTopicDraftById(@PathVariable Long topicDraftId, User user){
        okrTopicDraftService.deleteTopicDraftById(topicDraftId, user);
        return ResponseEntity.ok().build();
    }
}
