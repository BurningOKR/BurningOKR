package org.burningokr.repositories.activity;

import org.burningokr.model.activity.Activity;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends ExtendedRepository<Activity, Long> {}
