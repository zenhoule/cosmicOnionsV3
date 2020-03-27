import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CosmiconionsTestModule } from '../../../test.module';
import { DonDetailComponent } from 'app/entities/don/don-detail.component';
import { Don } from 'app/shared/model/don.model';

describe('Component Tests', () => {
  describe('Don Management Detail Component', () => {
    let comp: DonDetailComponent;
    let fixture: ComponentFixture<DonDetailComponent>;
    const route = ({ data: of({ don: new Don(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CosmiconionsTestModule],
        declarations: [DonDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DonDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DonDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load don on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.don).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
