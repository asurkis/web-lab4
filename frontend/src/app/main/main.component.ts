import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SharedDataService } from '../shared-data.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  constructor(
    private shared: SharedDataService,
    private router: Router
  ) { }

  ngOnInit() {
    this.shared.authError = false;
    if (!this.shared.authenticated) {
      this.router.navigateByUrl('/login');
      return;
    }

    this.shared.updateResults();
  }

  logout() {

  }
}
