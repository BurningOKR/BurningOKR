package org.burningokr.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
@Component
public class OkrInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull Object handler
  ) {
    HttpServletRequest requestWrapperObject = new ContentCachingRequestWrapper(request);

    log.debug(
      String.format(
        "Incoming %s request on: %s with User-Agent: %s",
        requestWrapperObject.getMethod(),
        requestWrapperObject.getRequestURL(),
        requestWrapperObject.getHeader("User-Agent")
      ));

    return true;
  }

  @Override
  public void afterCompletion(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull Object handler,
    Exception exception
  ) {
    log.debug("Sending Response.");
  }
}
