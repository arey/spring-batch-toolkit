package com.javaetmoi.core.batch.tasklet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Uncompress all files of a zip archive to a sub-directory.
 * <p/>
 * <p>
 * Archive filename could be dynamically set from job context with an SpEL.<br/>
 * The sub-directory where archive is unzipped matches with the archive filename without the zip extension
 * (ie. myfile.zip => myfile subdirectory).<br/>
 * Once processed by another tasklet, the sub-directory could be deleted with the DeleteDirectoryTasklet.
 * </p>
 */
public class UncompressTasklet implements Tasklet {

    private String archiveFilename;

    private String uncompressBaseDirectory;

    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) throws IOException {

        File inputFile = new File(archiveFilename);
        ZipInputStream zis = new ZipInputStream(new FileInputStream(inputFile));

        File targetDirAsFile = new File(uncompressBaseDirectory, archiveFilename.replace(".zip", ""));
        if (!targetDirAsFile.exists()) {
            FileUtils.forceMkdir(targetDirAsFile);
        }

        BufferedOutputStream dest;
        ZipEntry entry = zis.getNextEntry();
        while (entry != null) {

            if (entry.isDirectory()) {
                File target = new File(targetDirAsFile, entry.getName());
                if (!target.exists()) {
                    FileUtils.forceMkdir(target);
                }
            } else {
                File target = new File(targetDirAsFile, entry.getName());
                if (!target.exists()) {
                    target.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(target);
                dest = new BufferedOutputStream(fos);
                IOUtils.copy(zis, dest);
                dest.flush();
                dest.close();
                contribution.incrementWriteCount(1);
            }

            entry = zis.getNextEntry();
        }
        zis.close();

        contribution.incrementReadCount();
        return RepeatStatus.FINISHED;
    }


    public void setUncompressBaseDirectory(String uncompressBaseDirectory) {
        this.uncompressBaseDirectory = uncompressBaseDirectory;
    }

    public void setArchiveFilename(String archiveFilename) {
        this.archiveFilename = archiveFilename;
    }
}