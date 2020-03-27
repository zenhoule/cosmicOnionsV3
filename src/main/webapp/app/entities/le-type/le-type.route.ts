import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILeType, LeType } from 'app/shared/model/le-type.model';
import { LeTypeService } from './le-type.service';
import { LeTypeComponent } from './le-type.component';
import { LeTypeDetailComponent } from './le-type-detail.component';
import { LeTypeUpdateComponent } from './le-type-update.component';

@Injectable({ providedIn: 'root' })
export class LeTypeResolve implements Resolve<ILeType> {
  constructor(private service: LeTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((leType: HttpResponse<LeType>) => {
          if (leType.body) {
            return of(leType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeType());
  }
}

export const leTypeRoute: Routes = [
  {
    path: '',
    component: LeTypeComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.leType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LeTypeDetailComponent,
    resolve: {
      leType: LeTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.leType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LeTypeUpdateComponent,
    resolve: {
      leType: LeTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.leType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LeTypeUpdateComponent,
    resolve: {
      leType: LeTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.leType.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
