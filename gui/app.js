const {app, BrowserWindow} = require('electron')
const url = require("url");
const path = require("path");

let mainWindow = undefined;
let filePath = undefined;

function createWindow () {
    mainWindow = new BrowserWindow({
        width: 800,
        height: 600,
        webPreferences: {
            nodeIntegration: true
        }
    })

    mainWindow.loadURL(
        url.format({
            pathname: path.join(__dirname, 'dist/app/index.html'),
            protocol: 'file:',
            slashes: true
        })
    );

    mainWindow.on('closed', function () {
        mainWindow = null
    })
}

app.on('ready', function() {

    // let arg1 = app.commandLine.getSwitchValue("path");

    // if(arg1 === undefined || arg1 === '') {
    //     console.error("Error: Aborting... 'path' argument for directory of SBOMs not set!");
    //     app.quit();
    //     return;
    // }

    // filePath = arg1;
    
    //TODO: Launch backend with file path arg

    createWindow();
})

app.on('window-all-closed', function () {
    if (process.platform !== 'darwin') app.quit()
})

app.on('activate', function () {
    if (mainWindow === null) createWindow()
})