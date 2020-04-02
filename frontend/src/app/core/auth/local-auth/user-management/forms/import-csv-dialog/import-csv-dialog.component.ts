import { Component, OnInit, ViewChild } from '@angular/core';
import { Papa } from 'ngx-papaparse';
import { MatDialog, MatDialogRef, MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
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

interface ImportCsvDialogForm {
  csvFile: FileInput;
}

@Component({
  selector: 'app-import-csv-dialog',
  templateUrl: './import-csv-dialog.component.html',
  styleUrls: ['./import-csv-dialog.component.css']
})
export class ImportCsvDialogComponent implements OnInit {

  fileForm: FormGroupTyped<ImportCsvDialogForm>;
  rowData = new MatTableDataSource([] as User[]);
  columnsToDisplay = ['givenName', 'email', 'department', 'jobTitle'];
  warnings: { tooManyFields: boolean };
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor(
    private dialogRef: MatDialogRef<ImportCsvDialogComponent>,
    private papa: Papa,
    private csvService: CsvUserParseService,
    private dialog: MatDialog,
    private formbuilder: FormBuilder
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
      reader.onload = e => {
        const csvContent: string | ArrayBuffer = reader.result.toString();
        const records: CsvParseResult = this.csvService.parseCsvStringToUserArray(csvContent);
        this.rowData.data = records.users;
        this.warnings = records.warnings;
      };
    }
  }

  onSave(): void {
    const data: ConfirmationDialogData = {
      title: `${this.rowData.data.length} Benutzer erstellen.`,
      message: `Diese Operation wird alle ${this.rowData.data.length} Nutzer hinzufügen. Möchten sie dies wirklich tun?`,
      confirmButtonText: 'Bestätigen'
    };
    this.dialog.open(ConfirmationDialogComponent, {data})
      .afterClosed()
      .pipe(
        filter(v => v)
      )
      .subscribe(_ => this.dialogRef.close(this.rowData.data));
  }
}
