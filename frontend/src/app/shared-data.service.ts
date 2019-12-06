import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Point, Result } from './data-types';

@Injectable({
  providedIn: 'root'
})
export class SharedDataService {
  authAttemptPromise: Promise<any>;
  isAttemptingLogin: boolean;
  authError = false;
  authenticated: boolean;
  selectedRadius = 0;
  username = '';
  password = '';

  points: Point[] = [];
  results: Result[] = [];

  backendUrl = 'http://localhost:8080';

  constructor(
    private http: HttpClient
  ) {
    this.isAttemptingLogin = true;
    this.authAttemptPromise = new Promise((resolve, reject) => {
      http.get(this.backendUrl + '/me', { withCredentials: true, responseType: 'json' }).toPromise()
        .then(r => { resolve(r); this.authenticated = true; })
        .catch(r => { reject(r); this.authenticated = false; })
        .finally(() => this.isAttemptingLogin = false);
    });
  }

  async fetchResults() {
    try {
      const responsePoints: any = await this.http.get(this.backendUrl + '/me/points', {
        withCredentials: true,
        responseType: 'json',
      }).toPromise();

      const points = responsePoints._embedded.points;
      const results = [];

      for (const p of points) {
        p.requestResults = this.http.get(p._links.results.href, {
          withCredentials: true,
          responseType: 'json'
        }).toPromise();
      }

      for (const p of points) {
        const responseResults = await p.requestResults;
        for (const r of responseResults._embedded.results) {
          results.push(r);
          r.point = p;
        }
      }

      this.points = points;
      this.results = results;
    } catch (err) {
      console.log(err);
    }
  }

  async pushChanges() {

  }

  async pushRequest({ x, y }: Point) {
    const formData = new FormData();
    formData.append('x', '' + x);
    formData.append('y', '' + y);
    formData.append('r', '' + this.selectedRadius);
    this.http.post(this.backendUrl + '/add', formData, { withCredentials: true }).subscribe(
      next => console.log(next),
      err => console.log(err),
      () => console.log('complete'),
    );
  }

  async registerOrLogin(what: string): Promise<any> {
    const formData = new FormData();
    formData.append('username', this.username);
    formData.append('password', this.password);
    this.isAttemptingLogin = true;
    try {
      const result = await this.http.post(this.backendUrl + '/' + what,
          formData, { observe: 'response', withCredentials: true }).toPromise();
      this.authenticated = true;
      return result;
    } catch (err) {
      this.authError = true;
      throw err;
    } finally {
      this.isAttemptingLogin = false;
    }
  }

  register(): Promise<any> {
    return this.authAttemptPromise = this.registerOrLogin('register');
  }

  login(): Promise<any> {
    return this.authAttemptPromise = this.registerOrLogin('login');
  }

  logout(): Promise<any> {
    return this.authAttemptPromise = new Promise((resolve, reject) => {
      this.http.get(this.backendUrl + '/logout', {
        withCredentials: true,
        responseType: 'json'
      }).toPromise().finally(() => {
        this.authenticated = false;
        reject();
      });
    });
  }
}
