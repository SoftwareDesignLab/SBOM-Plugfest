import { Injectable } from '@angular/core';
import { ClientService } from './client.service';
import { HttpParams } from '@angular/common/http';
import { IpcRenderer } from 'electron';
import { Comparison } from '@features/comparison/comparison';

@Injectable({
  providedIn: 'root'
})
export class DataHandlerService {

  private ipc!: IpcRenderer;
  private filePaths: string[] = [];

  public metrics: { [id: string]: Object } = {};
  public comparison!: Comparison; 

  //TODO: Cleanup (yeah this shouldn't be here but midnight + demo + ratio)
  public selectedQualityReport!: string;

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

  AddFiles(paths: string[]) {
    this.filePaths.push(...paths);

    paths.forEach((path) => {
      this.RunMetricsOnFile(path);
    })
  }

  RunAllMetrics() {
    this.filePaths.forEach((file) => {
      this.RunMetricsOnFile(file);
    })
  }

  RunMetricsOnFile(path: string) {
    this.ipc.invoke('getFileData', path).then((data: any) => {
      this.client.post("qa", new HttpParams().set("contents",data).set("fileName", path)).subscribe((result) => {
        this.metrics[path] = result;
      })
    });
  }

  GetValidSBOMs() {
    return Object.keys(this.metrics).filter((x) => this.metrics[x] !== null);
  }

  Compare(main: string, others: string[]) {
    let toSend: { [path: string]: string } = {};
    let total = others.length + 1;
    let i = 0;

    Object.keys(this.metrics).filter(x => x === main || others.includes(x)).forEach((path) => {
      this.ipc.invoke('getFileData', path).then((data: any) => {
        toSend[path] = data;
        i++;

        //last time running
        if(i == total) {
          let fileData: string[] = [];
          let filePaths: string[] = [];

          //Ensure that the compare is first in list
          Object.keys(toSend).forEach((path) => {
            if(path === main) {
              fileData.unshift(toSend[path]);
              filePaths.unshift(path);
            } else {
              fileData.push(toSend[path]);
              filePaths.push(path);
            }
          })

          this.client.post("compare", new HttpParams().set('contents', JSON.stringify(fileData)).set('fileNames', JSON.stringify(filePaths))).subscribe((result: any) => {
            this.comparison = result;
          })
        } 
      })
    })
  }
}
