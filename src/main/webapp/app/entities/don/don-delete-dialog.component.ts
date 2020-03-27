import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDon } from 'app/shared/model/don.model';
import { DonService } from './don.service';

@Component({
  templateUrl: './don-delete-dialog.component.html'
})
export class DonDeleteDialogComponent {
  don?: IDon;

  constructor(protected donService: DonService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.donService.delete(id).subscribe(() => {
      this.eventManager.broadcast('donListModification');
      this.activeModal.close();
    });
  }
}
