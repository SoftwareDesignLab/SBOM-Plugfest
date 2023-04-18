/* Author: Kevin Laporte, Justin Jantzi */

import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { ClientService } from '@services/client.service';

@Injectable({
  providedIn: 'root'
})

export class ComparisonService {

   constructor(
    private client: ClientService
   ) {}

   /**
    * Sends an API call to the backend for the Comparison Report
    *
    * @returns list of files to compare
    */
   getComparisonReport(files: any) {
    return this.client.post("compare", new HttpParams().set("boms", files));
   }
}
