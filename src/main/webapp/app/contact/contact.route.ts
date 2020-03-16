import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ContactComponent } from './contact.component';

export const CONTACT_ROUTE: Route = {
  path: 'contact',
  component: ContactComponent,
  data: {
    authorities: [],
    pageTitle: 'contact.title'
  },
  canActivate: [UserRouteAccessService]
};
