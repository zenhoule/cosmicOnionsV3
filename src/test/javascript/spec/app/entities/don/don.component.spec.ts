import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CosmiconionsTestModule } from '../../../test.module';
import { DonComponent } from 'app/entities/don/don.component';
import { DonService } from 'app/entities/don/don.service';
import { Don } from 'app/shared/model/don.model';

describe('Component Tests', () => {
  describe('Don Management Component', () => {
    let comp: DonComponent;
    let fixture: ComponentFixture<DonComponent>;
    let service: DonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CosmiconionsTestModule],
        declarations: [DonComponent],
        providers: []
      })
        .overrideTemplate(DonComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DonComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DonService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Don(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.dons && comp.dons[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
