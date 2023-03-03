// TODO fix auth
//package org.burningokr.config;
//
//import lombok.RequiredArgsConstructor;
//import org.burningokr.interceptor.OkrInterceptor;
//import org.burningokr.service.userhandling.UserService;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.servlet.config.annotation.*;
//import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
//
//import java.util.List;
//
//@Configuration
//@EnableWebMvc
//@RequiredArgsConstructor
//public class ArgumentResolverConfig implements WebMvcConfigurer {
//
//  private final OkrInterceptor okrInterceptor;
//  private final LocaleChangeInterceptor localeChangeInterceptor;
//  private final UserService userService;
//
//  @Override
//  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//    argumentResolvers.add(new UserArgumentResolver(userService));
//  }
//
//  @Override
//  public void addViewControllers(ViewControllerRegistry registry) {
//    registry.addRedirectViewController("/v2/api-docs", "/v2/api-docs");
//    registry.addRedirectViewController(
//      "/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
//    registry.addRedirectViewController(
//      "/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
//    registry.addRedirectViewController("/swagger-resources", "/swagger-resources");
//  }
//
//  @Override
//  public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    registry
//      .addResourceHandler("/swagger-ui.html**")
//      .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
//    registry
//      .addResourceHandler("/webjars/**")
//      .addResourceLocations("classpath:/META-INF/resources/webjars/");
//  }
//
//  @Override
//  public void addInterceptors(InterceptorRegistry registry) {
//    registry.addInterceptor(okrInterceptor).addPathPatterns("/api/**/");
//    registry.addInterceptor(localeChangeInterceptor);
//  }
//}
