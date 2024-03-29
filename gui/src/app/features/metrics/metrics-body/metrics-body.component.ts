import { Component, OnChanges, SimpleChanges } from "@angular/core";
import { DataHandlerService } from "@services/data-handler.service";
import { QualityReport, grade, testResult } from "../test";
import { DownloaderService } from "@services/downloader.service";

@Component({
  selector: "app-metrics-body",
  templateUrl: "./metrics-body.component.html",
  styleUrls: ["./metrics-body.component.css"],
})
export class MetricsBodyComponent  {
  constructor(public handler: DataHandlerService, private downloadSerivce: DownloaderService) {}
  testResult: testResult | null = null;
  qr?: QualityReport | null = null;
  filteredArray: any[] = [];
  showPassed = false;
  componentView = false;
  processorIgnoreList: string[] = [];

  // Gets results for metrics tests
  get result() {
    this.qr = this.handler.GetMetrics(this.handler.selectedQualityReport);

    return this.qr?.results.filter((res) => !this.processorIgnoreList.includes(res.processor));
  }

  toggleProcessorFilter(name: string) {
    if(this.processorIgnoreList.includes(name))
      this.processorIgnoreList = this.processorIgnoreList.filter((x) => x !== name);
    else
      this.processorIgnoreList.push(name);
  }

  // Gets processor names
  get processors() {
    return this.qr?.processors || [];
  }

  // Gets messages from specified identifier
  getIdentifierMessages(identifier: string) {
    if (!this.qr) {
      return [];
    }
    return Object.keys(this.qr?.mergedResults[identifier]);
  }

    // gets list of identifiers
  get identifiers() {
    return this.qr?.identifiers || [];
  }

  // Gets formatted metrics results
  // HOTFIX!!!!!!! REMOVE AFTER BACKEND IS UPDATED
  getMergedResult(identifier: string, message: string) {
    let uniqueVals: any[] = [];
    if (this.qr?.mergedResults[identifier][message]) {
      return this.qr?.mergedResults[identifier][message].sort((a, b) => a.identifier - b.identifier).filter((result) => {
        const res = JSON.stringify(result);
        if (uniqueVals.indexOf(res) === -1) {
          uniqueVals.push(res);
          return true;
        }
        return false;
      });
    }
    return [];
  }

  // Prints result message for drop down
  getTestMessage(result: any) {
    let message = '';
    message += result.stringValue ? result.stringValue : '';
    message += (result.fieldName && result.stringValue) ? ':' : '';
    message += result.fieldName ? result.fieldName : '';
    return message;
  }

  // Gets color associated with test result
  getResultColor(result: any) {
    return this.qr?.colors[this.qr?.processors.indexOf(result.processor)] || "";
  }

  // Gets color for processor
  getLabelColor(name: string) {
    return this.qr?.colors[this.qr?.processors.indexOf(name)] || "";
  }

  // Gets if test passed/failed/errored
  getGrade(num: number) {
    switch (num) {
      case grade.error:
        return "Error";
      case grade.fail:
        return "Failed";
      case grade.pass:
        return "Passed";
      default:
        return "N/A";
    }
  }

  getShape(processor: string) {
      if (this.qr) {
      const index = this.qr?.processors.indexOf(processor);
      return this.qr?.shapes[index];
    }
    return null;
  }

  getPassedAmount() {
    let passed = 0;
    let total = 0;

    if(!this.qr || this.handler.selectedQualityReport === undefined)
      return "";

    for(let identifier of this.identifiers) {
      for(let message of this.getIdentifierMessages(identifier)) {
        for(let result of this.getMergedResult(identifier, message)) {
          total++;
          
          if(result.pass === 1)
            passed++;
        }
      }
    } 

    return passed.toString() + "/" + total.toString() + " Passed";
  }

  download() {
    this.downloadSerivce.download(this.qr, "metrics.json");
  }
}
