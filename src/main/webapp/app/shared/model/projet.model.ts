import { IDon } from 'app/shared/model/don.model';
import { IMessage } from 'app/shared/model/message.model';
import { IUser } from 'app/core/user/user.model';
import { ILeType } from 'app/shared/model/le-type.model';

export interface IProjet {
  id?: number;
  description?: any;
  photoContentType?: string;
  photo?: any;
  objectif?: number;
  soldeCours?: number;
  nbJoursRestant?: number;
  dons?: IDon[];
  messages?: IMessage[];
  user?: IUser;
  leType?: ILeType;
}

export class Projet implements IProjet {
  constructor(
    public id?: number,
    public description?: any,
    public photoContentType?: string,
    public photo?: any,
    public objectif?: number,
    public soldeCours?: number,
    public nbJoursRestant?: number,
    public dons?: IDon[],
    public messages?: IMessage[],
    public user?: IUser,
    public leType?: ILeType
  ) {}
}
