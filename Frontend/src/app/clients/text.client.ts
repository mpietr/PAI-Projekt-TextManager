import { environment } from './../../environments/environment';
import { Observable, catchError, map, throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TextResource } from './textresource';

@Injectable({
  providedIn: 'root',
})
export class TextClient {
    private tokenKey = 'token';

    constructor(private http: HttpClient) {}

    getAllTexts(): Observable<TextResource[]> {
        let id = localStorage.getItem(this.tokenKey)
        let request = environment.apiUrl + '/text/find/' + id
        return this.http.get<TextResource[]>(request);
    }

    getTextByCode(code: String): Observable<TextResource> {
        return this.http.get<TextResource>(environment.apiUrl + '/text/details/' + code);
    }

    getFilteredTexts(
        name: string,
        text: string,
        tags: string

    ): Observable<TextResource[]> {
        let id = localStorage.getItem(this.tokenKey)
        return this.http.post<TextResource[]>(
            environment.apiUrl + '/text/filter',
            {
                id: id,
                name: name,
                text: text,
                tags: tags
            }
        )
    }

    public addText(
        name: string,
        text: string,
        tags: string
      ): Observable<TextResource> {
        let id = parseInt(localStorage.getItem(this.tokenKey)!);
        
        return this.http.post<TextResource>(
          environment.apiUrl + '/text/add',
          {
            name: name,
            ownerId: { userId: id },
            text: text,
            tags: tags
          }
        ).pipe(
          catchError((error: HttpErrorResponse) => {
            if (error.status === 400) {
              alert("Text with such title already exists");
            }
            return throwError(error);
          })
        );
      }

    public editText(
        name: string,
        text: string,
        tags: string, 
        code: string
      ): Observable<TextResource> {
        let id = parseInt(localStorage.getItem(this.tokenKey)!)
        return this.http.post<TextResource>(
            environment.apiUrl + '/text/update',
            {
              name: name,
              ownerId: {userId: id},
              text: text,
              tags: tags,
              code: code
            }
          );
    }

    public deleteText(
        code: string
      ): Observable<object> {
        return this.http.get(environment.apiUrl + '/text/delete/' + code);
    }

}