import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGoodies } from 'app/shared/model/goodies.model';
import { GoodiesService } from './goodies.service';

@Component({
  templateUrl: './goodies-delete-dialog.component.html'
})
export class GoodiesDeleteDialogComponent {
  goodies?: IGoodies;

  constructor(protected goodiesService: GoodiesService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.goodiesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('goodiesListModification');
      this.activeModal.close();
    });
  }
}
