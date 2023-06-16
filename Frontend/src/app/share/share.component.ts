import { Component, OnInit } from '@angular/core';
import { TextResource } from '../clients/textresource';
import { Observable, map } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { TextClient } from '../clients/text.client';

@Component({
  selector: 'app-share',
  templateUrl: './share.component.html',
  styleUrls: ['./share.component.css']
})
export class ShareComponent implements OnInit {
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
