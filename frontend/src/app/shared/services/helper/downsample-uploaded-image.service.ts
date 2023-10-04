import { Injectable } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DownsampleUploadedImageService {

  uploadImage(file: File): Observable<string> {
    const base64StringSubject: ReplaySubject<string> = new ReplaySubject<string>(1);

    if (file) {
      const reader: FileReader = new FileReader();
      reader.onload = e => {
        // Create an <img> element to load and process the image
        const imgElement: HTMLImageElement = document.createElement('img');
        imgElement.src = e.target.result as string;
        imgElement.onload = () => {
          //When the image is loaded, downsample it by a factor of 4 (0.25)
          base64StringSubject.next(this.downsampleImage(imgElement, 0.25));
        };
      };
      // Read the file as a data URL
      reader.readAsDataURL(file);
    }

    return base64StringSubject.asObservable();
  }

  private downsampleImage(originalImage: HTMLImageElement, scaleFactor: number): string {
    // Create a canvas element and get its 2D context
    const canvas: HTMLCanvasElement = document.createElement('canvas');
    const context: CanvasRenderingContext2D = canvas.getContext('2d');
    // Set the canvas dimensions based on the desired scale factor
    canvas.width = originalImage.width * scaleFactor;
    canvas.height = originalImage.height * scaleFactor;
    // Draw the original image onto the canvas, scaling it down
    context.drawImage(originalImage, 0, 0, canvas.width, canvas.height);
    // Convert the canvas content to a downsized data URL (JPEG format here)
    const base64String: string = canvas.toDataURL('image/jpeg');
    const tempArray: string[] = base64String.split(',');

    return tempArray[1];
  }
}
