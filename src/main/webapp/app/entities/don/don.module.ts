import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CosmiconionsSharedModule } from 'app/shared/shared.module';
import { DonComponent } from './don.component';
import { DonDetailComponent } from './don-detail.component';
import { DonUpdateComponent } from './don-update.component';
import { DonDeleteDialogComponent } from './don-delete-dialog.component';
import { donRoute } from './don.route';

@NgModule({
  imports: [CosmiconionsSharedModule, RouterModule.forChild(donRoute)],
  declarations: [DonComponent, DonDetailComponent, DonUpdateComponent, DonDeleteDialogComponent],
  entryComponents: [DonDeleteDialogComponent]
})
export class CosmiconionsDonModule {}
