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
        .then((r: any) => {
          this.username = r.username;
          resolve(r);
        })
        .catch(r => reject(r))
        .finally(() => this.isAttemptingLogin = false);
    });
  }

  async fetchResultsOfPoint(points: any[]): Promise<any[]> {
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
        r.toDelete = false;
        r.point = p;
      }
    }
    return results;
  }

  async fetchResults() {
    const responsePoints: any = await this.http.get(this.backendUrl + '/me/points', {
      withCredentials: true,
      responseType: 'json',
    }).toPromise();

    if (!responsePoints._embedded) {
      this.points = [];
      this.results = [];
      return;
    }

    const points = responsePoints._embedded.points;
    const results = await this.fetchResultsOfPoint(points);

    this.points = points;
    this.results = results;
  }

  async pushChanges() {
    try {
      const responseResults: any = await this.http.post(this.backendUrl + '/change', this.results.map(r => ({
        id: r.id,
        toDelete: r.toDelete === true,
        radius: r.radius
      })), {
        withCredentials: true
      }).toPromise();
      await this.fetchResults();
    } catch (err) {
      console.log(err);
    } finally {}
  }

  async pushRequest({ x, y }: Point) {
    try {
      const responsePoints: any = await this.http.post(this.backendUrl + '/add', [{
        x: +x,
        y: +y,
        rs: [ this.selectedRadius ]
      }], {
        withCredentials: true
      }).toPromise();

      if (!responsePoints._embedded) {
        return;
      }

      const points = responsePoints._embedded.points;
      const results = await this.fetchResultsOfPoint(points);

      for (const p of points) {
        this.points.push(p);
      }
      for (const r of results) {
        this.results.push(r);
      }
    } finally {}
  }

  async registerOrLogin(what: string): Promise<any> {
    const formData = new FormData();
    formData.append('username', this.username);
    formData.append('password', this.password);
    this.isAttemptingLogin = true;
    try {
      const result = await this.http.post(this.backendUrl + '/' + what, {
        username: this.username,
        password: this.password
      }, {
        observe: 'response',
        withCredentials: true
      }).toPromise();
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
    return this.authAttemptPromise = new Promise((_, reject) => {
      this.http.get(this.backendUrl + '/logout', {
        withCredentials: true,
        responseType: 'json'
      }).toPromise().finally(() => {
        reject();
      });
    });
  }
}
