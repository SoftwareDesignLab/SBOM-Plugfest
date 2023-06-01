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

  get result() {
    this.qr = this.handler.metrics[this.handler.selectedQualityReport];
    return this.handler.metrics[this.handler.selectedQualityReport]?.results;
  }

  get identifiers() {
    return this.qr?.identifiers ? Array.from(this.qr.identifiers.values()) : [];
  }

  getTestMessage(result: any) {
    return `${result.field} ${result.message.toLowerCase()} ${
      result.t? info["STRING_VALUE"] : ""
    }`
  }

  getKeys(obj: Object) {
    return Object.keys(obj);
  }
}
