import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CosmiconionsTestModule } from '../../../test.module';
import { GoodiesComponent } from 'app/entities/goodies/goodies.component';
import { GoodiesService } from 'app/entities/goodies/goodies.service';
import { Goodies } from 'app/shared/model/goodies.model';

describe('Component Tests', () => {
  describe('Goodies Management Component', () => {
    let comp: GoodiesComponent;
    let fixture: ComponentFixture<GoodiesComponent>;
    let service: GoodiesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CosmiconionsTestModule],
        declarations: [GoodiesComponent],
        providers: []
      })
        .overrideTemplate(GoodiesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GoodiesComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GoodiesService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Goodies(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.goodies && comp.goodies[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
