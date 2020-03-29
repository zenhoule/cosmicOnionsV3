import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CosmiconionsSharedModule } from '../shared/shared.module';

import { A_PROPOS_ROUTE, AProposComponent } from './';

@NgModule({
  imports: [CosmiconionsSharedModule, RouterModule.forRoot([A_PROPOS_ROUTE], { useHash: true })],
  declarations: [AProposComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CosmiconionsAppAProposModule {}
