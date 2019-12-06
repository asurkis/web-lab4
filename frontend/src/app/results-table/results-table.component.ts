import { Component, OnInit } from '@angular/core';
import { SharedDataService } from '../shared-data.service';
import { Result } from '../data-types';

@Component({
  selector: 'app-results-table',
  templateUrl: './results-table.component.html',
  styleUrls: ['./results-table.component.css']
})
export class ResultsTableComponent {
  results: Result[] = [];

  constructor(
    private shared: SharedDataService
  ) { }

  handleClick() {
    this.shared.pushChanges();
  }
}
