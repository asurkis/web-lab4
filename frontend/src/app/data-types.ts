export interface Point {
  id?: number;
  x: number;
  y: number;
}

export interface Result {
  id?: number;
  point: Point;
  radius: number;
  result: boolean;
}

export function pointFitsIntoRadius({ x, y }: Point, r: number): boolean {
  return (-r <= x && x <= 0 && 0 <= y && y <= r)
      || (0 <= x && 0 <= y && x + y <= .5 * r)
      || (0 <= x && y <= 0 && x * x + y * y <= r * r);
}
