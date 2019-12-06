import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SharedDataService } from '../shared-data.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  confirmPassword = '';

  constructor(
    private shared: SharedDataService,
    private router: Router
  ) {
    shared.authError = false;
    this.shared.authAttemptPromise.then(_ => this.router.navigateByUrl('/')).catch(_ => {});
  }

  register() {
    this.shared.register().then(_ => { this.router.navigateByUrl('/'); }).catch(_ => {});
  }
}
