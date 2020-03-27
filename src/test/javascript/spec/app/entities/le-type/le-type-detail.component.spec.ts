import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CosmiconionsTestModule } from '../../../test.module';
import { LeTypeDetailComponent } from 'app/entities/le-type/le-type-detail.component';
import { LeType } from 'app/shared/model/le-type.model';

describe('Component Tests', () => {
  describe('LeType Management Detail Component', () => {
    let comp: LeTypeDetailComponent;
    let fixture: ComponentFixture<LeTypeDetailComponent>;
    const route = ({ data: of({ leType: new LeType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CosmiconionsTestModule],
        declarations: [LeTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LeTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LeTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load leType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.leType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
