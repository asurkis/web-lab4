import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SharedDataService } from '../shared-data.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  constructor(
    private shared: SharedDataService,
    private router: Router
  ) {
    this.shared.authError = false;
    this.shared.authAttemptPromise.then(_ => this.router.navigateByUrl('/')).catch(_ => {});
  }

  login() {
    this.shared.login().then(_ => { this.router.navigateByUrl('/'); }).catch(_ => {});
  }
}
