import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CosmiconionsTestModule } from '../../../test.module';
import { LeTypeUpdateComponent } from 'app/entities/le-type/le-type-update.component';
import { LeTypeService } from 'app/entities/le-type/le-type.service';
import { LeType } from 'app/shared/model/le-type.model';

describe('Component Tests', () => {
  describe('LeType Management Update Component', () => {
    let comp: LeTypeUpdateComponent;
    let fixture: ComponentFixture<LeTypeUpdateComponent>;
    let service: LeTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CosmiconionsTestModule],
        declarations: [LeTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LeTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LeTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LeType(123);
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
        const entity = new LeType();
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
