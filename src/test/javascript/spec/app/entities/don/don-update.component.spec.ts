import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CosmiconionsTestModule } from '../../../test.module';
import { DonUpdateComponent } from 'app/entities/don/don-update.component';
import { DonService } from 'app/entities/don/don.service';
import { Don } from 'app/shared/model/don.model';

describe('Component Tests', () => {
  describe('Don Management Update Component', () => {
    let comp: DonUpdateComponent;
    let fixture: ComponentFixture<DonUpdateComponent>;
    let service: DonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CosmiconionsTestModule],
        declarations: [DonUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DonUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DonService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Don(123);
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
        const entity = new Don();
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
