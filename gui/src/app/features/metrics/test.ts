export interface test {
  uid: string;
  attributeResults: {
    [key: string]: { [key: string]: testResult[] };
  };
}

export interface testResult {
  pass: number;
  message: string;
  additionalInfo: AdditionalInfo;
}

interface AdditionalInfo {
  [key: string]: string;
}

export class QualityReport {
  test: test;
  processors: string[] = [];
  results: any[] = [];
  identifiers: string[] = [];
  componentResults: { [key: string]: testResult[] } = {};
  colors = [
    "var(--red)",
    "var(--purple)",
    "var(--yellow)",
    "var(--blue)",
    "var(--green)",
    "var(--orange)",
    "var(--pink)",
  ];

  constructor(test: test) {
    this.test = test;
    this.processors = Object.keys(this.test.attributeResults);
    const ids = new Set<string>();
    this.processors.forEach((processor) => {
      const tests = Object.keys(this.test.attributeResults[processor]);
      tests.forEach((test) => {
        this.test.attributeResults[processor][test].forEach((result) => {
          if (result.additionalInfo["IDENTIFIER"]) {
            ids.add(result.additionalInfo["IDENTIFIER"]);
          }
          this.results.push({
            result,
            processor,
            test,
            message: result.message,
            pass: result.pass,
            type: result.additionalInfo["TYPE"] || "",
            fieldName: result.additionalInfo["FIELD_NAME"],
            identifier: result.additionalInfo["IDENTIFIER"] || "",
          });
        });
      });
    });
    this.identifiers = Array.from(ids);
  }

  /**
    filters results by component
    @param identifier - component identifier
  */
  getResultsByComponent(identifier: string): any[] {
    return this.results.filter((result) => result.identifier === identifier);
  }
}
