import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILeType } from 'app/shared/model/le-type.model';
import { LeTypeService } from './le-type.service';

@Component({
  templateUrl: './le-type-delete-dialog.component.html'
})
export class LeTypeDeleteDialogComponent {
  leType?: ILeType;

  constructor(protected leTypeService: LeTypeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('leTypeListModification');
      this.activeModal.close();
    });
  }
}
