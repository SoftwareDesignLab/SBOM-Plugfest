/** @Author Justin Jantzi */
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DownloaderService {
    download(object: any, filename: string) {
        var dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(object));
        var downloadAnchorNode = document.createElement('a');
        downloadAnchorNode.setAttribute("href",dataStr);
        downloadAnchorNode.setAttribute("download", filename);
        document.body.appendChild(downloadAnchorNode); // required for firefox
        downloadAnchorNode.click();
        downloadAnchorNode.remove();
      }
}