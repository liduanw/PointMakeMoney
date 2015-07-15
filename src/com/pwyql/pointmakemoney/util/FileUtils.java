package com.pwyql.pointmakemoney.util;

import java.io.File;
import java.io.FileFilter;

/**
 */
public class FileUtils {

    public static void deleteDir(String dir) {
	File f = new File(dir);
	if (f.isDirectory()) {
	    f.listFiles(new FileFilter() {

		@Override
		public boolean accept(File file) {
		    // TODO Auto-generated method stub
		    if (file.isDirectory()) {
			deleteDir(file.getAbsolutePath());
		    }
		    file.delete();
		    return false;
		}
	    });
	}

	f.delete();

    }
}
