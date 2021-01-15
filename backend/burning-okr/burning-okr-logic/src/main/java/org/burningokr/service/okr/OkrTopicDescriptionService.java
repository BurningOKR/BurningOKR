package org.burningokr.service.okr;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.OkrTopicDescriptionRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.burningokr.service.activity.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OkrTopicDescriptionService {

  private final OkrTopicDescriptionRepository okrTopicDescriptionRepository;
  private final OkrDepartmentRepository okrDepartmentRepository;
  private final ActivityService activityService;
  private final Logger logger = LoggerFactory.getLogger(OkrTopicDescriptionService.class);

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
  public OkrTopicDescription updateOkrTopicDescription(
      OkrTopicDescription okrTopicDescription, User user) {
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

    logger.info(
        "Updated OkrTopicDescription " + existing.getName() + "(id:" + existing.getId() + ")");
    activityService.createActivity(user, existing, Action.EDITED);

    return existing;
  }

  /**
   * Safely deletes an OkrTopicDescription. The OkrTopicDescription will not be deleted, when it is
   * still referenced by (multiple) departments.
   *
   * @param okrTopicDescriptionId the id of the okrTopicDescription to delete.
   */
  public void safeDeleteOkrTopicDescription(Long okrTopicDescriptionId, User user) {
    long count = (long) getOkrDepartmentsWithTopicDescription(okrTopicDescriptionId).size();

    if (count == 0) {
      deleteOkrTopicDescription(okrTopicDescriptionId, user);
    } else {
      logger.info(
          "OkrTopicDescription with Id "
              + okrTopicDescriptionId
              + " was not deleted, because it is referenced by"
              + count
              + " departments.");
    }
  }

  /**
   * gets all departments that reference an okrTopicDescription
   *
   * @param okrTopicDescriptionId the referenced okrTopicDescription id
   * @return a {@link List} of {@link OkrDepartment}
   */
  public List<OkrDepartment> getOkrDepartmentsWithTopicDescription(Long okrTopicDescriptionId) {
    ArrayList<OkrDepartment> okrDepartments = new ArrayList<>();

    okrDepartmentRepository
        .findAll()
        .forEach(
            department -> {
              if (department.getOkrTopicDescription().getId().equals(okrTopicDescriptionId)) {
                okrDepartments.add(department);
              }
            });

    return okrDepartments;
  }

  private void deleteOkrTopicDescription(Long okrTopicDescriptionId, User user) {
    OkrTopicDescription existing = findById(okrTopicDescriptionId);
    okrTopicDescriptionRepository.delete(existing);

    logger.info("Deleted OkrTopicDescription with Id " + existing.getId());
    activityService.createActivity(user, existing, Action.DELETED);
  }
}
