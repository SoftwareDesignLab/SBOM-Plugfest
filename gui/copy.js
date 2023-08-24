const fs = require('fs');
const path = require('path');

const sourceDir = path.join('..', 'api', 'build', 'libs');
const destination = path.join('builds', 'plugfest-win32-x64', 'backend.jar');

fs.readdir(sourceDir, (err, files) => {
  if (err) {
    console.error('Error reading source directory:', err);
    return;
  }

  const jarFile = files.find(file => file.endsWith('.jar') && !file.includes('-plain'));

  if (jarFile) {
    const source = path.join(sourceDir, jarFile);

    fs.copyFile(source, destination, (copyErr) => {
      if (copyErr) {
        console.error('Error copying file:', copyErr);
      } else {
        console.log('File copied successfully:', jarFile);
      }
    });
  } else {
    console.error('No suitable JAR file found in the source directory.');
  }
});