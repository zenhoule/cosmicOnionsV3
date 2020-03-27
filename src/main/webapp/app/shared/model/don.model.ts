import { IUser } from 'app/core/user/user.model';
import { IProjet } from 'app/shared/model/projet.model';

export interface IDon {
  id?: number;
  montant?: number;
  user?: IUser;
  projet?: IProjet;
}

export class Don implements IDon {
  constructor(public id?: number, public montant?: number, public user?: IUser, public projet?: IProjet) {}
}
