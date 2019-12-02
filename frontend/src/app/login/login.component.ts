import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SharedDataService } from '../shared-data.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(
    private shared: SharedDataService,
    private router: Router
  ) { }

  ngOnInit() {
    if (this.shared.authenticated) {
      this.router.navigateByUrl('/main');
    }
  }
}
