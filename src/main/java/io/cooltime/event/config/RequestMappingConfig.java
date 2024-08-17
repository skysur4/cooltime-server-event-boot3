package io.cooltime.event.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import io.cooltime.event.handler.InheritedRequestMappingHandler;

/**
 * BaseController 및 BaseV1Controller의 requestMapping 경로를 상속 받도록 설정
 */

@Configuration
public class RequestMappingConfig extends DelegatingWebFluxConfiguration {
  @Override
  protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
    return new InheritedRequestMappingHandler();
  }
}