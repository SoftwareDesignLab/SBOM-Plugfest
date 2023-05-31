import { identity } from "rxjs";

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

export class QuailtyReport {
  test: test;
  categories: string[] = [];
  results: testResult[] = [];
  identifiers: Set<string> = new Set<string>();
  componentResults: { [key: string]: testResult[] } = {};

  constructor(test: test) {
    this.test = test;
    this.categories = Object.keys(this.test.attributeResults);
    this.categories.forEach(category => {
      const tests = Object.keys(this.test.attributeResults[category])
      tests.forEach(test => {
        this.results = this.test.attributeResults[category][test];
        // this.identifiers.add(this.test.attributeResults?.['IDENTIFIER']);
      });

    });
  }

  getComponentResults() {
    if (Object.keys(this.componentResults).length === 0) {
      this.categories.forEach(category => {

      });
    }
  }

  getAllIdentifiers() {

  }

  /**
    filters results by component
    @param identifier - component identifier
  */
  getResultsByComponent(identifier: string): testResult[] {

    return this.results.filter(element => element.additionalInfo?.["IDENTIFIER"] === identifier);
  }
}

