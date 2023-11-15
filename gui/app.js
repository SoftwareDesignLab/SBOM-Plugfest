const {app, BrowserWindow, ipcMain, dialog} = require('electron')
const url = require("url");
const path = require("path");
const fs = require("fs");
const spawn = require('child_process').spawn;   // for backend
const determineOS = require('./helpers');;

let mainWindow = undefined;
let process_id; // string to store process id from spring stdout

function createWindow () {
    mainWindow = new BrowserWindow({
        minWidth: 800,
        minHeight: 600,
        show: false,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false,
        }
    })

    mainWindow.maximize();
    mainWindow.show();

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

app.on('ready', async function() {

    let gradle = determineOS === "Mac" ? "./gradlew" : "gradlew";
    let execCmd = `cd .. && ` + gradle + ` run `; // Command to compile and run backend jar

    if(process.argv[0].endsWith("plugfest.exe"))
        execCmd = "java -jar backend.jar";

    // Launch backend
    console.log("Building Backend Jar");


    console.log('> ' + execCmd);
    const jarProcess = spawn(execCmd, { // Spawn a shell child process to run this command
        shell: true
    });

    // Add listener for stdout data
    jarProcess.stdout.on('data', (data) => {
        console.log(`stdout: ${data}`); // Print stdout data

        if (data.includes('with PID')) { // Spring prints a line with the PID: see below multiline comment

            let splitLine = data.toString().split(/[ ]+/); // Split line by spaces

            process_id = splitLine[splitLine.indexOf("INFO") + 1]; // Get PID from spring stdout

        }
    });

    // Print stderr data if any comes up
    jarProcess.stderr.on('data', (data) => {
        console.error(`stderr: ${data}`);
    });

    // If backend shuts down prematurely, print exit code
    jarProcess.on('close', (code) => {
        console.log(`Backend process exited with code ${code}`);
    });
    // todo launch window only after backend is ready?
    createWindow();
})

app.on('window-all-closed', function () {
    if(process_id) { // If spring is running, kill backend and log
        console.log("Backend process killed " + (process.kill(process_id, 'SIGINT') ? "successfully" : "unsuccessfully"));
    }
    if (process.platform !== 'darwin') app.quit()
})

app.on('activate', function () {
    if (mainWindow === null) createWindow()
})

ipcMain.handle("selectFiles", async () => {
  let files = await dialog.showOpenDialog(mainWindow, {
    properties: ["openFile","multiSelections"],
  });

  return files.filePaths;
});

ipcMain.handle("getFileData", async (event, ...args) => {
    let data = fs.readFileSync(args[0], 'utf8');
    return data;
})
