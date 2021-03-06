import { IDon } from 'app/shared/model/don.model';
import { IMessage } from 'app/shared/model/message.model';
import { IUser } from 'app/core/user/user.model';
import { ICategorie } from 'app/shared/model/categorie.model';

export interface IProjet {
  id?: number;
  nom?: string;
  description?: any;
  photoContentType?: string;
  photo?: any;
  videoContentType?: string;
  video?: any;
  objectif?: number;
  soldeCours?: number;
  nbJoursRestant?: number;
  dons?: IDon[];
  messages?: IMessage[];
  user?: IUser;
  categorie?: ICategorie;
}

export class Projet implements IProjet {
  constructor(
    public id?: number,
    public nom?: string,
    public description?: any,
    public photoContentType?: string,
    public photo?: any,
    public videoContentType?: string,
    public video?: any,
    public objectif?: number,
    public soldeCours?: number,
    public nbJoursRestant?: number,
    public dons?: IDon[],
    public messages?: IMessage[],
    public user?: IUser,
    public categorie?: ICategorie
  ) {}
}
