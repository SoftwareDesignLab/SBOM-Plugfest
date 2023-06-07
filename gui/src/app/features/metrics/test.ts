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
    "var(--blue)",
    "var(--green)",
    "var(--teal)",
    "var(--orange)",
    "var(--pink)",
    "var(--yellow)",
  ];

  mergedResults: { [key: string]: { [key: string]: any[] } } = {};

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
          const formattedRes = {
            result,
            processor,
            test,
            message: result.message,
            pass: result.pass,
            type: result.additionalInfo["TYPE"] || "",
            fieldName: result.additionalInfo["FIELD_NAME"],
            identifier: result.additionalInfo["IDENTIFIER"] || "",
            stringValue: result.additionalInfo['STRING_VALUE'] || '',
          };
          this.results.push(formattedRes);

          if (formattedRes.identifier && formattedRes.result.message) {
            if (!this.mergedResults[formattedRes?.identifier]) {
                this.mergedResults[formattedRes.identifier] = { [formattedRes.message]: [formattedRes]}
            } else {
                if (this.mergedResults[formattedRes?.identifier][formattedRes.message]) {
                    this.mergedResults[formattedRes?.identifier][formattedRes.message].push(formattedRes);
                } else {
                    this.mergedResults[formattedRes?.identifier][formattedRes.message] = [formattedRes];
                }
            }
          }
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
