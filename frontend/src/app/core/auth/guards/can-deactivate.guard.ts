import { Injectable } from '@angular/core';
import { CanDeactivate } from '@angular/router';

export interface ComponentCanDeactivate {
  canDeactivate: () => boolean;
}

@Injectable({
  providedIn: 'root',
})
export class CanDeactivateGuard implements CanDeactivate<ComponentCanDeactivate> {
  canDeactivate(
    component: ComponentCanDeactivate,
  ): boolean {
    return component.canDeactivate();
  }

}
