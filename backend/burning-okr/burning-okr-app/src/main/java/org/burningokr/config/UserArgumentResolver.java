package org.burningokr.config;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.burningokr.model.users.IUser;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return IUser.class.equals(parameter.getParameterType());
  }

  @Override
  public IUser resolveArgument(
    @NonNull MethodParameter parameter,
    ModelAndViewContainer mavContainer,
    @NonNull NativeWebRequest webRequest,
    WebDataBinderFactory binderFactory
  ) {
    // TODO fix auth (jklein 23.02.2023)
    throw new NotImplementedException("fix auth");
  }
}
