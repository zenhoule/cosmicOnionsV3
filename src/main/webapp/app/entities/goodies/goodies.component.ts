import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGoodies } from 'app/shared/model/goodies.model';
import { GoodiesService } from './goodies.service';
import { GoodiesDeleteDialogComponent } from './goodies-delete-dialog.component';

@Component({
  selector: 'jhi-goodies',
  templateUrl: './goodies.component.html'
})
export class GoodiesComponent implements OnInit, OnDestroy {
  goodies?: IGoodies[];
  eventSubscriber?: Subscription;

  constructor(
    protected goodiesService: GoodiesService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.goodiesService.query().subscribe((res: HttpResponse<IGoodies[]>) => {
      this.goodies = res.body ? res.body : [];
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInGoodies();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IGoodies): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInGoodies(): void {
    this.eventSubscriber = this.eventManager.subscribe('goodiesListModification', () => this.loadAll());
  }

  delete(goodies: IGoodies): void {
    const modalRef = this.modalService.open(GoodiesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.goodies = goodies;
  }
}
