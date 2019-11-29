import { Component, OnInit } from '@angular/core';
import { SharedDataService } from '../shared-data.service';

@Component({
  selector: 'app-results-picture',
  templateUrl: './results-picture.component.html',
  styleUrls: ['./results-picture.component.css']
})
export class ResultsPictureComponent implements OnInit {
  scale = 50;
  displayRect = {
    left: -6,
    bottom: -6,
    right: 6,
    top: 6,
  };
  pictureWidth = Math.abs(this.translateX(this.displayRect.right) - this.translateX(this.displayRect.left));
  pictureHeight = Math.abs(this.translateY(this.displayRect.top) - this.translateY(this.displayRect.bottom));

  maxCoord = 5;
  points = [
    { x: -1, y: -1 },
    { x: 1, y: 1 },
  ];

  constructor(
    private sharedData: SharedDataService
  ) { }

  ngOnInit() {
  }

  get radius(): number {
    return this.sharedData.selectedRadius;
  }

  set radius(r: number) {
    this.sharedData.selectedRadius = r;
  }

  translateX(x: number): number {
    return (x - this.displayRect.left) * this.scale;
  }

  translateY(y: number): number {
    return (-y - this.displayRect.bottom) * this.scale;
  }

  translateCoord(x: number, y: number): { x: number, y: number } {
    return {
      x: this.translateX(x),
      y: this.translateY(y),
    };
  }

  pictureX(x: number) {

  }

  pictureCoord(x: number, y: number): string {
    const translated = this.translateCoord(x, y);
    return ' ' + translated.x + ' ' + translated.y + ' ';
  }

  get markListX(): number[] {
    const result = [];
    for (let i = this.displayRect.left + 1; i < this.displayRect.right; i++) {
      result.push(i);
    }
    return result;
  }

  get markListY(): number[] {
    const result = [];
    for (let i = this.displayRect.bottom + 1; i < this.displayRect.top; i++) {
      if (i !== 0) {
        result.push(i);
      }
    }
    return result;
  }

  pointColor(point: { x: number, y: number }): string {
    if (!this.radius) {
      return '#eee';
    }
    return '#ff0';
  }
}
