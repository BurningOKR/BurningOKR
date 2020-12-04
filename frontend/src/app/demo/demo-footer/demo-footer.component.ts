import { AfterViewInit, Component, ElementRef, HostListener, ViewChild } from '@angular/core';

@Component({
  selector: 'app-demo-footer',
  templateUrl: './demo-footer.component.html',
  styleUrls: ['./demo-footer.component.scss']
})
export class DemoFooterComponent implements AfterViewInit {
  @ViewChild('footer', {static: true}) footer: ElementRef;

  // @HostListener('window:scroll')
  // repositionFooter(): void {
  //   this.footer.nativeElement.style.position = 'static';
  //   if (this.footer.nativeElement.offsetTop < document.documentElement.scrollTop + document.body.scrollTop
  //     + document.documentElement.offsetHeight - this.footer.nativeElement.offsetHeight) {
  //         this.footer.nativeElement.style.position = 'fixed';
  //   }
  // }

  ngAfterViewInit(): void {
    //this.repositionFooter();
    console.log('logging');
  }
}
