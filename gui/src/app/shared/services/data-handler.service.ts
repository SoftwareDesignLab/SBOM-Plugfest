import { Injectable } from '@angular/core';
import { ClientService } from './client.service';
import { HttpParams } from '@angular/common/http';
import { IpcRenderer } from 'electron';

@Injectable({
  providedIn: 'root'
})
export class DataHandlerService {

  private ipc!: IpcRenderer;
  private filePaths: string[] = [];
  private metrics: { [id: string]: Object } = {};

  constructor(private client: ClientService) { 
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

  RunMetrics() {
      this.filePaths.forEach((file) => {
        this.ipc.invoke('getFileData', file).then((data) => {
          this.client.post("qa", new HttpParams().set("bom", data)).subscribe((result) => {
            this.metrics[file] = result;
          })
        });
      })
  }
}
