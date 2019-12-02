import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SharedDataService {
  isAttemptingLogin = false;
  selectedRadius: number;
  authenticated: boolean;
  username: string;
  password: string;

  constructor(
    private http: HttpClient
  ) { }

  login() {

  }

  logout() {

  }

  register() {

  }
}
