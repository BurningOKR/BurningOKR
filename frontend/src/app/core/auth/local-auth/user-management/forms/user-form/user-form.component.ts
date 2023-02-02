import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { environment } from '../../../../../../../environments/environment';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css'],
})
export class UserFormComponent implements OnInit {
  @Input() userForm: FormGroup;
  @Input() canEditAdminStatus: boolean;

  @ViewChild('canvasElement') canvas;

  isPlayground: boolean = environment.playground;

  ngOnInit(): void {
    if ((this.userForm.get('email').value === 'iwant@burningokr') && this.isPlayground) {
      this.userForm.get('email')
        .disable();
    }
  }

  setPhoto($event: any): void {
    const files: FileList = $event.target.files;
    if (files.length) {
      const file: File = files.item(0);
      const reader: FileReader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = e => {
        const img: HTMLImageElement = new Image();
        img.src = (e.target as any).result;
        img.onload = () => {
          const ctx: CanvasRenderingContext2D = this.canvas.nativeElement.getContext('2d') as CanvasRenderingContext2D;
          ctx.drawImage(img, 0, 0, 250, 250);

          const dataUrl: string = (this.canvas.nativeElement as HTMLCanvasElement).toDataURL('image/jpeg', 0.7);
          this.userForm.controls.photo.setValue(dataUrl.substring(23, dataUrl.length));
        };
      };
    }
  }
}
