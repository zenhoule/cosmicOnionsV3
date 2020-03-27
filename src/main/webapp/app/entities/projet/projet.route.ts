import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProjet, Projet } from 'app/shared/model/projet.model';
import { ProjetService } from './projet.service';
import { ProjetComponent } from './projet.component';
import { ProjetDetailComponent } from './projet-detail.component';
import { ProjetUpdateComponent } from './projet-update.component';

@Injectable({ providedIn: 'root' })
export class ProjetResolve implements Resolve<IProjet> {
  constructor(private service: ProjetService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProjet> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((projet: HttpResponse<Projet>) => {
          if (projet.body) {
            return of(projet.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Projet());
  }
}

export const projetRoute: Routes = [
  {
    path: '',
    component: ProjetComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.projet.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProjetDetailComponent,
    resolve: {
      projet: ProjetResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.projet.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProjetUpdateComponent,
    resolve: {
      projet: ProjetResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.projet.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProjetUpdateComponent,
    resolve: {
      projet: ProjetResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'cosmiconionsApp.projet.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
