import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeType } from 'app/shared/model/le-type.model';

@Component({
  selector: 'jhi-le-type-detail',
  templateUrl: './le-type-detail.component.html'
})
export class LeTypeDetailComponent implements OnInit {
  leType: ILeType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leType }) => {
      this.leType = leType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
