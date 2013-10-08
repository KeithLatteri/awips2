/**
 * This software was developed and / or modified by Raytheon Company,
 * pursuant to Contract DG133W-05-CQ-1067 with the US Government.
 * 
 * U.S. EXPORT CONTROLLED TECHNICAL DATA
 * This software product contains export-restricted data whose
 * export/transfer/disclosure is restricted by U.S. law. Dissemination
 * to non-U.S. persons whether in the United States or abroad requires
 * an export license or other authorization.
 * 
 * Contractor Name:        Raytheon Company
 * Contractor Address:     6825 Pine Street, Suite 340
 *                         Mail Stop B8
 *                         Omaha, NE 68106
 *                         402.291.0100
 * 
 * See the AWIPS II Master Rights File ("Master Rights File.pdf") for
 * further licensing information.
 **/
package com.raytheon.uf.viz.archive.ui;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.raytheon.uf.common.archive.config.ArchiveConfigManager;
import com.raytheon.uf.common.archive.config.ArchiveConstants;
import com.raytheon.uf.common.archive.config.DisplayData;
import com.raytheon.uf.common.status.IUFStatusHandler;
import com.raytheon.uf.common.status.UFStatus;
import com.raytheon.uf.common.status.UFStatus.Priority;
import com.raytheon.uf.common.util.FileUtil;
import com.raytheon.uf.viz.core.VizApp;
import com.raytheon.viz.ui.dialogs.CaveSWTDialog;

/**
 * This class performs the desired type of case creation and display a
 * progress/status message dialog.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Jun 3, 2013  1966       rferrel     Initial creation
 * Aug 16, 2013 2225       rferrel     Change structure of copy to include
 *                                     archive and category directory and 
 *                                     implementation of compression.
 * Oct 08, 2013 2442       rferrel     Remove category directory.
 * 
 * </pre>
 * 
 * @author rferrel
 * @version 1.0
 */

public class GenerateCaseDlg extends CaveSWTDialog {
    private final IUFStatusHandler statusHandler = UFStatus
            .getHandler(GenerateCaseDlg.class);

    /** Use to display the current state of the case generation. */
    private Label stateLbl;

    /** Progress bar to indicate activity. */
    private ProgressBar progressBar;

    /** Stops the case generation leaving it in an unknown state. */
    private Button cancelBtn;

    /** Active have generation is finish or has an error. */
    private Button closeBtn;

    /**
     * The case's destination directory. Assumed to be a sub-directory of
     * targetDir.
     */
    private final File caseDir;

    /** Starting time for the case. */
    private final Calendar startCal;

    /** End time for the case. */
    private final Calendar endCal;

    /** Data list for the case. */
    private final DisplayData[] sourceDataList;

    /** When true compress the case directory. */
    private final boolean doCompress;

    /** When true break the compress file into multiple files. */
    private final boolean doMultiFiles;

    // Needed when compress and split implemented
    // /** The compress size for multiple files. */
    // private final long splitSize;

    /** Job to perform the case generation off of the UI thread. */
    private GenerateJob generateJob;

    /** Shorten case name to use in status message. */
    private final String caseName;

    /** Listeners to add to the job when scheduled. */
    private final List<IJobChangeListener> jobListeners = new ArrayList<IJobChangeListener>();

    /** Data manager. */
    private final ArchiveConfigManager archiveManager = ArchiveConfigManager
            .getInstance();

    /**
     * Constructor.
     * 
     * @param parentShell
     * @param targetDir
     * @param caseDir
     * @param startCal
     * @param endCal
     * @param sourceList
     * @param doCompress
     * @param doMultiFiles
     * @param splitSize
     */
    protected GenerateCaseDlg(Shell parentShell, File targetDir, File caseDir,
            Calendar startCal, Calendar endCal, List<DisplayData> sourceList,
            boolean doCompress, boolean doMultiFiles, long splitSize) {
        super(parentShell, SWT.DIALOG_TRIM, CAVE.DO_NOT_BLOCK
                | CAVE.PERSPECTIVE_INDEPENDENT);
        this.caseDir = caseDir;
        this.startCal = startCal;
        this.endCal = endCal;
        this.sourceDataList = sourceList.toArray(new DisplayData[sourceList
                .size()]);
        Arrays.sort(this.sourceDataList, DisplayData.LABEL_ORDER);
        this.doCompress = doCompress;
        this.doMultiFiles = doMultiFiles;

        // Needed when compress and split implemented.
        // this.splitSize = splitSize;
        this.caseName = caseDir.getAbsolutePath().substring(
                targetDir.getAbsolutePath().length() + 1);
        setText("Generating - " + caseName);
    }

