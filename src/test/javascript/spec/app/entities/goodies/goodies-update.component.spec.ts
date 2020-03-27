import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CosmiconionsTestModule } from '../../../test.module';
import { GoodiesUpdateComponent } from 'app/entities/goodies/goodies-update.component';
import { GoodiesService } from 'app/entities/goodies/goodies.service';
import { Goodies } from 'app/shared/model/goodies.model';

describe('Component Tests', () => {
  describe('Goodies Management Update Component', () => {
    let comp: GoodiesUpdateComponent;
    let fixture: ComponentFixture<GoodiesUpdateComponent>;
    let service: GoodiesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CosmiconionsTestModule],
        declarations: [GoodiesUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(GoodiesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GoodiesUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GoodiesService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Goodies(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Goodies();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
