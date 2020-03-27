import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeType } from 'app/shared/model/le-type.model';
import { LeTypeService } from './le-type.service';
import { LeTypeDeleteDialogComponent } from './le-type-delete-dialog.component';

@Component({
  selector: 'jhi-le-type',
  templateUrl: './le-type.component.html'
})
export class LeTypeComponent implements OnInit, OnDestroy {
  leTypes?: ILeType[];
  eventSubscriber?: Subscription;

  constructor(protected leTypeService: LeTypeService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.leTypeService.query().subscribe((res: HttpResponse<ILeType[]>) => {
      this.leTypes = res.body ? res.body : [];
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLeTypes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILeType): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLeTypes(): void {
    this.eventSubscriber = this.eventManager.subscribe('leTypeListModification', () => this.loadAll());
  }

  delete(leType: ILeType): void {
    const modalRef = this.modalService.open(LeTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.leType = leType;
  }
}
