package org.burningokr.service.okrUnit.departmentservices;

import org.burningokr.model.activity.Action;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.burningokr.service.okrUnitUtil.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("okrUnitServiceManagers")
public class OkrUnitServiceManagers<T extends OkrChildUnit> extends OkrUnitServiceMembers<T> {
  @Autowired
  OkrUnitServiceManagers(
      ParentService parentService,
      UnitRepository<T> unitRepository,
      ObjectiveRepository objectiveRepository,
      ActivityService activityService,
      EntityCrawlerService entityCrawlerService) {
    super(
        parentService, unitRepository, objectiveRepository, activityService, entityCrawlerService);
  }

  @Override
  @Transactional
  public T updateUnit(T updatedUnit, User user) {
    T referencedUnit = unitRepository.findByIdOrThrow(updatedUnit.getId());

    throwIfCycleForDepartmentIsClosed(referencedUnit);

    if (updatedUnit instanceof OkrDepartment) {
      ((OkrDepartment) referencedUnit)
          .setOkrMemberIds(((OkrDepartment) updatedUnit).getOkrMemberIds());

      referencedUnit = unitRepository.save(referencedUnit);
      logger.info(
          "Updated OkrDepartment "
              + referencedUnit.getName()
              + "(id:"
              + referencedUnit.getId()
              + ")");
      activityService.createActivity(user, referencedUnit, Action.EDITED);
    }

    return referencedUnit;
  }
}
