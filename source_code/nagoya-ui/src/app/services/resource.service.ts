import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {GeneticResource} from '../model/geneticResource';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {Taxonomy} from '../model/taxonomy';

@Injectable({
  providedIn: 'root'
})
export class ResourceService {

  constructor(private http: HttpClient) {
  }


  public create(geneticResource) {
    return this.http.put<GeneticResource>(environment.serverUrl + 'genetics/', geneticResource, {
      headers: new HttpHeaders({'Content-Type': 'application/json'}),
      observe: 'response'
    });
  }

  public getAll(): Observable<GeneticResource []> {
    return this.http.post<GeneticResource []>(environment.serverUrl + 'genetics/search', {}, {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    });
  }

  public search(geneticResourceFilter): Observable<GeneticResource []> {
    return this.http.post<GeneticResource []>(environment.serverUrl + 'genetics/search', geneticResourceFilter, {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    });
  }

  public getTaxonomy(taxonomy: Taxonomy): Observable<Taxonomy []> {
    let parentId = '';
    if (taxonomy) {
      parentId = taxonomy.id;
    }
    return this.http.get<Taxonomy[]>(environment.serverUrl + 'genetics/search/taxonomy/' + parentId);
  }


}
