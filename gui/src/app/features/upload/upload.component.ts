import { Component, ViewChild, ElementRef } from '@angular/core';
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

  constructor(private dataHandler: DataHandlerService) {
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

  ContainsFiles() {
    return Object.keys(this.dataHandler.metrics).length > 0 || this.GetLoadingFiles().length > 0;
  }

  GetFiles() {
    return this.dataHandler.metrics;
  }

  GetLoadingFiles() {
    return this.dataHandler.loadingFiles;
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
}
