import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Point, Result } from './data-types';
import { Observable } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SharedDataService {
  isAttemptingLogin = false;
  authError = false;
  authenticated = false;
  selectedRadius: number;
  username: string;
  password: string;

  points: Point[] = [];
  results: Result[] = [];

  backendUrl = 'http://localhost:8080';

  constructor(
    private http: HttpClient
  ) { }

  async fetchResults() {
    const requestPoints = this.http.get(this.backendUrl + '/points', {
      withCredentials: true,
      responseType: 'json',
    }).toPromise();
    const requestResults = this.http.get(this.backendUrl + '/results', {
      withCredentials: true,
      responseType: 'json',
    }).toPromise();
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

  register(): Observable<object> {
    const formData = new FormData();
    formData.append('username', this.username);
    formData.append('password', this.password);
    this.isAttemptingLogin = true;
    return this.http.post(this.backendUrl + '/register', formData, { withCredentials: true }).pipe(tap(
      next => {
        this.authError = false;
        this.authenticated = true;
      },
      error => {
        this.authError = true;
      },
      () => { this.isAttemptingLogin = false; }
    ));
  }

  login(): Observable<object> {
    const formData = new FormData();
    formData.append('username', this.username);
    formData.append('password', this.password);
    this.isAttemptingLogin = true;
    return this.http.post(this.backendUrl + '/login', formData, { withCredentials: true }).pipe(tap(
      next => {
        this.authError = false;
        this.authenticated = true;
      },
      error => {
        this.authError = true;
      },
      () => { this.isAttemptingLogin = false; }
    ));
  }

  logout(): Observable<object> {
    return this.http.get(this.backendUrl + '/logout').pipe(tap(
      () => { this.authenticated = false; }
    ));
  }
}