    /**
     * Add a job listener.
     * 
     * @param listener
     */
    protected void addJobChangeListener(IJobChangeListener listener) {
        jobListeners.add(listener);
    }

    /**
     * Remove a job listener.
     * 
     * @param listener
     */
    protected void removeJobChangeListener(IJobChangeListener listener) {
        jobListeners.remove(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.raytheon.viz.ui.dialogs.CaveSWTDialogBase#initializeComponents(org
     * .eclipse.swt.widgets.Shell)
     */
    @Override
    protected void initializeComponents(Shell shell) {
        Composite mainComp = new Composite(shell, SWT.NONE);
        GridLayout gl = new GridLayout(1, false);
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        gl.horizontalSpacing = 0;
        mainComp.setLayout(gl);

        init();
    }

    /**
     * Set up main layout.
     */
    private void init() {
        createProgress();
        GuiUtil.addSeparator(shell, SWT.HORIZONTAL);
        createBottomActionButtons();
    }

    /**
     * The progress component.
     */
    private void createProgress() {
        Composite progComp = new Composite(shell, SWT.NONE);
        GridLayout gl = new GridLayout(1, false);
        progComp.setLayout(gl);

        GridData gd = new GridData(SWT.FILL, SWT.DEFAULT, true, false);
        gd.minimumWidth = 300;
        stateLbl = new Label(shell, SWT.BORDER | SWT.CENTER);
        stateLbl.setLayoutData(gd);
        stateLbl.setText("state of progress goes here.");

        progressBar = new ProgressBar(shell, SWT.DEFAULT);
        gd = new GridData(SWT.FILL, SWT.DEFAULT, true, false);
        progressBar.setLayoutData(gd);
    }

    /**
     * Component for the action buttons.
     */
    private void createBottomActionButtons() {
        Composite actionComp = new Composite(shell, SWT.NONE);
        GridLayout gl = new GridLayout(2, false);
        actionComp.setLayout(gl);

        cancelBtn = new Button(actionComp, SWT.PUSH);
        cancelBtn.setText(" Cancel ");
        cancelBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (generateJob != null && generateJob.getState() != Job.NONE) {
                    generateJob.cancel();
                }

                close();
            }
        });

        closeBtn = new Button(actionComp, SWT.PUSH);
        closeBtn.setText(" Close ");
        closeBtn.setEnabled(false);
        closeBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                close();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.raytheon.viz.ui.dialogs.CaveSWTDialog#preOpened()
     */
    @Override
    protected void preOpened() {
        super.preOpened();

        generateJob = new GenerateJob();
        for (IJobChangeListener listener : jobListeners) {
            generateJob.addJobChangeListener(listener);
        }

        generateJob.schedule();
        shell.addShellListener(new ShellAdapter() {

            @Override
            public void shellClosed(ShellEvent e) {
                if (generateJob != null && generateJob.getState() != Job.NONE) {
                    e.doit = false;
                }
            }
        });
    }

    /**
     * Allow a non-UI thread to update the state label.
     * 
     * @param message
     * @param tooltip
     */
    private void setStateLbl(final String message, final String tooltip) {
        VizApp.runAsync(new Runnable() {
            @Override
            public void run() {
                if (!stateLbl.isDisposed()) {
                    stateLbl.setText(message);
                    stateLbl.setToolTipText(tooltip);
                }
            }
        });
    }

    /**
     * Allow a non-UI thread to update the progress bar and background of the
     * state label when there is an error.
     * 
     * @param value
     * @param state
     */
    private void setProgressBar(final int value, final int state) {
        VizApp.runAsync(new Runnable() {

            @Override
            public void run() {
                if (!progressBar.isDisposed()) {
                    int val = value;
                    if (val < progressBar.getMinimum()) {
                        val = progressBar.getMinimum();
                    } else if (val >= progressBar.getMaximum()) {
                        val = progressBar.getMaximum();
                        closeBtn.setEnabled(true);
                        cancelBtn.setEnabled(false);
                    }
                    progressBar.setSelection(val);
                    progressBar.setState(state);
                    progressBar.setVisible(val != progressBar.getMaximum());
                    Color bgColor = null;
                    if (state == SWT.ERROR) {
                        bgColor = shell.getDisplay().getSystemColor(
                                SWT.COLOR_RED);
                    }
                    stateLbl.setBackground(bgColor);
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.raytheon.viz.ui.dialogs.CaveSWTDialogBase#disposed()
     */
    @Override
    protected void disposed() {
        if (generateJob != null && generateJob.getState() != Job.NONE) {
            generateJob.cancel();
        }
    }

    /**
     * The performs the work of generating the case on a non-UI thread.
     */
    private class GenerateJob extends Job {
        private final AtomicBoolean shutdown = new AtomicBoolean(false);

        public GenerateJob() {
            super("Generate Case");
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            if (monitor.isCanceled()) {
                return Status.OK_STATUS;
            }

            setStateLbl("Creating: " + caseDir.getName(),
                    caseDir.getAbsolutePath());
            ICaseCopy caseCopy = null;

            String errorMessage = null;
            if (caseDir.exists()) {
                errorMessage = "Case exists: " + caseDir.getName();
            } else if (!caseDir.mkdirs()) {
                errorMessage = "Unable to create case: " + caseDir.getName();
            }

            if (errorMessage != null) {
                setStateLbl(errorMessage, caseDir.getAbsolutePath());
                setProgressBar(100, SWT.ERROR);
                return Status.OK_STATUS;
            }

            if (shutdown.get()) {
                return Status.OK_STATUS;
            }

            String currentArchive = null;
            String currentCategory = null;
            boolean updateDestDir = false;

            try {
                for (DisplayData displayData : sourceDataList) {
                    if (shutdown.get()) {
                        return Status.OK_STATUS;
                    }

                    if (!displayData.getArchiveName().equals(currentArchive)) {
                        updateDestDir = true;
                        currentArchive = displayData.getArchiveName();
                        currentCategory = displayData.getCategoryName();
                    } else if (!displayData.getCategoryName().equals(
                            currentCategory)) {
                        updateDestDir = true;
                        currentCategory = displayData.getCategoryName();
                    }

                    if (updateDestDir) {
                        updateDestDir = false;
                        if (caseCopy != null) {
                            caseCopy.finishCase();
                        } else {
                            if (!doCompress) {
                                caseCopy = new CopyMove();
                            } else if (doMultiFiles) {
                                caseCopy = new CompressAndSplitCopy();
                            } else {
                                caseCopy = new CompressCopy();
                            }
                        }
                        caseCopy.startCase(caseDir, displayData, shutdown);
                        setStateLbl(currentArchive + " | " + currentCategory,
                                caseDir.getAbsolutePath() + "\n"
                                        + currentArchive + "\n"
                                        + currentCategory);
                    }

                    List<File> files = archiveManager.getDisplayFiles(
                            displayData, startCal, endCal);
                    for (File source : files) {
                        if (shutdown.get()) {
                            return Status.OK_STATUS;
                        }

                        caseCopy.copy(source);
                    }
                }
                if (caseCopy != null) {
                    caseCopy.finishCase();
                }
                caseCopy = null;

                setStateLbl("Created: " + caseName, caseDir.getAbsolutePath());
                setProgressBar(100, SWT.NORMAL);

            } catch (CaseCreateException e) {
                statusHandler.handle(Priority.PROBLEM, e.getLocalizedMessage(),
                        e);
                setStateLbl(
                        "Failed to Create: " + caseName,
                        caseDir.getAbsolutePath() + "\n"
                                + e.getLocalizedMessage());
                setProgressBar(100, SWT.ERROR);
            } finally {
                if (caseCopy != null) {
                    try {
                        caseCopy.finishCase();
                    } catch (CaseCreateException ex) {
                        // Ignore
                    }
                    caseCopy = null;
                }
            }

            return Status.OK_STATUS;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.core.runtime.jobs.Job#canceling()
         */
        @Override
        protected void canceling() {
            shutdown.set(true);
            generateJob = null;
        }
    }

    /**
     * This class copies selected files/directories to a case-directory/archive.
     */
    private static class CopyMove implements ICaseCopy {
        /**
         * Flag to indicate user canceled the case generation.
         */
        private AtomicBoolean shutdown;

        /**
         * Top destination directory to move files/dirctories to.
         */
        private File destDir;

        /**
         * Index on source Files where relative path starts.
         */
        private int startRelativePath;

        /**
         * Copy source File to desired destination.
         * 
         * @param source
         * @param destination
         * @throws IOException
         */
        private void copyFile(File source, File destination) throws IOException {
            if (shutdown.get()) {
                return;
            }

            if (source.isDirectory()) {

                if (!destination.exists()) {
                    destination.mkdir();
                }

                String[] files = source.list();

                for (String file : files) {
                    copyFile(new File(source, file),
                            new File(destination, file));
                }
            } else {
                FileUtil.copyFile(source, destination);
                destination.setLastModified(source.lastModified());
            }
        }

        @Override
        public void copy(File source) throws CaseCreateException {
            String relativePath = source.getAbsolutePath().substring(
                    startRelativePath);
            File destination = new File(destDir, relativePath);
            try {
                destination.getParentFile().mkdirs();
                copyFile(source, destination);
            } catch (IOException ex) {
                throw new CaseCreateException("CopyMove.copy: ", ex);
            }
        }

        @Override
        public void startCase(File caseDir, DisplayData displayData,
                AtomicBoolean shutdown) {
            this.shutdown = shutdown;
            String archiveDirName = ArchiveConstants
                    .convertToFileName(displayData.getArchiveName());
            destDir = new File(caseDir, archiveDirName);
            destDir.mkdirs();
            startRelativePath = displayData.getRootDir().length();
        }

        @Override
        public void finishCase() {
            // Nothing to do.
        }
    }

    /**
     * This class takes selected directories/files to
     * case-directory/archive/compress-category-file. The compress-category-file
     * is a tar gzip file containing the categorie's data.
     */
    private static class CompressCopy implements ICaseCopy {
        /**
         * Flag to indicate user canceled case generation.
         */
        private AtomicBoolean shutdown;

        /**
         * Top Level destination directory.
         */
        private File destDir;

        /**
         * Stream to the file being created.
         */
        private FileOutputStream fileStream;

        /**
         * Stream to perform the compression.
         */
        private GZIPOutputStream zipStream;

        /**
         * Stream to create the tar image.
         */
        private ArchiveOutputStream tarStream;

        /**
         * Index to start of relative path in source File.
         */
        private int startRelativePath;

        /**
         * Directories already created in the tar image.
         */
        private final HashSet<File> tarDirFile = new HashSet<File>();

        /**
         * Buffer to use for reading in a file.
         */
        private final byte[] buffer = new byte[(int) (32 * FileUtils.ONE_KB)];

        @Override
        public void copy(File source) throws CaseCreateException {
            try {
                addParentDir(source);
                addTarFiles(new File[] { source });
            } catch (IOException e) {
                throw new CaseCreateException("Compress Copy failed: ", e);
            }
        }

        /**
         * Add list of Files to the tar image.
         * 
         * @param files
         * @throws IOException
         */
        private void addTarFiles(File[] files) throws IOException {
            for (File file : files) {
                if (shutdown.get()) {
                    return;
                }
                String name = file.getAbsolutePath().substring(
                        startRelativePath);
                if (file.isDirectory()) {
                    if (!tarDirFile.contains(file)) {
                        TarArchiveEntry entry = new TarArchiveEntry(file, name);
                        tarStream.putArchiveEntry(entry);
                        tarStream.closeArchiveEntry();
                        tarDirFile.add(file);
                        addTarFiles(file.listFiles());
                    }
                } else {
                    TarArchiveEntry entry = new TarArchiveEntry(file, name);
                    entry.setSize(file.length());
                    FileInputStream fileStream = null;
                    tarStream.putArchiveEntry(entry);
                    try {
                        fileStream = new FileInputStream(file);
                        int len;
                        while ((len = fileStream.read(buffer)) != -1) {
                            tarStream.write(buffer, 0, len);
                        }
                    } finally {
                        if (fileStream != null) {
                            closeStream(fileStream);
                        }
                    }

                    tarStream.closeArchiveEntry();
                }
            }
        }

        /**
         * Convince method to close a steam and ignore any IOException.
         * 
         * @param stream
         */
        private void closeStream(Closeable stream) {
            try {
                stream.close();
            } catch (IOException ex) {
                // Ignore
            }
        }

        /**
         * If needed add parent directories to the tar image.
         * 
         * @param file
         * @throws IOException
         */
        private void addParentDir(File file) throws IOException {
            File parent = file.getParentFile();
            if (parent != null && !tarDirFile.contains(parent)
                    && (parent.getAbsolutePath().length() > startRelativePath)) {
                addParentDir(parent);
                String name = parent.getAbsolutePath().substring(
                        startRelativePath);
                TarArchiveEntry entry = new TarArchiveEntry(parent, name);
                tarStream.putArchiveEntry(entry);
                tarStream.closeArchiveEntry();
                tarDirFile.add(parent);
            }
        }

        @Override
        public void startCase(File caseDir, DisplayData displayData,
                AtomicBoolean shutdown) throws CaseCreateException {
            try {
                this.shutdown = shutdown;
                String archiveDirName = ArchiveConstants
                        .convertToFileName(displayData.getArchiveName());
                String categoryDirName = ArchiveConstants
                        .convertToFileName(displayData.getCategoryName());
                destDir = new File(caseDir, archiveDirName);
                destDir.mkdirs();
                tarDirFile.clear();
                startRelativePath = displayData.getRootDir().length();
                File tarFile = new File(destDir, categoryDirName
                        + ArchiveConstants.TAR_EXTENSION);
                fileStream = new FileOutputStream(tarFile);
                zipStream = new GZIPOutputStream(fileStream);
                ArchiveStreamFactory factory = new ArchiveStreamFactory();
                tarStream = factory.createArchiveOutputStream(
                        ArchiveStreamFactory.TAR, zipStream);
                if (tarStream instanceof TarArchiveOutputStream) {
                    ((TarArchiveOutputStream) tarStream)
                            .setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
                }
            } catch (Exception e) {
                throw new CaseCreateException("CompressCopy.startCase: ", e);
            }
        }

        @Override
        public void finishCase() throws CaseCreateException {
            try {
                if (tarStream != null) {
                    tarStream.finish();
                }
                if (zipStream != null) {
                    zipStream.finish();
                }
            } catch (IOException e) {
                throw new CaseCreateException("CaseCopy.finish: ", e);
            } finally {
                if (tarStream != null) {
                    closeStream(tarStream);
                } else if (zipStream != null) {
                    closeStream(zipStream);
                } else if (fileStream != null) {
                    closeStream(fileStream);
                }
                tarStream = null;
                zipStream = null;
                fileStream = null;
            }
        }
    }

    /*
     * This class intended for making "image" files read for burning to a CD or
     * DVD. Need to resolve issues on how this should be done.
     */
    private static class CompressAndSplitCopy implements ICaseCopy {

        public void startCase(File caseDir, DisplayData displayData,
                AtomicBoolean shutdown) throws CaseCreateException {
            throw new CaseCreateException(
                    "Compress and split not yet implemented.");
        }

        @Override
        public void copy(File source) throws CaseCreateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishCase() {
            // TODO Auto-generated method stub
        }

        // TODO Example code for future implementation of this class.
        // Will need to break up into the starCase, copy and finishCase will
        // need close and join.

        // private void compressAndSplitCase() {
        // ArchiveOutputStream tarStream = null;
        // GZIPOutputStream zipStream = null;
        // try {
        // Pipe pipe = Pipe.open();
        // OutputStream poStream = Channels.newOutputStream(pipe.sink());
        // zipStream = new GZIPOutputStream(poStream);
        // ArchiveStreamFactory factory = new ArchiveStreamFactory();
        //
        // tarStream = factory.createArchiveOutputStream(
        // ArchiveStreamFactory.TAR, zipStream);
        //
        // if (tarStream instanceof TarArchiveOutputStream) {
        // ((TarArchiveOutputStream) tarStream)
        // .setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        // }
        //
        // final InputStream piStream = Channels.newInputStream(pipe
        // .source());
        // splitDone.set(false);
        //
        // Job splitJob = new Job("Split") {
        //
        // @Override
        // protected IStatus run(IProgressMonitor monitor) {
        // OutputStream splitStream = null;
        // long totSize = 0;
        // try {
        // byte[] buffer = new byte[12 * 1024];
        //
        // int bufCnt = 0;
        // long splitCnt = 0L;
        // while ((bufCnt = piStream.read(buffer)) != -1) {
        // totSize += bufCnt;
        // if (splitStream == null) {
        // splitStream = openSplitFile(++numSplitFiles);
        // }
        // long fileSize = splitCnt + bufCnt;
        // if (fileSize < splitSize) {
        // splitStream.write(buffer, 0, bufCnt);
        // splitCnt = fileSize;
        // } else if (fileSize == splitSize) {
        // splitStream.write(buffer, 0, bufCnt);
        // splitStream.close();
        // splitStream = null;
        // splitCnt = 0L;
        // } else {
        // int cnt = (int) (splitSize - splitCnt);
        // splitStream.write(buffer, 0, cnt);
        // splitStream.close();
        // splitStream = openSplitFile(++numSplitFiles);
        // int remainder = bufCnt - cnt;
        // splitStream.write(buffer, cnt, remainder);
        // splitCnt = remainder;
        // }
        // }
        // } catch (IOException e) {
        // statusHandler.handle(Priority.PROBLEM,
        // e.getLocalizedMessage(), e);
        // } finally {
        // if (splitStream != null) {
        // try {
        // splitStream.close();
        // } catch (IOException e) {
        // // Ignore
        // }
        // }
        // splitDone.set(true);
        // System.out.println("totalSize: " + totSize
        // + ", splitSize: " + splitSize
        // + ", numSplitFiles: " + numSplitFiles);
        // }
        //
        // return Status.OK_STATUS;
        // }
        // };
        // splitJob.schedule();
        //
        // createTarFile(tarStream, caseDir.listFiles());
        // tarStream.finish();
        // zipStream.finish();
        // try {
        // tarStream.close();
        // } catch (IOException ex) {
        // // Ignore
        // }
        // tarStream = null;
        //
        // try {
        // zipStream.close();
        // } catch (IOException ex) {
        // // Ignore
        // }
        // zipStream = null;
        //
        // while (!splitDone.get()) {
        // if (splitJob.getState() == Job.RUNNING) {
        // try {
        // System.out.println("splitJob.join()");
        // splitJob.join();
        // } catch (InterruptedException e) {
        // statusHandler.handle(Priority.INFO,
        // e.getLocalizedMessage(), e);
        // }
        // } else {
        // try {
        // private void compressAndSplitCase() {
        // ArchiveOutputStream tarStream = null;
        // GZIPOutputStream zipStream = null;
        // try {
        // Pipe pipe = Pipe.open();
        // OutputStream poStream = Channels.newOutputStream(pipe.sink());
        // zipStream = new GZIPOutputStream(poStream);
        // ArchiveStreamFactory factory = new ArchiveStreamFactory();
        //
        // tarStream = factory.createArchiveOutputStream(
        // ArchiveStreamFactory.TAR, zipStream);
        //
        // if (tarStream instanceof TarArchiveOutputStream) {
        // ((TarArchiveOutputStream) tarStream)
        // .setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        // }
        //
        // final InputStream piStream = Channels.newInputStream(pipe
        // .source());
        // splitDone.set(false);
        //
        // Job splitJob = new Job("Split") {
        //
        // @Override
        // protected IStatus run(IProgressMonitor monitor) {
        // OutputStream splitStream = null;
        // long totSize = 0;
        // try {
        // byte[] buffer = new byte[12 * 1024];
        //
        // int bufCnt = 0;
        // long splitCnt = 0L;
        // while ((bufCnt = piStream.read(buffer)) != -1) {
        // totSize += bufCnt;
        // if (splitStream == null) {
        // splitStream = openSplitFile(++numSplitFiles);
        // }
        // long fileSize = splitCnt + bufCnt;
        // if (fileSize < splitSize) {
        // splitStream.write(buffer, 0, bufCnt);
        // splitCnt = fileSize;
        // } else if (fileSize == splitSize) {
        // splitStream.write(buffer, 0, bufCnt);
        // splitStream.close();
        // splitStream = null;
        // splitCnt = 0L;
        // } else {
        // int cnt = (int) (splitSize - splitCnt);
        // splitStream.write(buffer, 0, cnt);
        // splitStream.close();
        // splitStream = openSplitFile(++numSplitFiles);
        // int remainder = bufCnt - cnt;
        // splitStream.write(buffer, cnt, remainder);
        // splitCnt = remainder;
        // }
        // }
        // } catch (IOException e) {
        // statusHandler.handle(Priority.PROBLEM,
        // e.getLocalizedMessage(), e);
        // } finally {
        // if (splitStream != null) {
        // try {
        // splitStream.close();
        // } catch (IOException e) {
        // // Ignore
        // }
        // }
        // splitDone.set(true);
        // System.out.println("totalSize: " + totSize
        // + ", splitSize: " + splitSize
        // + ", numSplitFiles: " + numSplitFiles);
        // }
        //
        // return Status.OK_STATUS;
        // }
        // };
        // splitJob.schedule();
        //
        // createTarFile(tarStream, caseDir.listFiles());
        // tarStream.finish();
        // zipStream.finish();
        // try {
        // tarStream.close();
        // } catch (IOException ex) {
        // // Ignore
        // }
        // tarStream = null;
        //
        // try {
        // zipStream.close();
        // } catch (IOException ex) {
        // // Ignore
        // }
        // zipStream = null;
        //
        // while (!splitDone.get()) {
        // if (splitJob.getState() == Job.RUNNING) {
        // try {
        // System.out.println("splitJob.join()");
        // splitJob.join();
        // } catch (InterruptedException e) {
        // statusHandler.handle(Priority.INFO,
        // e.getLocalizedMessage(), e);
        // }
        // } else {
        // try {
        // Thread.sleep(200L);
        // } catch (InterruptedException e) {
        // statusHandler.handle(Priority.INFO,
        // e.getLocalizedMessage(), e);
        // }
        // }
        // }
        // } catch (IOException e) {
        // statusHandler.handle(Priority.PROBLEM, e.getLocalizedMessage(),
        // e);
        // } catch (ArchiveException e1) {
        // statusHandler.handle(Priority.PROBLEM,
        // e1.getLocalizedMessage(), e1);
        // } finally {
        // if (tarStream != null) {
        // try {
        // tarStream.close();
        // } catch (IOException e) {
        // // Ignore
        // }
        // }
        //
        // if (zipStream != null) {
        // try {
        // zipStream.close();
        // } catch (IOException e) {
        // // Ignore
        // }
        // }
        // }
        // setProgressBar(100, SWT.NORMAL);
        // deleteCaseDir();
        // String message = caseDir.getName() + "split into " + numSplitFiles
        // + " file(s).";
        // setStateLbl(message, null);
        // }
        // Thread.sleep(200L);
        // } catch (InterruptedException e) {
        // statusHandler.handle(Priority.INFO,
        // e.getLocalizedMessage(), e);
        // }
        // }
        // }
        // } catch (IOException e) {
        // statusHandler.handle(Priority.PROBLEM, e.getLocalizedMessage(),
        // e);
        // } catch (ArchiveException e1) {
        // statusHandler.handle(Priority.PROBLEM,
        // e1.getLocalizedMessage(), e1);
        // } finally {
        // if (tarStream != null) {
        // try {
        // tarStream.close();
        // } catch (IOException e) {
        // // Ignore
        // }
        // }
        //
        // if (zipStream != null) {
        // try {
        // zipStream.close();
        // } catch (IOException e) {
        // // Ignore
        // }
        // }
        // }
        // setProgressBar(100, SWT.NORMAL);
        // deleteCaseDir();
        // String message = caseDir.getName() + "split into " + numSplitFiles
        // + " file(s).";
        // setStateLbl(message, null);
        // }

    }
}
