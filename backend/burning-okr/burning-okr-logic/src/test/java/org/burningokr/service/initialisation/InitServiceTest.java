package org.burningokr.service.initialisation;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.model.configuration.OAuthClientDetails;
import org.burningokr.model.initialisation.InitState;
import org.burningokr.model.initialisation.InitStateName;
import org.burningokr.model.users.LocalUser;
import org.burningokr.repositories.initialisation.InitStateRepository;
import org.burningokr.service.configuration.OAuthClientDetailsService;
import org.burningokr.service.configuration.OAuthConfigurationService;
import org.burningokr.service.exceptions.InvalidInitStateException;
import org.burningokr.service.userhandling.AdminUserService;
import org.burningokr.service.userhandling.LocalUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InitServiceTest {

  @Mock
  private InitStateRepository initStateRepository;
  @Mock
  private LocalUserService localUserService;
  @Mock
  private AdminUserService adminUserService;
  @Mock
  private InitOrderService initOrderService;
  @Mock
  private OAuthClientDetailsService oauthClientDetailsService;
  @Mock
  private OAuthConfigurationService oauthConfigurationService;
  @Mock
  private ApplicationContext applicationContext;

  @InjectMocks
  private InitService initService;

  private Iterable<InitState> iterable;
  private InitState initState;
  private LocalUser localUser;

  @BeforeEach
  public void init() {
    initState = new InitState();
    localUser = new LocalUser();

    when(initStateRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);
    when(initOrderService.getNextInitState(any())).thenReturn(InitStateName.INITIALIZED);
  }

  @Test
  public void getInitState_expectedInitState() {
    initState.setInitState(InitStateName.CREATE_USER);
    initState.setId(5L);
    iterable = Collections.singletonList(initState);

    when(initStateRepository.findAll()).thenReturn(iterable);

    InitState initStateFromService = initService.getInitState();

    verify(initStateRepository).findAll();

    assertEquals(initState.getInitState(), initStateFromService.getInitState());
    assertEquals(initState.getId(), initStateFromService.getId());
  }

  @Test
  public void getInitState_expectException() {
    iterable = Collections.emptyList();

    when(initStateRepository.findAll()).thenReturn(iterable);

    try {
      initService.getInitState();
      fail(); // Should throw exception before getting here
    } catch (Exception e) {
      assertTrue(e instanceof EntityNotFoundException);
    }
  }

  @Test
  public void getInitState_returnsOnlyFirstEntry() {
    InitState initState = new InitState();
    initState.setInitState(InitStateName.CREATE_USER);
    initState.setId(5L);

    InitState initState2 = new InitState();
    initState2.setInitState(InitStateName.INITIALIZED);
    initState2.setId(1L);

    iterable = Arrays.asList(initState, initState2);

    when(initStateRepository.findAll()).thenReturn(iterable);

    InitState initStateFromService = initService.getInitState();

    assertEquals(initState.getId(), initStateFromService.getId());
    assertEquals(initState.getInitState(), initStateFromService.getInitState());
  }

  @Test
  public void getInitState_setsRuntimeId() {
    initState.setInitState(InitStateName.CREATE_USER);
    initState.setId(5L);
    iterable = Collections.singletonList(initState);

    when(initStateRepository.findAll()).thenReturn(iterable);
    when(applicationContext.getId()).thenReturn("runtimeId");

    InitState initStateFromService = initService.getInitState();

    verify(initStateRepository).findAll();

    assertEquals(initState.getInitState(), initStateFromService.getInitState());
    assertEquals("runtimeId", initStateFromService.getRuntimeId());
    assertEquals(initState.getId(), initStateFromService.getId());
  }

  @Test
  public void setAdminUser_setsCorrectInitState() {
    initState.setInitState(InitStateName.CREATE_USER);
    initState.setId(1L);
    iterable = Collections.singletonList(initState);
    LocalUser localUser = new LocalUser();

    when(initStateRepository.findAll()).thenReturn(iterable);
    when(localUserService.createLocalUser(localUser, false)).thenReturn(new LocalUser());

    try {
      InitState initStateFromService = initService.setAdminUser(localUser, "TestAbc");

      assertEquals(initStateFromService.getInitState(), InitStateName.INITIALIZED);
    } catch (Exception e) {
      fail(e.getClass().getName());
    }
  }

  @Test
  public void setAdminUser_throwsExceptionWhenInWrongInitState() {
    initState.setInitState(InitStateName.INITIALIZED);
    initState.setId(1L);
    iterable = Collections.singletonList(initState);

    when(initStateRepository.findAll()).thenReturn(iterable);

    try {
      initService.setAdminUser(new LocalUser(), "TestAbc");
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof InvalidInitStateException);
    }
  }

  @Test
  public void setAdminUser_createsUser() {
    initState.setInitState(InitStateName.CREATE_USER);
    initState.setId(1L);
    iterable = Collections.singletonList(initState);
    LocalUser localUser = new LocalUser();

    when(initStateRepository.findAll()).thenReturn(iterable);
    when(localUserService.createLocalUser(localUser, false)).thenReturn(new LocalUser());

    try {
      initService.setAdminUser(localUser, "TestAbc");
      verify(localUserService).createLocalUser(localUser, false);
    } catch (Exception e) {
      fail(e.getClass().getName());
    }
  }

  @Test
  public void setOAuthClientDetails_setsCorrectInitState() {
    initState.setInitState(InitStateName.SET_OAUTH_CLIENT_DETAILS);
    initState.setId(1L);
    iterable = Collections.singletonList(initState);

    when(initStateRepository.findAll()).thenReturn(iterable);

    try {
      InitState initStateFromService = initService.setOAuthClientDetails(new OAuthClientDetails());

      assertEquals(InitStateName.INITIALIZED, initStateFromService.getInitState());
    } catch (Exception e) {
      fail(e.getClass().getName());
    }
  }

  @Test
  public void setOAuthClientDetails_throwsExceptionWhenInWrongInitState() {
    initState.setInitState(InitStateName.INITIALIZED);
    initState.setId(1L);
    iterable = Collections.singletonList(initState);

    when(initStateRepository.findAll()).thenReturn(iterable);

    try {
      initService.setOAuthClientDetails(new OAuthClientDetails());
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof InvalidInitStateException);
    }
  }

  @Test
  public void setOAuthClientDetails_setsClientDetails() {
    initState.setInitState(InitStateName.SET_OAUTH_CLIENT_DETAILS);
    initState.setId(1L);
    iterable = Collections.singletonList(initState);

    when(initStateRepository.findAll()).thenReturn(iterable);

    try {
      OAuthClientDetails oauthClientDetails = new OAuthClientDetails();

      initService.setOAuthClientDetails(oauthClientDetails);
      verify(oauthClientDetailsService).encodeClientSecret(oauthClientDetails);
      verify(oauthClientDetailsService).fillDefaultValues(oauthClientDetails);
      verify(oauthClientDetailsService).updateOAuthClientDetails(oauthClientDetails);
      verify(oauthConfigurationService).updateOAuthConfiguration(any());
    } catch (Exception e) {
      fail(e.getClass().getName());
    }
  }
}
