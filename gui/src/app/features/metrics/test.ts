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
  categories: string[] = [];
  results: any[] = [];
  identifiers: Set<string> = new Set<string>();
  componentResults: { [key: string]: testResult[] } = {};

  constructor(test: test) {
    this.test = test;
    this.categories = Object.keys(this.test.attributeResults);
    this.categories.forEach((category) => {
      const tests = Object.keys(this.test.attributeResults[category]);
      tests.forEach((test) => {
        this.test.attributeResults[category][test].forEach((result) => {
          this.identifiers.add(result.additionalInfo["IDENTIFIER"] || "");
          this.results.push({
            result,
            category,
            test,
            message: result.message,
            pass: result.pass,
            type: result.additionalInfo["TYPE"] || "",
            fieldName: result.additionalInfo['FIELD_NAME'],
            identifier: result.additionalInfo["IDENTIFIER"] || "",
          });
        });
      });
    });
  }

  /**
    filters results by component
    @param identifier - component identifier
  */
  getResultsByComponent(identifier: string): testResult[] {
    return this.results.filter((result) => result.identifier === identifier);
  }

  getResultsbyType(type: string) {
    return this.results.filter((result)=> result.type === type);
  }

  getResultByGrade(num: -1 | 1 | 0) {
    return this.results.filter(result => result.pass === num);
  }

  getResultsForCategory(category: string) {
    return this.results.filter(result => result.category === category)
  }
}
