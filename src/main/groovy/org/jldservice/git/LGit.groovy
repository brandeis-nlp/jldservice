package org.jldservice.git;

import org.eclipse.jgit.api.Git;

/**
 * Created by shicq on 3/7/14.
 */


class LGit {

    static def localPath = "git_local";


    static def clone (remotePath){
        def fil = new File(localPath);
        if (!fil.exists()) {
            fil.mkdirs();
        }
        Git.cloneRepository()
                .setURI(remotePath)
                .setDirectory(new File(localPath))
                .call();
    }

}