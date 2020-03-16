import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { CosmiconionsSharedModule } from 'app/shared/shared.module';
import { CosmiconionsCoreModule } from 'app/core/core.module';
import { CosmiconionsAppRoutingModule } from './app-routing.module';
import { CosmiconionsHomeModule } from './home/home.module';
import { CosmiconionsEntityModule } from './entities/entity.module';
import { CosmiconionsAppContactModule } from './contact/contact.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    CosmiconionsSharedModule,
    CosmiconionsCoreModule,
    CosmiconionsHomeModule,
    CosmiconionsAppContactModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    CosmiconionsEntityModule,
    CosmiconionsAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent]
})
export class CosmiconionsAppModule {}
