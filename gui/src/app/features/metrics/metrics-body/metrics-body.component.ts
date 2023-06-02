import { Component, Input } from "@angular/core";
import { test, testResult, QualityReport } from "../test";
import { DataHandlerService } from "@services/data-handler.service";

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
  testFilter = "";

  get result() {
    this.qr = this.handler.metrics[this.handler.selectedQualityReport];
    if (this.testFilter.length) {
      return this.handler.metrics[this.handler.selectedQualityReport]?.results.filter(res => res.processor === this.testFilter);
    }
    return this.handler.metrics[this.handler.selectedQualityReport]?.results;
  }

  get processors() {
    return this.qr?.processors || [];
  }

  get identifiers() {
    return this.qr?.identifiers || [];
  }

  getTestMessage(result: any) {
    return `${result.fieldName}: ${result.message.toLowerCase()}`;
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
}
