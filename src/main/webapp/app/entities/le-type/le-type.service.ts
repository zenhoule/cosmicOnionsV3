import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILeType } from 'app/shared/model/le-type.model';

type EntityResponseType = HttpResponse<ILeType>;
type EntityArrayResponseType = HttpResponse<ILeType[]>;

@Injectable({ providedIn: 'root' })
export class LeTypeService {
  public resourceUrl = SERVER_API_URL + 'api/le-types';

  constructor(protected http: HttpClient) {}

  create(leType: ILeType): Observable<EntityResponseType> {
    return this.http.post<ILeType>(this.resourceUrl, leType, { observe: 'response' });
  }

  update(leType: ILeType): Observable<EntityResponseType> {
    return this.http.put<ILeType>(this.resourceUrl, leType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
