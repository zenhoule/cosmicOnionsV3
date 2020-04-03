import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IProjet, Projet } from 'app/shared/model/projet.model';
import { ProjetService } from './projet.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ICategorie } from 'app/shared/model/categorie.model';
import { CategorieService } from 'app/entities/categorie/categorie.service';

type SelectableEntity = IUser | ICategorie;

@Component({
  selector: 'jhi-projet-update',
  templateUrl: './projet-update.component.html'
})
export class ProjetUpdateComponent implements OnInit {
  isSaving = false;

  users: IUser[] = [];

  categories: ICategorie[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    description: [],
    photo: [],
    photoContentType: [],
    video: [],
    videoContentType: [],
    objectif: [],
    soldeCours: [],
    nbJoursRestant: [],
    user: [],
    categorie: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected projetService: ProjetService,
    protected userService: UserService,
    protected categorieService: CategorieService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projet }) => {
      this.updateForm(projet);

      this.userService
        .query()
        .pipe(
          map((res: HttpResponse<IUser[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IUser[]) => (this.users = resBody));

      this.categorieService
        .query()
        .pipe(
          map((res: HttpResponse<ICategorie[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ICategorie[]) => (this.categories = resBody));
    });
  }

  updateForm(projet: IProjet): void {
    this.editForm.patchValue({
      id: projet.id,
      nom: projet.nom,
      description: projet.description,
      photo: projet.photo,
      photoContentType: projet.photoContentType,
      video: projet.video,
      videoContentType: projet.videoContentType,
      objectif: projet.objectif,
      soldeCours: projet.soldeCours,
      nbJoursRestant: projet.nbJoursRestant,
      user: projet.user,
      categorie: projet.categorie
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('cosmiconionsApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const projet = this.createFromForm();
    if (projet.id !== undefined) {
      this.subscribeToSaveResponse(this.projetService.update(projet));
    } else {
      this.subscribeToSaveResponse(this.projetService.create(projet));
    }
  }

  private createFromForm(): IProjet {
    return {
      ...new Projet(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      description: this.editForm.get(['description'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      videoContentType: this.editForm.get(['videoContentType'])!.value,
      video: this.editForm.get(['video'])!.value,
      objectif: this.editForm.get(['objectif'])!.value,
      soldeCours: this.editForm.get(['soldeCours'])!.value,
      nbJoursRestant: this.editForm.get(['nbJoursRestant'])!.value,
      user: this.editForm.get(['user'])!.value,
      categorie: this.editForm.get(['categorie'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProjet>>): void {
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
