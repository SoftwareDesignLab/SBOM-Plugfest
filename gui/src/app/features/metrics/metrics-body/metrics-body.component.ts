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

  //  STRING_VALUE: "Pivotal Software, Inc.",
  //   IDENTIFIER: "spring-boot-autoconfigure",
  //   FIELD_NAME: "publisher",
  //   TYPE: "Component",

  get result() {
    this.qr = this.handler.metrics[this.handler.selectedQualityReport];
    return this.handler.metrics[this.handler.selectedQualityReport]?.results;
  }

  get identifiers() {
    return this.qr?.identifiers ? Array.from(this.qr.identifiers.values()) : [];
  }

  getTestMessage(result: testResult) {
    const info = result.additionalInfo;
    return `${info["FIELD_NAME"]}: ${result.message.toLowerCase()} ${
      info["STRING_VALUE"] ? info["STRING_VALUE"] : ""
    }`;
  }

  getPassedPercent(testResult: any) {
    return 100;
    return (
      (testResult.successfulTests / testResult.tests.length) *
      100
    ).toFixed(2);
  }

  getKeys(obj: Object) {
    return Object.keys(obj);
  }
}
