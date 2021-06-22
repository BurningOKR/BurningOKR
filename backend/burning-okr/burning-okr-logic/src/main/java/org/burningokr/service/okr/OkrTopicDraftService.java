package org.burningokr.service.okr;

import com.google.common.collect.Lists;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.service.activity.ActivityService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OkrTopicDraftService {

    private OkrTopicDraftRepository okrTopicDraftRepository;
    private ActivityService activityService;

    public OkrTopicDraftService(OkrTopicDraftRepository okrTopicDraftRepository,
                                ActivityService activityService) {
        this.okrTopicDraftRepository = okrTopicDraftRepository;
        this.activityService = activityService;
    }


    public Collection<OkrTopicDraft> getAllTopicDrafts() {
        return Lists.newArrayList(okrTopicDraftRepository.findAll());
    }

    public void deleteTopicDraftById(Long topicDraftId, User user) {
        OkrTopicDraft referencedTopicDraft = okrTopicDraftRepository.findByIdOrThrow(topicDraftId);
        okrTopicDraftRepository.deleteById(topicDraftId);
        activityService.createActivity(user, referencedTopicDraft, Action.DELETED);
    }
}
