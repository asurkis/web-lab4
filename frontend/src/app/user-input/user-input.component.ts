import { Component, OnInit } from '@angular/core';
import { SelectItem } from 'primeng/api';

@Component({
  selector: 'app-user-input',
  templateUrl: './user-input.component.html',
  styleUrls: ['./user-input.component.css']
})
export class UserInputComponent implements OnInit {
  x = '0';
  y = '0';
  r = '0';
  validXs: SelectItem[] = [];
  validRs: SelectItem[] = [];

  constructor() {
    for (let i = -4; i <= 4; i++) {
      this.validXs.push({
        label: '' + i,
        value: i,
      });
    }

    for (let i = 0; i <= 4; i++) {
      this.validRs.push({
        label: '' + i,
        value: i
      });
    }
  }

  ngOnInit() {
  }

  get isFormValid(): boolean {
    return '' !== this.y && -5 <= +this.y && +this.y <= 3;
  }

  handleClick(event) {
    console.log(event);
  }
}
