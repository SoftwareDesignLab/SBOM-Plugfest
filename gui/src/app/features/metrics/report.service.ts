/* Author: Kevin Laporte, Justin Jantzi */

import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { ClientService } from '@services/client.service';

@Injectable({
  providedIn: 'root'
})

export class ReportService {

   constructor(
    private client: ClientService
   ) {}

   /**
    * Sends an API call to the backend for the Quality Report
    *
    * @returns list of SBOM objects created from the files
    */
   getQualityReport(files: any) {
    return this.client.post("qa", new HttpParams().set("boms", files));
   }
}
