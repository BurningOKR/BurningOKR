package org.burningokr.config;

import org.burningokr.model.users.User;
import org.burningokr.service.userhandling.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.naming.ConfigurationException;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {

  private final UserService userService;

  @Autowired
  public UserArgumentResolver(UserService userService) {
    this.userService = userService;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return User.class.equals(parameter.getParameterType());
  }

  @Override
  public User resolveArgument(
      @NonNull MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      @NonNull NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws ConfigurationException {
    return userService.getCurrentUser();
  }
}
