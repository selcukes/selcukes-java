package io.github.selcukes.core.helper;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class FileHelper {
	
	protected static final String SUPPORT_FOLDER = "support";
	/**
	 * Get Json folder path
	 * @param path filepath
	 * @return filepath
	 */
	public static String ymalFolder(String path) {
		File file = new File(path);
		for (String item : Objects.requireNonNull(file.list())) {

			if (SUPPORT_FOLDER.equals(item)) {
				return file.getAbsolutePath() + "/" + SUPPORT_FOLDER + "/yaml/";
			}
		}
		return ymalFolder(file.getParent());
	}
	/**
	 * Get WebDriver folder path
	 * @param path filepath
	 * @return filepath
	 */
	public static String driversFolder(String path) {
		File file = new File(path);
		for (String item : Objects.requireNonNull(file.list())) {
			
			if (SUPPORT_FOLDER.equals(item)) {
				return file.getAbsolutePath() + "/" + SUPPORT_FOLDER + "/webdrivers/";
			}
		}
		return driversFolder(file.getParent());
	}

	/**
	 * Set driver file as executable in Linux environment
	 * @param fileName filename
	 */
	public static void setFileExecutable(String fileName)
	{
		try {
		final Path filepath = Paths.get(fileName);
		final Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(filepath);
	    permissions.add(PosixFilePermission.OWNER_EXECUTE);
	    Files.setPosixFilePermissions(filepath, permissions);
		}catch(Exception e)
		{
			ExceptionHelper.rethrow(e);
		}
	}

	/**
	 *
	 * @param filePath filepath
	 * @return filePath filepath
	 */
	public static String systemFilePath(String filePath) {
		String separator = System.getProperty("file.separator");

		if (StringUtils.isNotBlank(separator) && "\\".equals(separator)) {
			int beginIndex = 0;

			if (filePath.startsWith("/")) {
				beginIndex = 1;
			}

			return filePath.substring(beginIndex).replaceAll("/", "\\\\");
		}

		return filePath;
	}

	/**
	 * Create a folder if not exist
	 * @param dirName Name of the folder
	 * @throws IOException if folder not found
	 */
	public static void createDirectory(File dirName) throws IOException {
		if (!dirName.exists()) {
			dirName.mkdirs();
		} else if (!dirName.isDirectory()) {
			throw new IOException("\"" + dirName + "\" is not a directory or a file.");
		}
	}

	public static void deleteFilesInDirectory(File dirName) throws IOException {
		try {
			FileUtils.cleanDirectory(dirName);
		} catch (IOException e) {
			throw new IOException("\"" + dirName + "\" is not a directory or a file to delete.",e);
		}
	}
	public static void deleteFile(File fileName)
	{
		if(fileName.exists())
			FileUtils.deleteQuietly(fileName);
	}
	
}

