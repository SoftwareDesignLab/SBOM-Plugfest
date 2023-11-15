function determineOS ()
{
    let os = navigator.userAgent;

    if (os.search('Windows')!==-1)
        return "Windows";

    else if (os.search('Mac')!==-1)
        return "Mac";
    
    else if (os.search('X11')!==-1 && !(os.search('Linux')!==-1))
        return "UNIX";
    
    else if (os.search('Linux')!==-1 && os.search('X11')!==-1)
        return "Linux";

    return "Unknown";
}

module.exports = { determineOS };