import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { CosmiconionsTestModule } from '../../../test.module';
import { GoodiesDetailComponent } from 'app/entities/goodies/goodies-detail.component';
import { Goodies } from 'app/shared/model/goodies.model';

describe('Component Tests', () => {
  describe('Goodies Management Detail Component', () => {
    let comp: GoodiesDetailComponent;
    let fixture: ComponentFixture<GoodiesDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ goodies: new Goodies(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CosmiconionsTestModule],
        declarations: [GoodiesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(GoodiesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GoodiesDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load goodies on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.goodies).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
