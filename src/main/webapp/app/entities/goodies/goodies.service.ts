import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IGoodies } from 'app/shared/model/goodies.model';

type EntityResponseType = HttpResponse<IGoodies>;
type EntityArrayResponseType = HttpResponse<IGoodies[]>;

@Injectable({ providedIn: 'root' })
export class GoodiesService {
  public resourceUrl = SERVER_API_URL + 'api/goodies';

  constructor(protected http: HttpClient) {}

  create(goodies: IGoodies): Observable<EntityResponseType> {
    return this.http.post<IGoodies>(this.resourceUrl, goodies, { observe: 'response' });
  }

  update(goodies: IGoodies): Observable<EntityResponseType> {
    return this.http.put<IGoodies>(this.resourceUrl, goodies, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGoodies>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGoodies[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
