import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CosmiconionsSharedModule } from '../shared/shared.module';

import { CONTACT_ROUTE, ContactComponent } from './';

@NgModule({
  imports: [CosmiconionsSharedModule, RouterModule.forRoot([CONTACT_ROUTE], { useHash: true })],
  declarations: [ContactComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CosmiconionsAppContactModule {}
