package org.burningokr.service.okr;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.repositories.okr.OkrTopicDescriptionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OkrTopicDescriptionServiceTest {

  @Mock private OkrTopicDescriptionRepository okrTopicDescriptionRepository;

  @InjectMocks private OkrTopicDescriptionService okrTopicDescriptionService;

  private OkrTopicDescription okrTopicDescription;

  @Before
  public void setUp() {
    okrTopicDescription = new OkrTopicDescription();
  }

  @Test
  public void updateOkrTopicDescription_updates() {
    List<UUID> startTeam = new ArrayList<>();
    startTeam.add(UUID.randomUUID());
    startTeam.add(UUID.randomUUID());
    startTeam.add(UUID.randomUUID());

    List<UUID> stakeholder = new ArrayList<>();
    stakeholder.add(UUID.randomUUID());
    stakeholder.add(UUID.randomUUID());
    stakeholder.add(UUID.randomUUID());

    okrTopicDescription.setId(10L);
    okrTopicDescription.setInitiatorId(UUID.randomUUID());
    okrTopicDescription.setAcceptanceCriteria("testCriteria");
    okrTopicDescription.setContributesTo("testContributesTo");
    okrTopicDescription.setDelimitation("testDelimitation");
    okrTopicDescription.setBeginning(LocalDate.of(2020, 3, 3));
    okrTopicDescription.setDependencies("testDependencies");
    okrTopicDescription.setResources("testResources");
    okrTopicDescription.setHandoverPlan("testHandoverPlan");
    okrTopicDescription.setStartTeam(startTeam);
    okrTopicDescription.setStakeholders(stakeholder);
    okrTopicDescription.setName("testName");

    when(okrTopicDescriptionRepository.findByIdOrThrow(ArgumentMatchers.any(Long.class)))
        .thenReturn(new OkrTopicDescription());
    when(okrTopicDescriptionRepository.save(ArgumentMatchers.any(OkrTopicDescription.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    OkrTopicDescription updatedDescription =
        okrTopicDescriptionService.updateOkrTopicDescription(okrTopicDescription);

    assertEquals(okrTopicDescription.getInitiatorId(), updatedDescription.getInitiatorId());
    assertEquals(okrTopicDescription.getName(), updatedDescription.getName());
    assertEquals(
        okrTopicDescription.getAcceptanceCriteria(), updatedDescription.getAcceptanceCriteria());
    assertEquals(okrTopicDescription.getBeginning(), updatedDescription.getBeginning());
    assertEquals(okrTopicDescription.getContributesTo(), updatedDescription.getContributesTo());
    assertEquals(okrTopicDescription.getDelimitation(), updatedDescription.getDelimitation());
    assertEquals(okrTopicDescription.getDependencies(), updatedDescription.getDependencies());
    assertEquals(okrTopicDescription.getHandoverPlan(), updatedDescription.getHandoverPlan());
    assertEquals(okrTopicDescription.getResources(), updatedDescription.getResources());
    assertEquals(
        (long) okrTopicDescription.getStakeholders().size(),
        updatedDescription.getStakeholders().size());
    assertEquals(
        (long) okrTopicDescription.getStartTeam().size(), updatedDescription.getStartTeam().size());
  }
}
