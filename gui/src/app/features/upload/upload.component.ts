import { Component, ViewChild, ElementRef, Input } from '@angular/core';
import { ClientService } from '@services/client.service';
import { DataHandlerService } from '@services/data-handler.service';
import { IpcRenderer } from 'electron';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent {
  private ipc!: IpcRenderer;
  isLoading = false;
  @ViewChild('container') container!: ElementRef;
  @Input() stepper: any;
  errorResponse: any;

  constructor(private dataHandler: DataHandlerService, private clientService: ClientService) {
    if (window.require) {
      try {
        this.ipc = window.require('electron').ipcRenderer;
      } catch (e) {
        console.log(e);
      }
    } else {
      console.warn('App not running inside Electron!');
    }
  }

  browse() {
    this.ipc.invoke('selectFiles').then((files: string[]) => {
      if(files === undefined || files === null || files.length === 0) {
        this.scrollToEnd();
        return;

      }

      this.dataHandler.AddFiles(files);
    });
  }

  parse(contents: string, fileName: string) {
    this.clientService.post("parse", {'fileName': fileName, 'contents': contents }).subscribe(
      response => {
        console.log('Error response:', response);
      },
      error => {
        this.errorResponse = error.message;
      }
    );
  }

  getSBOMAlias(path: string) {
    return this.dataHandler.getSBOMAlias(path);
  }

  ContainsFiles() {
    return Object.keys(this.dataHandler.metrics).length > 0 || this.GetLoadingFiles().length > 0;
  }

  GetFiles() {
    return this.dataHandler.metrics;
  }

  GetLoadingFiles() {
    return this.dataHandler.loadingFiles;
  }

  GetValidSBOMs() {
    return this.dataHandler.GetValidSBOMs();
  }

  SelectAll() {
    this.setAllSelected(true);
  }

  DeselectAll() {
    this.setAllSelected(false);
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
    this.dataHandler.filePaths = this.dataHandler.filePaths.filter((x) => x != file);
    delete this.dataHandler.metrics[file];
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
