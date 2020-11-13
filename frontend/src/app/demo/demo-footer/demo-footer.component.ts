import { Component, ElementRef, HostListener, ViewChild } from '@angular/core';

@Component({
  selector: 'app-demo-footer',
  templateUrl: './demo-footer.component.html',
  styleUrls: ['./demo-footer.component.scss']
})
export class DemoFooterComponent {
  @ViewChild('footer', {static: true}) footer: ElementRef;

  @HostListener('window:scroll')
  func(): void {
    this.footer.nativeElement.style.position = 'static';
    if (this.footer.nativeElement.offsetTop < document.documentElement.scrollTop + document.body.scrollTop
      + document.documentElement.offsetHeight - this.footer.nativeElement.offsetHeight) {
          this.footer.nativeElement.style.position = 'fixed';
    }
    // var foot = document.getElementById('footer');
    // foot.style.position = "static";
    // if( foot.offsetTop < document.documentElement.scrollTop + document.body.scrollTop + document.documentElement.offsetHeight - foot.offsetHeight)
    //     foot.style.position = "fixed";
    // })();
  }
}
