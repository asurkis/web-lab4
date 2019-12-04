import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SharedDataService } from '../shared-data.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  confirmPassword: string;

  constructor(
    private shared: SharedDataService,
    private router: Router
  ) { }

  ngOnInit() {
    this.shared.authError = false;
    if (this.shared.authenticated) {
      this.router.navigateByUrl('/');
    }
  }

  register() {
    this.shared.register().subscribe(
      next => { this.router.navigateByUrl('/'); }
    );
  }
}
