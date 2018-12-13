import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {GeneticResource} from '../model/geneticResource';
import {environment} from '../../environments/environment';

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
}
