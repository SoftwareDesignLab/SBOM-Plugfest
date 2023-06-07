import { Component } from "@angular/core";
import { DataHandlerService } from "@services/data-handler.service";
import { QualityReport, grade, testResult } from "../test";

@Component({
  selector: "app-metrics-body",
  templateUrl: "./metrics-body.component.html",
  styleUrls: ["./metrics-body.component.css"],
})
export class MetricsBodyComponent {
  constructor(private handler: DataHandlerService) {}
  testResult: testResult | null = null;
  qr: QualityReport | null = null;
  filteredArray: any[] = [];
  showPassed = false;
  componentView = false;
  processorIgnoreList: string[] = [];

  // Gets results for metrics tests
  get result() {
    this.qr = this.handler.metrics[this.handler.selectedQualityReport];

    return this.handler.metrics[
      this.handler.selectedQualityReport
    ]?.results.filter((res) => !this.processorIgnoreList.includes(res.processor));

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

  // Gets formatted metrics results
  // HOTFIX!!!!!!! REMOVE AFTER BACKEND IS UPDATED
  getMergedResult(identifier: string, message: string) {
    let uniqueVals: any[] = [];
    if (this.qr?.mergedResults[identifier][message]) {
      return this.qr?.mergedResults[identifier][message].filter((result) => {
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

  // gets list of identifiers
  get identifiers() {
    return this.qr?.identifiers || [];
  }

  // Prints result message for drop down
  getTestMessage(result: any) {
    return ` ${result.stringValue || result.fieldName}`;
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
}
