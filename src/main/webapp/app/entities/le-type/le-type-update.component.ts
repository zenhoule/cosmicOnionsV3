import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILeType, LeType } from 'app/shared/model/le-type.model';
import { LeTypeService } from './le-type.service';

@Component({
  selector: 'jhi-le-type-update',
  templateUrl: './le-type-update.component.html'
})
export class LeTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: []
  });

  constructor(protected leTypeService: LeTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leType }) => {
      this.updateForm(leType);
    });
  }

  updateForm(leType: ILeType): void {
    this.editForm.patchValue({
      id: leType.id,
      nom: leType.nom
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leType = this.createFromForm();
    if (leType.id !== undefined) {
      this.subscribeToSaveResponse(this.leTypeService.update(leType));
    } else {
      this.subscribeToSaveResponse(this.leTypeService.create(leType));
    }
  }

  private createFromForm(): ILeType {
    return {
      ...new LeType(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeType>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
