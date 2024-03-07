package com.kalsym.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ali Khan
 */
public class FileManipulation {

    static Logger logger = LoggerFactory.getLogger("com.kalsym.utility");

    /**
     * Copies a file on local file system, from sourcePath, to destinationPath
     *
     * @param sourcePath The source Path of the file to be copied from
     * @param destinationPath The destination Path of the file to be copied to
     */
    public static void copyFile(String sourcePath, String destinationPath) {
        try {
            File f1 = new File(sourcePath);
            File f2 = new File(destinationPath);
            InputStream in = new FileInputStream(f1);

            // For Append the file.
            // OutputStream out = new FileOutputStream(f2,true);
            // For Overwrite the file.
            OutputStream out = new FileOutputStream(f2);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            logger.info("File copied.");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " in the specified directory.");
            logger.error(ex.getMessage() + " in the specified directory.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            logger.error(e.getMessage());
        }
    }

    /**
     * Checks if a file or directory exists on local file system
     *
     * @param Path
     * @return ture if the Path is a directory path or a file path, else false
     */
    public static boolean fileOrDirectoryExists(String Path) {
        try {
            File f = new File(Path);
            if (f.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exp) {
            return false;
        }
    }

    /**
     * Creates a directory on the specified path, returns true if successful,
     * false: otherwise
     *
     * @param Path
     * @return
     *
     * @throws SecurityException If a security manager exists and its <code>{@link
     *          java.lang.SecurityManager#checkWrite(java.lang.String)}</code> method
     * does not permit the named directory to be created
     */
    public static boolean createDirectory(String Path) {
        File f = new File(Path);
        if (f.mkdir()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets File Name from the file Path, by calling java.io.File.getName();
     *
     * @param FilePath
     * @return
     */
    public static String getFileNameFromPath(String FilePath) {
        String fileName = new File(FilePath).getName();
        return fileName;
    }

    /**
     * Copies a file on local file system, from FilePath, to DestDirectory
     *
     * @param FilePath
     * @param DestDir
     */
    public static void copyFileToDestinationDirectory(String FilePath,
            String DestDir) {
        if (!fileOrDirectoryExists(DestDir)) {
            //create directory here
            createDirectory(DestDir);
        }
        String destPath = DestDir + "/" + getFileNameFromPath(FilePath);
        copyFile(FilePath, destPath);
    }

    /**
     * Returns the MMYYYY directory format from the sql.Date object
     *
     * @param date
     * @return
     */
    public static String getDirFormatFromDate(Date date) {
        //LocalString Format: Dec 2, 2012 12:00:00 AM
        String localeString = date.toLocaleString();
        String MMM = localeString.substring(0, 3).toUpperCase();
        int commaIndex = localeString.indexOf(',');
        String YYYY = localeString.substring(commaIndex + 2, commaIndex + 6);
        String finalString = MMM + YYYY;
        return finalString;
    }

    /**
     * Reads the Complete File Content as String
     *
     * @param FilePath
     * @return Returns the read File Content using the default encoding
     * @throws java.io.FileNotFoundException
     */
    public static String readFileContentAsString(String FilePath) throws FileNotFoundException, IOException {
        BufferedReader br;
        br = new BufferedReader(new FileReader(FilePath));
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            String allContent = sb.toString();
            return allContent;
        } finally {
            try {
                br.close();
            } catch (Exception ex) {
            }
        }
    }
}
