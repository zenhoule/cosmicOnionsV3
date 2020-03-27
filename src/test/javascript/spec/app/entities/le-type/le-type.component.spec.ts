import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CosmiconionsTestModule } from '../../../test.module';
import { LeTypeComponent } from 'app/entities/le-type/le-type.component';
import { LeTypeService } from 'app/entities/le-type/le-type.service';
import { LeType } from 'app/shared/model/le-type.model';

describe('Component Tests', () => {
  describe('LeType Management Component', () => {
    let comp: LeTypeComponent;
    let fixture: ComponentFixture<LeTypeComponent>;
    let service: LeTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CosmiconionsTestModule],
        declarations: [LeTypeComponent],
        providers: []
      })
        .overrideTemplate(LeTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeTypeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LeTypeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LeType(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.leTypes && comp.leTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
