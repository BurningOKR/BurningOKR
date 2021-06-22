package org.burningokr.service.okr;

import com.google.common.collect.Lists;
import java.util.Collection;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.springframework.stereotype.Service;

@Service
public class OkrTopicDraftService {

  private OkrTopicDraftRepository okrTopicDraftRepository;

  public OkrTopicDraftService(OkrTopicDraftRepository okrTopicDraftRepository) {
    this.okrTopicDraftRepository = okrTopicDraftRepository;
  }

  public Collection<OkrTopicDraft> getAllTopicDrafts() {
    return Lists.newArrayList(okrTopicDraftRepository.findAll());
  }
}
