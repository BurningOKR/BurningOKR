package org.burningokr.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class OkrInterceptor extends HandlerInterceptorAdapter {

  private final Logger logger = LoggerFactory.getLogger(OkrInterceptor.class);

  @Override
  public boolean preHandle(
    HttpServletRequest request, HttpServletResponse response, Object handler
  ) {
    HttpServletRequest requestWrapperObject = new ContentCachingRequestWrapper(request);

    logger.info(
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

    logger.info("Sending Response.");
  }
}
