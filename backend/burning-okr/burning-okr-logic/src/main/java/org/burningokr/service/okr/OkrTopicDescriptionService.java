package org.burningokr.service.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.repositories.okr.OkrTopicDescriptionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OkrTopicDescriptionService {

  private final OkrTopicDescriptionRepository okrTopicDescriptionRepository;

  /**
   * finds am OkrTopicDescription by its id
   *
   * @param id the id of the OkrTopicDescription
   * @return an {@link OkrTopicDescription} object
   */
  public OkrTopicDescription findById(long id) {
    return okrTopicDescriptionRepository.findByIdOrThrow(id);
  }

  /**
   * Updates an OkrTopicDescription
   *
   * @param okrTopicDescription the {@link OkrTopicDescription} to update
   * @return the updated OkrTopicDescription
   */
  public OkrTopicDescription updateOkrTopicDescription(OkrTopicDescription okrTopicDescription) {
    OkrTopicDescription existing = findById(okrTopicDescription.getId());

    existing.setName(okrTopicDescription.getName());
    existing.setStakeholders(okrTopicDescription.getStakeholders());
    existing.setStartTeam(okrTopicDescription.getStartTeam());
    existing.setHandoverPlan(okrTopicDescription.getHandoverPlan());
    existing.setResources(okrTopicDescription.getResources());
    existing.setDependencies(okrTopicDescription.getDependencies());
    existing.setBeginning(okrTopicDescription.getBeginning());
    existing.setDelimitation(okrTopicDescription.getDelimitation());
    existing.setContributesTo(okrTopicDescription.getContributesTo());
    existing.setAcceptanceCriteria(okrTopicDescription.getAcceptanceCriteria());
    existing.setInitiatorId(okrTopicDescription.getInitiatorId());

    existing = okrTopicDescriptionRepository.save(existing);

    return existing;
  }
}
