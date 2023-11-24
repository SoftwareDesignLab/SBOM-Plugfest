import { Component, ViewChild, ElementRef, Input } from '@angular/core';
import { ClientService } from '@services/client.service';
import { DataHandlerService, FileStatus } from '@services/data-handler.service';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent {
  isLoading = false;
  @ViewChild('container') container!: ElementRef;
  @ViewChild('fileInput') fileInputRef!: ElementRef<HTMLInputElement>;
  @Input() stepper: any;
  errorResponse: any;

  constructor(private dataHandler: DataHandlerService) {}

  onFileSelect(files: FileList | null) {
    if (!files || files.length === 0) {
      this.scrollToEnd();
      return;
    }

    this.dataHandler.AddFiles(files);
  }

  browse() {
    this.fileInputRef.nativeElement.click();
  }

  getSBOMAlias(path: string) {
    return this.dataHandler.getSBOMAlias(path);
  }

  ContainsFiles() {
    return this.GetLoadingFiles().length > 0 || this.GetLoadedFiles() > 0;
  }
  

  GetLoadingFiles() {
    return this.dataHandler.GetSBOMsOfType(FileStatus.LOADING);
  }

  GetValidSBOMs() {
    return this.dataHandler.GetSBOMsOfType(FileStatus.VALID);
  }
  
  GetErrorSBOMs() {
    return this.dataHandler.GetSBOMsOfType(FileStatus.ERROR);
  }

  GetLoadedFiles() {
    return this.GetValidSBOMs().push(...this.GetErrorSBOMs());
  }

  GetLoadedFileKeys() {
    return Object.keys(this.GetLoadedFiles());
  }

  SelectAll() {
    this.setAllSelected(true);
  }

  DeselectAll() {
    this.setAllSelected(false);
  }

  GetSbomInfo(path: string) {
    return this.dataHandler.GetSBOMInfo(path);
  }

  DeleteSelected() {
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');

    for (let i = 0; i < checkboxes.length; i++) {
      const checkbox = checkboxes[i] as HTMLInputElement;
      if (checkbox.checked && !checkbox.disabled) {
        this.RemoveFile(checkbox.value);
      }
    }
  }

  setAllSelected(value: boolean) {
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');

    for (let i = 0; i < checkboxes.length; i++) {
      const checkbox = checkboxes[i] as HTMLInputElement;
      checkbox.checked = value;
    }
  }

  RemoveFile(file: string) {
    this.dataHandler.DeleteFile(file);
  }

  private scrollToEnd() {
    setTimeout(() => {
      this.container.nativeElement.scrollTop = this.container.nativeElement.scrollHeight;
    }, 0);
  }

  NextStepper() {
    this.stepper.next();
  }
}
