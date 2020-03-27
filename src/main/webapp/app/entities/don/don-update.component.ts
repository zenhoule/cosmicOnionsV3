import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IDon, Don } from 'app/shared/model/don.model';
import { DonService } from './don.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IProjet } from 'app/shared/model/projet.model';
import { ProjetService } from 'app/entities/projet/projet.service';

type SelectableEntity = IUser | IProjet;

@Component({
  selector: 'jhi-don-update',
  templateUrl: './don-update.component.html'
})
export class DonUpdateComponent implements OnInit {
  isSaving = false;

  users: IUser[] = [];

  projets: IProjet[] = [];

  editForm = this.fb.group({
    id: [],
    montant: [],
    user: [],
    projet: []
  });

  constructor(
    protected donService: DonService,
    protected userService: UserService,
    protected projetService: ProjetService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ don }) => {
      this.updateForm(don);

      this.userService
        .query()
        .pipe(
          map((res: HttpResponse<IUser[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IUser[]) => (this.users = resBody));

      this.projetService
        .query()
        .pipe(
          map((res: HttpResponse<IProjet[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IProjet[]) => (this.projets = resBody));
    });
  }

  updateForm(don: IDon): void {
    this.editForm.patchValue({
      id: don.id,
      montant: don.montant,
      user: don.user,
      projet: don.projet
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const don = this.createFromForm();
    if (don.id !== undefined) {
      this.subscribeToSaveResponse(this.donService.update(don));
    } else {
      this.subscribeToSaveResponse(this.donService.create(don));
    }
  }

  private createFromForm(): IDon {
    return {
      ...new Don(),
      id: this.editForm.get(['id'])!.value,
      montant: this.editForm.get(['montant'])!.value,
      user: this.editForm.get(['user'])!.value,
      projet: this.editForm.get(['projet'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDon>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
