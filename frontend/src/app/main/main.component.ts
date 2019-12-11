import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SharedDataService } from '../shared-data.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {
  constructor(
    private shared: SharedDataService,
    private router: Router
  ) {
    shared.authError = false;
    shared.authAttemptPromise
      .then(_ => shared.fetchResults())
      .catch(_ => router.navigateByUrl('/login'));
  }

  logout() {
    this.shared.logout().finally(() => { this.router.navigateByUrl('/login'); });
  }
}
