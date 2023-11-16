const { exec } = require('child_process');
const { determineOS } = require('./helpers');

let gradle = determineOS() === "Mac" ? "./gradlew" : "gradlew";

const command = 'cd ../ && ' + gradle + ' build bootJar -x test';

exec(command, (err, stdout, stderr) => {
    if (err) {
        console.error(err);
        return;
    }

    console.log("Built backend jar successfully");
});