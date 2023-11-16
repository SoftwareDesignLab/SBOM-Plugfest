function determineOS() {
    if (typeof process !== 'undefined' && process.platform) {
        switch (process.platform) {
            case 'win32':
                return 'Windows';
            case 'darwin':
                return 'Mac';
            case 'linux':
                return 'Linux';
            default:
                return 'Unknown';
        }
    }

    // If not in a Node.js environment, return "Unknown"
    return 'Unknown';
}

module.exports = { determineOS };