import { environment } from '../../environments/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { User } from "./user";

@Injectable({
  providedIn: 'root',
})
export class AuthenticationClient {
  constructor(private http: HttpClient) {}

  public login(username: string, password: string): Observable<string> {
    return this.http.post(
      environment.apiUrl + '/user/login',
      {
        username: username,
        password: password,
      },
      { responseType: 'text' }
    ).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          alert("No user with such username");
        }
        if (error.status == 401) {
          alert("Wrong password or username");
        }
        return throwError(error);
      })
    );
  }

  public register(
    username: string,
    email: string,
    password: string
  ): Observable<string> {

    return this.http.post(
      environment.apiUrl + '/user/register',
      {
        username: username,
        email: email,
        password: password
      },
      { responseType: 'text' }
    ).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 400) {
          alert("Email or username already taken");
        }
        return throwError(error);
      })
    );
  }

  public remind(
    email: string
  ): Observable<object> {
    return this.http.get(environment.apiUrl + '/user/req-reset/' + email).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 400) {
          alert("No such email in database");
        }
        return throwError(error);
      })
    );
  }

  public reset(
    code: string,
    password: string
  ): Observable<object> {
    return this.http.post(environment.apiUrl + '/user/reset',
    {
      code: code,
      password: password
    }
    ).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 400) {
          alert("Invalid reset link");
        }
        return throwError(error);
      })
    );
  }

}