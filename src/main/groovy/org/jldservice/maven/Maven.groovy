package org.jldservice.maven

import org.apache.maven.cli.MavenCli
import org.codehaus.plexus.util.FileUtils

/**
 * @Chunqi SHI (diligence.cs@gmail.com)
 */

// https://github.com/mariuszs/maven-cli-example/blob/master/pom.xml
// https://github.com/mariuszs/maven-cli-example

class Maven {

    static def copyDependencies (dependencies, libdirectory){
        def pomxml = new PomXml()
        pomxml.addDependencies(dependencies)
        def targetxml = pomxml.setDependenciesCopyDirectory(libdirectory)
        new File(libdirectory, "pom.xml").withWriter() { it << targetxml }
        MavenCli cli = new MavenCli();
        def cmds = ["dependency:copy-dependencies"] as String[];
        cli.doMain(cmds, libdirectory, System.out, System.out);
    }


    static def compile(pomDir) {
        MavenCli cli = new MavenCli();
        def cmds = ["compile"] as String[];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(os);
        cli.doMain(cmds, pomDir, System.out, err);
        return os.toString();
    }


    static def singleJar(pomDir) {
        File fil = new File(pomDir, "pom.xml");
        File filbak = new File(pomDir, "pom.xml.bak");
        String pomStr = FileUtils.fileRead(fil);
        pomStr = PomXml.setAssemblyPlugin(pomStr);
        FileUtils.copyFile(fil, filbak);
        FileUtils.fileWrite(fil.absolutePath,pomStr);
        MavenCli cli = new MavenCli();
        def cmds = ["compile", "assembly:single"] as String[];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(os);
        cli.doMain(cmds, pomDir, System.out, err);
        FileUtils.copyFile(fil, filbak);
        filbak.delete();
        return os.toString();
    }




    public static void merges(String mergedFolderStr,
                                                String... foldersStr) {
        final File mergedFolder = new File(mergedFolderStr);
        final Map<String, File> filesMap = new HashMap<String, File> ();
        for (String folder : foldersStr) {
            updateFilesMap(new File (folder), filesMap, null);
        }

        for (final Map.Entry<String, File> fileEntry : filesMap.entrySet()) {
            final String relativeName = fileEntry.getKey();
            final File srcFile = fileEntry.getValue();
            FileUtils.copyFile (srcFile, new File (mergedFolder, relativeName));
        }
    }

    private static void updateFilesMap(final File baseFolder, final Map<String, File> filesMap,
                                final String relativeName) {
        for (final File file : baseFolder.listFiles()) {
            final String fileRelativeName = getFileRelativeName (relativeName, file.getName());

            if (file.isDirectory()) {
                updateFilesMap(file, filesMap, fileRelativeName);
            }
            else {
                final File existingFile = filesMap.get (fileRelativeName);
                if (existingFile == null || file.lastModified() > existingFile.lastModified() ) {
                    filesMap.put (fileRelativeName, file);
                }
            }
        }
    }

    private static String getFileRelativeName(final String baseName, final String fileName) {
        return baseName == null ? fileName : baseName + "/" + fileName;
    }
}