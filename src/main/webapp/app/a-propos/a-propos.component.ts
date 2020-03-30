import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-a-propos',
  templateUrl: './a-propos.component.html',
  styleUrls: ['a-propos.component.scss']
})
export class AProposComponent implements OnInit {
  message: string;

  constructor() {
    this.message = 'AProposComponent message';
  }

  ngOnInit(): void {}
}
