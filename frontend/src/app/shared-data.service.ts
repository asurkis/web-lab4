import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Point, Result } from './data-types';

@Injectable({
  providedIn: 'root'
})
export class SharedDataService {
  isAttemptingLogin = false;
  authError = false;
  authenticated = true;
  selectedRadius: number;
  username: string;
  password: string;

  points: Point[] = [];
  results: Result[] = [];

  backendUrl = 'http://localhost:8080';

  constructor(
    private http: HttpClient
  ) { }

  async updateResults() {
    const requestPoints = this.http.get(this.backendUrl + '/points').toPromise();
    const requestResults = this.http.get(this.backendUrl + '/results').toPromise();
    const responsePoints: any = await requestPoints;
    const responseResults: any = await requestResults;

    const points = {};
    const results = {};
    for (const p of responsePoints._embedded.points) {
      points[p._links.self.href] = p;
    }
    for (const r of responseResults._embedded.results) {
      results[r._links.self.href] = r;
      r.point = points[r._links.point.href];
    }

    this.points = [];
    for (const idx in points) {
      this.points.push(points[idx]);
    }
    this.results = [];
    for (const idx in results) {
      this.results.push(results[idx]);
    }
  }

  login() {
    const headers = new HttpHeaders({
      authorization: 'Basic ' + btoa(this.username + ':' + this.password)
    });
    this.http.post(this.backendUrl + '/login', { headers }).toPromise()
    .then(resp => {
      console.log(resp);
      this.authError = false;
    }, err => {
      console.error(err);
      this.authError = true;
    })
    .finally(() => console.log('finally'));
  }

  logout() {
    this.http.get(this.backendUrl + '/logout').toPromise()
      .then(r => console.log('then', r), e => console.log('catch', e))
      .finally(() => console.log('finally'));
  }

  register() {
    this.http.post(this.backendUrl + '/register', `username=${this.username}&password=${this.password}`).toPromise()
      .then(r => console.log('then', r))
      .catch(e => console.log('catch', e))
      .finally(() => console.log('finally'));
  }
}
