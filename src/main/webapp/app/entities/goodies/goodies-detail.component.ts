import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IGoodies } from 'app/shared/model/goodies.model';

@Component({
  selector: 'jhi-goodies-detail',
  templateUrl: './goodies-detail.component.html'
})
export class GoodiesDetailComponent implements OnInit {
  goodies: IGoodies | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ goodies }) => {
      this.goodies = goodies;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
