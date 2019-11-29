import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultsPictureComponent } from './results-picture.component';

describe('ResultsPictureComponent', () => {
  let component: ResultsPictureComponent;
  let fixture: ComponentFixture<ResultsPictureComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResultsPictureComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResultsPictureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
