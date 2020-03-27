import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDon } from 'app/shared/model/don.model';
import { DonService } from './don.service';
import { DonDeleteDialogComponent } from './don-delete-dialog.component';

@Component({
  selector: 'jhi-don',
  templateUrl: './don.component.html'
})
export class DonComponent implements OnInit, OnDestroy {
  dons?: IDon[];
  eventSubscriber?: Subscription;

  constructor(protected donService: DonService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.donService.query().subscribe((res: HttpResponse<IDon[]>) => {
      this.dons = res.body ? res.body : [];
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDons();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDon): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDons(): void {
    this.eventSubscriber = this.eventManager.subscribe('donListModification', () => this.loadAll());
  }

  delete(don: IDon): void {
    const modalRef = this.modalService.open(DonDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.don = don;
  }
}
