import { Component } from "@angular/core";
import { DataHandlerService } from "@services/data-handler.service";
import { QualityReport, testResult } from "../test";

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
  processorFilter = "";

  get result() {
    this.qr = this.handler.metrics[this.handler.selectedQualityReport];
    if (this.processorFilter.length) {
      return this.handler.metrics[
        this.handler.selectedQualityReport
      ]?.results.filter((res) => res.processor === this.processorFilter);
    }
    return this.handler.metrics[this.handler.selectedQualityReport]?.results;
  }

  get processors() {
    return this.qr?.processors || [];
  }

  getIdentifierMessages(identifier: string) {
    if (!this.qr) {
      return [];
    }
    return Object.keys(this.qr?.mergedResults[identifier]);
  }

  getResults(identifier: string, message: string) {
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

  get identifiers() {
    return this.qr?.identifiers || [];
  }

  getTestMessage(result: any) {
    return ` ${result.stringValue || result.fieldName}`;
  }

  getColor(result: any) {
    return this.qr?.colors[this.qr?.processors.indexOf(result.processor)] || "";
  }

  getProcessorColor(name: string) {
    return this.qr?.colors[this.qr?.processors.indexOf(name)] || "";
  }

  getGrade(num: number) {
    switch (num) {
      case -1:
        return "Error";
      case 0:
        return "Failed";
      case 1:
        return "Passed";
      default:
        return "N/A";
    }
  }

  getKeys(obj: Object) {
    return Object.keys(obj);
  }
}
