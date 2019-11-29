import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';

import { AppComponent } from './app.component';
import { MainComponent } from './main/main.component';
import { LoginComponent } from './login/login.component';
import { ResultsTableComponent } from './results-table/results-table.component';
import { ResultsPictureComponent } from './results-picture/results-picture.component';

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    LoginComponent,
    ResultsTableComponent,
    ResultsPictureComponent,
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot([
      { path: '', component: MainComponent },
      { path: 'login', component: LoginComponent },
    ]),
    TableModule,
    InputTextModule,
    PasswordModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
