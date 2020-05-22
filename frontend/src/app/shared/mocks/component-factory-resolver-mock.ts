import { ComponentFactory, ComponentRef, Injector, NgModuleRef, Type } from '@angular/core';

export class ComponentFactoryResolverMock {
  resolveComponentFactory<T>(componentToBeResolved: Type<T>): ComponentFactory<T> {
    return {
      componentType: undefined,
      inputs: [],
      ngContentSelectors: [],
      outputs: [],
      selector: '',
      create(
        injector: Injector,
        projectableNodes?: any[][],
        rootSelectorOrNode?: string | any,
        ngModule?: NgModuleRef<any>
      ): ComponentRef<T> {
        return undefined;
      }
    };
  }
}
