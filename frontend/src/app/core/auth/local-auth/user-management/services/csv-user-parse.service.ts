import { Injectable } from '@angular/core';
import { Papa, ParseResult } from 'ngx-papaparse';
import { User } from '../../../../../shared/model/api/user';

export interface CsvParseWarnings {
  tooManyFields: boolean;
}

export interface CsvParseResult {
  warnings: CsvParseWarnings;
  users: User[];
}

@Injectable({
  providedIn: 'root'
})
export class CsvUserParseService {

  constructor(private papa: Papa) {
  }

  parseCsvStringToUserArray(csvString: string): CsvParseResult {
    const parsedData: ParseResult = this.papa.parse(csvString, {skipEmptyLines: true});
    const warnings: CsvParseWarnings = this.validateParseResult(parsedData);

    // FORMAT: VORNAME, NACHNAME, EMAIL, JOBBEZEICHNUNG, ABTEILUNG
    const users: User[] = [];
    for (const userArray of parsedData.data as [string[]]) {
      const user: User = {
        givenName: userArray[0],
        surname: userArray[1],
        email: userArray[2],
        jobTitle: userArray[3],
        department: userArray[4],
        active: true,
        id: null,
        photo: null
      };
      users.push(user);
    }

    return {users, warnings};
  }

  private validateParseResult(parsedData: ParseResult): CsvParseWarnings {
    if (parsedData.data.length === 0) {
      console.warn('The CSV file is empty');

      return;
    }
    const parsedArray: [string[]] = parsedData.data;

    let result: CsvParseWarnings;

    parsedArray.forEach(array => {
      if (array.length > 5) {
        if (!result) {
          result = {tooManyFields: true};
        }
        result.tooManyFields = true;
      }
    });

    return result;
  }
}
