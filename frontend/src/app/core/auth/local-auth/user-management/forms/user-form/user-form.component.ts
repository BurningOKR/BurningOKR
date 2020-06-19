import { Component, Input, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent {
  @Input() userForm: FormGroup;
  @Input() canEditAdminStatus: boolean;

  @ViewChild('canvasElement', {static: false}) canvas;

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
