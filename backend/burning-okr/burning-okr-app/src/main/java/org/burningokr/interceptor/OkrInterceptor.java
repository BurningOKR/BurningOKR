package org.burningokr.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class OkrInterceptor implements HandlerInterceptor {

  private final Logger logger = LoggerFactory.getLogger(OkrInterceptor.class);

  @Override
  public boolean preHandle(
    HttpServletRequest request, HttpServletResponse response, Object handler
  ) {
    HttpServletRequest requestWrapperObject = new ContentCachingRequestWrapper(request);

    logger.debug(
        "Incoming "
            + requestWrapperObject.getMethod()
            + " request on: "
            + requestWrapperObject.getRequestURL()
            + " with User-Agent: "
            + requestWrapperObject.getHeader("User-Agent"));

    return true;
  }

  @Override
  public void afterCompletion(
    HttpServletRequest request,
    HttpServletResponse response,
    Object handler,
    Exception exception
  ) {
    HttpServletResponse responseWrapperObject = new ContentCachingResponseWrapper(response);

    logger.debug("Sending Response.");
  }
}
