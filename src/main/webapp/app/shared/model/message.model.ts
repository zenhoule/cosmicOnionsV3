import { IUser } from 'app/core/user/user.model';
import { IProjet } from 'app/shared/model/projet.model';

export interface IMessage {
  id?: number;
  message?: any;
  user?: IUser;
  projet?: IProjet;
}

export class Message implements IMessage {
  constructor(public id?: number, public message?: any, public user?: IUser, public projet?: IProjet) {}
}
