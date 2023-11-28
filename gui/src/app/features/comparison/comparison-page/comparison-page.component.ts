/** @Author Tina DiLorenzo, Justin Jantzi */

import { Component, EventEmitter, Inject, Input, Output } from "@angular/core";
import { Comparison } from "../comparison";
import { SBOM } from "@models/sbom";

import { DataHandlerService, FileStatus } from "@services/data-handler.service";
import { DownloaderService } from "@services/downloader.service";

@Component({
  selector: "app-comparison-page",
  templateUrl: "./comparison-page.component.html",
  styleUrls: ["./comparison-page.component.css"],
})
export class ComparisonPageComponent {
  collapsed: boolean = false;

  sbomInfoOpened: string | null = null;

  sboms: string[] = ["a", "b"];
  targetSbom!: string;

  constructor(private dataHandler: DataHandlerService, private downloadService: DownloaderService) {}

  /** @TODO create an api call where you would send the target sbom and compare */
  // it against all sboms rather than doing singular api calls for each one  */
  selectTargetSbom($event: any) {
    this.targetSbom = $event;
  }

  // Display diff report
  compare() {

    if(!this.targetSbom)
      return;

    if(this.IsLoadingComparison())
      return;

    // Get all the checkboxes in the DOM
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');

    // Loop through each checkbox and check if it's selected and not disabled
    const selectedCheckboxes = [];
    for (let i = 0; i < checkboxes.length; i++) {
      const checkbox = checkboxes[i] as HTMLInputElement;
      if (checkbox.checked && !checkbox.disabled) {
        selectedCheckboxes.push(checkbox.value);
      }
    }

    if(selectedCheckboxes.length === 0) 
      return;

    this.dataHandler.Compare(this.targetSbom, selectedCheckboxes);
  }

  setAllSelected(value: boolean) {
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');

    for (let i = 0; i < checkboxes.length; i++) {
      const checkbox = checkboxes[i] as HTMLInputElement;
      checkbox.checked = value;
    }
  }

  GetValidSBOMs() {
    return this.dataHandler.GetSBOMsOfType(FileStatus.VALID);
  }

  GetDropdownSBOMs() {
    let keys = this.GetValidSBOMs();
    let data: { [id: string]: Object | null } = {};

    for(let i = 0; i < keys.length; i++) {
      let key = keys[i];
      let value = this.getSBOMAlias(key) as string;

      data[key] = value;
    }

    return data;
  }

  getSBOMAlias(path: string) {
    return this.dataHandler.getSBOMAlias(path);
  }

  GetComparison() {
    return this.dataHandler.comparison;
  }

  getSBOMInfo(path: string) {
    this.sbomInfoOpened = path;
  }

  IsLoadingComparison(): boolean {
    return this.dataHandler.IsLoadingComparison();
  }

  toggleCheckbox(index: number) {
    const checkbox = document.getElementsByName('comparison')[index] as HTMLInputElement;
    checkbox.checked = !checkbox.checked;
  }

  download() {
    this.downloadService.download(this.dataHandler.comparison, "comparison.json");
  }
}
