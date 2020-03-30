import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'projet',
        loadChildren: () => import('./projet/projet.module').then(m => m.CosmiconionsProjetModule)
      },
      {
        path: 'message',
        loadChildren: () => import('./message/message.module').then(m => m.CosmiconionsMessageModule)
      },
      {
        path: 'don',
        loadChildren: () => import('./don/don.module').then(m => m.CosmiconionsDonModule)
      },
      {
        path: 'goodies',
        loadChildren: () => import('./goodies/goodies.module').then(m => m.CosmiconionsGoodiesModule)
      },
      {
        path: 'categorie',
        loadChildren: () => import('./categorie/categorie.module').then(m => m.CosmiconionsCategorieModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class CosmiconionsEntityModule {}
