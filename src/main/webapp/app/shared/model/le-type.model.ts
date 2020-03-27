import { IProjet } from 'app/shared/model/projet.model';

export interface ILeType {
  id?: number;
  nom?: string;
  projets?: IProjet[];
}

export class LeType implements ILeType {
  constructor(public id?: number, public nom?: string, public projets?: IProjet[]) {}
}
