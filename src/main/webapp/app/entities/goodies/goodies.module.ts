import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CosmiconionsSharedModule } from 'app/shared/shared.module';
import { GoodiesComponent } from './goodies.component';
import { GoodiesDetailComponent } from './goodies-detail.component';
import { GoodiesUpdateComponent } from './goodies-update.component';
import { GoodiesDeleteDialogComponent } from './goodies-delete-dialog.component';
import { goodiesRoute } from './goodies.route';

@NgModule({
  imports: [CosmiconionsSharedModule, RouterModule.forChild(goodiesRoute)],
  declarations: [GoodiesComponent, GoodiesDetailComponent, GoodiesUpdateComponent, GoodiesDeleteDialogComponent],
  entryComponents: [GoodiesDeleteDialogComponent]
})
export class CosmiconionsGoodiesModule {}
