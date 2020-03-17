import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['contact.component.scss']
})
export class ContactComponent implements OnInit {
  message: string;

  constructor() {
    this.message = '';
  }

  ngOnInit(): void {}
}
