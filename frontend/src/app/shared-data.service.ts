import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Point, Result } from './data-types';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class SharedDataService {
  isAttemptingLogin = false;
  authError = false;
  authenticated = false;
  lastAuthToken: string;
  selectedRadius: number;
  username: string;
  password: string;

  points: Point[] = [];
  results: Result[] = [];

  backendUrl = 'http://localhost:8080';

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  async xAuthToken(): Promise<string> {
    if (!this.lastAuthToken) {
      try {
        const response: any = await this.http.get(this.backendUrl + '/token').toPromise();
        this.lastAuthToken = response.token;
      } catch (e) {
        console.log(e);
      } finally {
        console.log('finally');
      }
    }
    return this.lastAuthToken;
  }

  async fetchResults() {
    const headers = new HttpHeaders({
      'X-Auth-Token': await this.xAuthToken()
    });
    const requestPoints = this.http.get(this.backendUrl + '/points', { headers }).toPromise();
    const requestResults = this.http.get(this.backendUrl + '/results', { headers }).toPromise();
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

    this.points = Object.values<Point>(points);
    this.results = Object.values<Result>(results);
  }

  async sendRequest() {
  }

  async pushResults() {
  }

  async login() {
    const headers = new HttpHeaders().set('X-Auth-Token', await this.xAuthToken());
    const formData = new FormData();
    formData.append('username', this.username);
    formData.append('password', this.password);
    try {
      this.isAttemptingLogin = true;
      const resp = await this.http.post(this.backendUrl + '/login', formData, { headers }).toPromise()
      this.authenticated = true;
      this.authError = false;
      this.router.navigateByUrl('/');
    } catch (err) {
      this.authError = true;
    } finally {
      this.isAttemptingLogin = false;
    }
  }

  async logout() {
    this.http.get(this.backendUrl + '/logout').toPromise()
      .then(r => console.log('then', r), e => console.log('catch', e))
      .finally(() => console.log('finally'));
  }

  async register() {
    this.http.post(this.backendUrl + '/register', `username=${this.username}&password=${this.password}`).toPromise()
      .then(r => console.log('then', r))
      .catch(e => console.log('catch', e))
      .finally(() => console.log('finally'));
  }
}
