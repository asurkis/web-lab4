import { Component, OnInit } from '@angular/core';
import { SelectItem } from 'primeng/api';
import { SharedDataService } from '../shared-data.service';

@Component({
  selector: 'app-user-input',
  templateUrl: './user-input.component.html',
  styleUrls: ['./user-input.component.css']
})
export class UserInputComponent implements OnInit {
  x = '0';
  y = '0';
  validXs: SelectItem[] = [];
  validYs = {
    min: -5,
    max: 3,
  };
  validRs: SelectItem[] = [];

  constructor(
    private sharedData: SharedDataService
  ) {
    for (let i = -4; i <= 4; i++) {
      this.validXs.push({
        label: '' + i,
        value: '' + i,
      });
    }

    for (let i = 0; i <= 4; i++) {
      this.validRs.push({
        label: '' + i,
        value: '' + i,
      });
    }
  }

  ngOnInit() {
  }

  get r(): string {
    return '' + this.sharedData.selectedRadius;
  }

  set r(r: string) {
    this.sharedData.selectedRadius = +r;
  }

  get isFormValid(): boolean {
    return '' !== this.y && this.validYs.min <= +this.y && +this.y <= this.validYs.max;
  }

  handleClick(event) {
    console.log({
      x: this.x,
      y: this.y,
      r: this.r,
    });
  }
}
