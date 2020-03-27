import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CosmiconionsSharedModule } from 'app/shared/shared.module';
import { LeTypeComponent } from './le-type.component';
import { LeTypeDetailComponent } from './le-type-detail.component';
import { LeTypeUpdateComponent } from './le-type-update.component';
import { LeTypeDeleteDialogComponent } from './le-type-delete-dialog.component';
import { leTypeRoute } from './le-type.route';

@NgModule({
  imports: [CosmiconionsSharedModule, RouterModule.forChild(leTypeRoute)],
  declarations: [LeTypeComponent, LeTypeDetailComponent, LeTypeUpdateComponent, LeTypeDeleteDialogComponent],
  entryComponents: [LeTypeDeleteDialogComponent]
})
export class CosmiconionsLeTypeModule {}
