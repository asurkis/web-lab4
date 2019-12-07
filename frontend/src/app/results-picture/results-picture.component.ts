import { Component } from '@angular/core';
import { SharedDataService } from '../shared-data.service';
import { Point, pointFitsIntoRadius } from '../data-types';

@Component({
  selector: 'app-results-picture',
  templateUrl: './results-picture.component.html',
  styleUrls: ['./results-picture.component.css']
})
export class ResultsPictureComponent {
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

  constructor(
    private shared: SharedDataService
  ) { }

  handleClick(event) {
    const rect = document.querySelector('svg').getBoundingClientRect();
    const clientX = event.x - rect.left;
    const clientY = event.y - rect.top;
    const x = clientX / this.scale - this.maxCoord - 1;
    const y = this.maxCoord + 1 - clientY / this.scale;
    this.shared.pushRequest({ x, y });
  }

  translateX(x: number): number {
    return (x - this.displayRect.left) * this.scale;
  }

  translateY(y: number): number {
    return (-y - this.displayRect.bottom) * this.scale;
  }

  translateCoord(x: number, y: number): Point {
    return {
      x: this.translateX(x),
      y: this.translateY(y),
    };
  }

  pictureCoord(x: number, y: number): string {
    const translated = this.translateCoord(x, y);
    return ' ' + translated.x + ' ' + translated.y + ' ';
  }

  pointClass(point: Point): string {
    return this.radius
        ? (pointFitsIntoRadius(point, this.radius)
            ? 'fits1'
            : 'fits0')
        : 'noRadius';
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

  get radius(): number {
    return this.shared.selectedRadius;
  }
}
