import { Injectable } from '@angular/core';
import * as FileSaver from 'file-saver';
import { Subscription } from 'rxjs';
import { ExcelFileService } from './excel-file.service';
import { DepartmentId } from '../../shared/model/id-types';

@Injectable({
  providedIn: 'root'
})
export class ExcelMapper {

  private readonly blobType: string;

  constructor(private excelFileService: ExcelFileService) {
    this.blobType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
  }

  downloadExcelFileForOkrTeam(departmentId: DepartmentId): Subscription {
    return this.excelFileService.downloadExcelFileForOkrTeam$(departmentId)
      .subscribe(data => {
        const blob: Blob = new Blob([data], {type: this.blobType});
        FileSaver.saveAs(blob, 'okr.xlsx');
      });
  }

  downloadExcelFileForCompany(companyId: number): Subscription {
    return this.excelFileService.downloadExcelFileCompany$(companyId)
      .subscribe(data => {
        const blob: Blob = new Blob([data], {type: this.blobType});
        FileSaver.saveAs(blob, 'okr.xlsx');
      });
  }

  downloadExcelEmailFileForOkrTeam(departmentId: DepartmentId): Subscription {
    return this.excelFileService.downloadExcelEmailFileForOkrTeam$(departmentId)
      .subscribe(data => {
        const blob: Blob = new Blob([data], {type: this.blobType});
        FileSaver.saveAs(blob, 'okr-member.xlsx');
      });
  }

  downloadExcelEmailFileForCompany(companyId: number): Subscription {
    return this.excelFileService.downloadExcelEmailFileForCompany$(companyId)
      .subscribe(data => {
        const blob: Blob = new Blob([data], {type: this.blobType});
        FileSaver.saveAs(blob, 'okr-member.xlsx');
      });
  }
}
