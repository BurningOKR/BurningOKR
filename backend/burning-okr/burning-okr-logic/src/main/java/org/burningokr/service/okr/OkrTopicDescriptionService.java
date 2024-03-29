package org.burningokr.service.okr;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.repositories.okr.OkrTopicDescriptionRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.burningokr.service.activity.ActivityService;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostCommitDeleteEventListener;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OkrTopicDescriptionService implements PostCommitDeleteEventListener {

  private final OkrTopicDescriptionRepository okrTopicDescriptionRepository;
  private final OkrDepartmentRepository okrDepartmentRepository;
  private final ActivityService activityService;
  private final EntityManagerFactory entityManagerFactory;

  @PostConstruct
  private void init() {
    SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
    EventListenerRegistry registry =
      sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
    registry.getEventListenerGroup(EventType.POST_COMMIT_DELETE).appendListener(this);
  }

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
    OkrTopicDescription okrTopicDescription
  ) {
    OkrTopicDescription description = findById(okrTopicDescription.getId());

    description.setName(okrTopicDescription.getName());
    description.setStakeholders(okrTopicDescription.getStakeholders());
    description.setStartTeam(okrTopicDescription.getStartTeam());
    description.setHandoverPlan(okrTopicDescription.getHandoverPlan());
    description.setResources(okrTopicDescription.getResources());
    description.setDependencies(okrTopicDescription.getDependencies());
    description.setBeginning(okrTopicDescription.getBeginning());
    description.setDelimitation(okrTopicDescription.getDelimitation());
    description.setContributesTo(okrTopicDescription.getContributesTo());
    description.setDescription(okrTopicDescription.getDescription());
    description.setInitiatorId(okrTopicDescription.getInitiatorId());

    description = okrTopicDescriptionRepository.save(description);

    log.debug("Updated OkrTopicDescription %s (id: %d).".formatted(description.getName(), description.getId()));
    activityService.createActivity(description, Action.EDITED);

    return description;
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

  /**
   * Safely deletes an OkrTopicDescription. The OkrTopicDescription will not be deleted, when it is
   * still referenced by (multiple) departments.
   *
   * @param okrTopicDescriptionId the id of the okrTopicDescription to delete.
   */
  @Transactional
  public void safeDeleteOkrTopicDescription(Long okrTopicDescriptionId) {
    long count = getOkrDepartmentsWithTopicDescription(okrTopicDescriptionId).size();

    if (count == 0) {
      deleteOkrTopicDescription(okrTopicDescriptionId);
    } else {
      log.debug(
        String.format(
          "OkrTopicDescription with id %d was not deleted because it is referenced by %d departments.",
          okrTopicDescriptionId,
          count));
    }
  }

  /**
   * This method is called by hibernate, when an object is being deleted. If the deleted Object is
   * an OkrDepartment, then this method will call safeDeleteOkrTopicDescription() to delete the
   * OkrTopicDescription of the Department, if neccessary.
   *
   * @param event a {@link PostDeleteEvent}
   */
  @Override
  public void onPostDelete(PostDeleteEvent event) {
    if (event.getEntity() instanceof OkrDepartment department) {
      safeDeleteOkrTopicDescription(department.getOkrTopicDescription().getId());
    }
  }

  // TODO check if TopicDrafts are deleted after a cascading deletion, deprecated method try to remove

  /**
   * This method must return true, otherwise the onPostDelete method will not be called.
   *
   * @param entityPersister an {@link EntityPersister}
   * @return true
   */
  @Override
  public boolean requiresPostCommitHandling(EntityPersister entityPersister) {
    return true;
  }

  private void deleteOkrTopicDescription(Long okrTopicDescriptionId) {

    // (R.J. 15.01.2020)
    // For some reason, we cannot use the okrTopicDescriptionRepository
    // to delete the OkrTopicDescription here.
    //
    // When this method is called by the onPostDelete Method, the okrTopicDescriptionRepository
    // does not delete the OkrTopicDescription when the onPostDelete Method was called due to a
    // cascading deletion.
    // For Example when a department gets deleted, because the company was deleted.

    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      OkrTopicDescription existing =
        entityManager.find(OkrTopicDescription.class, okrTopicDescriptionId);
      entityManager.remove(existing);
      entityManager.flush();
      transaction.commit();
      log.debug("Deleted OkrTopicDescription with id %d.".formatted(existing.getId()));
      activityService.createActivity(existing, Action.DELETED);
    } catch (Exception e) {
      log.error("Unable to delete OkrTopicDescription with id %d.".formatted(okrTopicDescriptionId));
    } finally {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      entityManager.close();
    }
  }

  @Override
  public void onPostDeleteCommitFailed(PostDeleteEvent event) {
    // TODO (C.K. 07.08.2023): create error handling
  }
}
