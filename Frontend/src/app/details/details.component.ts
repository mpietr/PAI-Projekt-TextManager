import { Observable } from 'rxjs';
import { TextResource } from './../clients/textresource';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TextClient } from '../clients/text.client';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit{

  public textResource: Observable<TextResource> | undefined;

  constructor(
    private route: ActivatedRoute,
    private textClient: TextClient) {}

  ngOnInit() {
    this.route.paramMap
    .subscribe(params => {
      let code = params.get('code')
      if (code !== null) {
        console.log(code);
        this.textResource = this.textClient.getTextByCode(code);
      };
    })
    
  }
}
