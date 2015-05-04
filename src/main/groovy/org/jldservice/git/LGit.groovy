package org.jldservice.git

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.jldservice.config.Config

/**
 * Created by shicq on 3/7/14.
 */

//
// https://code.google.com/p/egit/wiki/JGitTutorialRepository
// http://stackoverflow.com/questions/6861881/jgit-cannot-find-a-tutorial-or-simple-example
// https://github.com/centic9/jgit-cookbook
//

class LGit {

//    static def localPath = "git_local";
    static def localPath = Config.getDef("git.local");

    static def git_clone (remotePath){
        def fil = new File(localPath);
        if (!fil.exists()) {
            fil.mkdirs();
        }
        def localRep = get_local_dir(remotePath);
        // before git clone, remove local directory.
        if (!localRep.exists()) {
            Git.cloneRepository()
                    .setURI(remotePath)
                    .setDirectory(localRep)
                    .call();
        }
        return localRep.absolutePath;
    }


    static def get_local_dir (remotePath) {
        def localRepo = new File(localPath, remotePath.replaceAll(".*/","").replaceAll("\\.git", ""));
        return localRepo;
    }


    static def git_pull(remotePath) {
        def localRep = get_local_dir(remotePath);
        def git = new Git(new FileRepository(new File(localRep, ".git")));
        git.pull().call();
        return localRep.absolutePath;
    }


    static def git_url(remotePath) {
        def localRepo = get_local_dir(remotePath);
        if (localRepo.exists()) {
            return git_pull(remotePath);
        } else {
            return git_clone(remotePath);
        }
    }

    static def ant = new AntBuilder()   // create an antbuilder

    static def unzip(fzip, tdir, overwr=false) {
        ant.unzip( src:fzip,
            dest:tdir,
            overwrite:overwr);
    }
}