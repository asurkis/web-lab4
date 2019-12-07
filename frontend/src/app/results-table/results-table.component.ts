import { Component } from '@angular/core';
import { SharedDataService } from '../shared-data.service';

@Component({
  selector: 'app-results-table',
  templateUrl: './results-table.component.html',
  styleUrls: ['./results-table.component.css']
})
export class ResultsTableComponent {
  toDeleteList = [];

  constructor(
    private shared: SharedDataService
  ) { }

  handleClick() {
    for (const r of this.toDeleteList) {
      r.toDelete = true;
    }
    this.shared.pushChanges();
  }
}
