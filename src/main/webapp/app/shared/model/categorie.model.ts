import { IProjet } from 'app/shared/model/projet.model';

export interface ICategorie {
  id?: number;
  nom?: string;
  projets?: IProjet[];
}

export class Categorie implements ICategorie {
  constructor(public id?: number, public nom?: string, public projets?: IProjet[]) {}
}
