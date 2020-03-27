import { IUser } from 'app/core/user/user.model';

export interface IGoodies {
  id?: number;
  nom?: string;
  photoContentType?: string;
  photo?: any;
  palier?: number;
  user?: IUser;
}

export class Goodies implements IGoodies {
  constructor(
    public id?: number,
    public nom?: string,
    public photoContentType?: string,
    public photo?: any,
    public palier?: number,
    public user?: IUser
  ) {}
}
