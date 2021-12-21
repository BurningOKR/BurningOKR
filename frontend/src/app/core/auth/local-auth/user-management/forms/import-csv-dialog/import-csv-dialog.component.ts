import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { User } from '../../../../../../shared/model/api/user';
import { CsvParseResult, CsvUserParseService } from '../../services/csv-user-parse.service';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData
} from '../../../../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { filter } from 'rxjs/operators';
import { FormBuilder } from '@angular/forms';
import { FileInput } from 'ngx-material-file-input';
import { FormGroupTyped } from '../../../../../../../typings';
import { TranslateService } from '@ngx-translate/core';

interface ImportCsvDialogForm {
  csvFile: FileInput;
}

interface ImportCsvWarnings {
  tooManyFields: boolean;
  duplicateEmailAdresses: boolean;
}

@Component({
  selector: 'app-import-csv-dialog',
  templateUrl: './import-csv-dialog.component.html',
  styleUrls: ['./import-csv-dialog.component.css']
})
export class ImportCsvDialogComponent implements OnInit {

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  fileForm: FormGroupTyped<ImportCsvDialogForm>;
  rowData = new MatTableDataSource([] as User[]);
  columnsToDisplay = ['givenName', 'email', 'department', 'jobTitle'];
  warnings: ImportCsvWarnings;

  constructor(
    private dialogRef: MatDialogRef<ImportCsvDialogComponent>,
    private csvService: CsvUserParseService,
    private dialog: MatDialog,
    private formbuilder: FormBuilder,
    private translate: TranslateService
  ) {
    this.fileForm = this.formbuilder.group({
      csvFile: []
    }) as FormGroupTyped<ImportCsvDialogForm>;
  }

  ngOnInit(): void {
    this.rowData.sort = this.sort;
    this.rowData.paginator = this.paginator;
    this.fileForm.controls.csvFile.valueChanges.subscribe(newFile => {
      this.setCsv(newFile);
    });
  }

  setCsv(fileInput: FileInput): void {
    const files: File[] = fileInput.files;
    if (files.length) {
      const file: File = files[0];
      const reader: FileReader = new FileReader();
      reader.readAsText(file);
      reader.onload = () => {
        const csvContent: string | ArrayBuffer = reader.result.toString();
        const records: CsvParseResult = this.csvService.parseCsvStringToUserArray(csvContent);
        this.rowData.data = records.users;
        this.warnings = {
          tooManyFields: records.warnings ? records.warnings.tooManyFields : false,
          duplicateEmailAdresses: this.hasDuplicateEmailAdresses(records.users)
        };
      };
    }
  }

  onSave(): void {

    const confirmationTitleI18n: string = this.translate.instant('import-csv-dialog.confirmation.title',
      { list_length: this.rowData.data.length });
    const confirmationTextI18n: string = this.translate.instant('import-csv-dialog.confirmation.text',
      { list_length: this.rowData.data.length });

    const data: ConfirmationDialogData = {
      title: confirmationTitleI18n,
      message: confirmationTextI18n,
    };
    this.dialog.open(ConfirmationDialogComponent, { data })
      .afterClosed()
      .pipe(
        filter(v => v)
      )
      .subscribe(_ => this.dialogRef.close(this.rowData.data));
  }

  private hasDuplicateEmailAdresses(users: User[]): boolean {
    return users.Any(user => users.Any(anotherUser => user !== anotherUser && user.email === anotherUser.email));
  }
}
