import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { AProposComponent } from './a-propos.component';

export const A_PROPOS_ROUTE: Route = {
  path: 'a-propos',
  component: AProposComponent,
  data: {
    authorities: [],
    pageTitle: 'a-propos.title'
  },
  canActivate: [UserRouteAccessService]
};
