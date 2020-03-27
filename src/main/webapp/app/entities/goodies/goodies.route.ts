import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IGoodies, Goodies } from 'app/shared/model/goodies.model';
import { GoodiesService } from './goodies.service';
import { GoodiesComponent } from './goodies.component';
import { GoodiesDetailComponent } from './goodies-detail.component';
import { GoodiesUpdateComponent } from './goodies-update.component';

@Injectable({ providedIn: 'root' })
export class GoodiesResolve implements Resolve<IGoodies> {
  constructor(private service: GoodiesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGoodies> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((goodies: HttpResponse<Goodies>) => {
          if (goodies.body) {
            return of(goodies.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Goodies());
  }
}

export const goodiesRoute: Routes = [
  {
    path: '',
    component: GoodiesComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.goodies.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: GoodiesDetailComponent,
    resolve: {
      goodies: GoodiesResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.goodies.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: GoodiesUpdateComponent,
    resolve: {
      goodies: GoodiesResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.goodies.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: GoodiesUpdateComponent,
    resolve: {
      goodies: GoodiesResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.goodies.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
