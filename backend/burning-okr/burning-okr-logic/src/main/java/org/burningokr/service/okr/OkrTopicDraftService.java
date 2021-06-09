package org.burningokr.service.okr;

import com.google.common.collect.Lists;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
public class OkrTopicDraftService {

    private OkrTopicDraftRepository okrTopicDraftRepository;

    public OkrTopicDraftService(OkrTopicDraftRepository okrTopicDraftRepository){
        this.okrTopicDraftRepository = okrTopicDraftRepository;
    }


    public Collection<OkrTopicDraft> getAllTopicDrafts() {
        return Lists.newArrayList(okrTopicDraftRepository.findAll());
    }
}
