import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDon, Don } from 'app/shared/model/don.model';
import { DonService } from './don.service';
import { DonComponent } from './don.component';
import { DonDetailComponent } from './don-detail.component';
import { DonUpdateComponent } from './don-update.component';

@Injectable({ providedIn: 'root' })
export class DonResolve implements Resolve<IDon> {
  constructor(private service: DonService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDon> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((don: HttpResponse<Don>) => {
          if (don.body) {
            return of(don.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Don());
  }
}

export const donRoute: Routes = [
  {
    path: '',
    component: DonComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.don.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DonDetailComponent,
    resolve: {
      don: DonResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.don.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DonUpdateComponent,
    resolve: {
      don: DonResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.don.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DonUpdateComponent,
    resolve: {
      don: DonResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.don.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
