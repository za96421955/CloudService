package com.cloudservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 工具类：文件
 * @author 陈晨
 * @date 2013.09.03
 */
public final class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	private FileUtil() {}

	/**
	 * 创建新文件, 迭代生成文件目录
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @author: 陈晨<br>
	 * @date: 2015-3-19<br>
	 */
	public static File create(String filePath) throws IOException {
		if (filePath == null) {
			return null;
		}
		// 分解文件目录
		String[] splitFilePathes = filePath.split("\\\\");
		if (splitFilePathes.length <= 1) {
			splitFilePathes = filePath.split("/");
		}
		// 创建文件
		File file = null;
		StringBuilder path = new StringBuilder(splitFilePathes[0]);
		for (int i = 1; i < splitFilePathes.length - 1; i++) {
			path.append("/").append(splitFilePathes[i]);
			file = new File(path.toString());
			if (file.exists()) {
				continue;
			}
			if (!file.mkdir()) {
				throw new IOException("文件创建失败");
			}
		}
		return file;
	}
	
	/**
	 * 追加文件内容
	 * @param content
	 * @param filePath
	 * @param charSet
	 * @return
	 * @author: 陈晨<br>
	 * @date: 2016年1月26日<br>
	 */
	public static File append(String content, String filePath, String... charSet) {
		FileOutputStream out = null;
		OutputStreamWriter writer = null;
		try {
			File file = new File(filePath);
			out = new FileOutputStream(file, true);
			if (charSet != null && charSet.length > 0) {
				writer = new OutputStreamWriter(out, charSet[0]);
			} else {
				writer = new OutputStreamWriter(out);
			}
			writer.append(content);
			return file;
		} catch (FileNotFoundException fnfe) {
			logger.error("[append] {}", fnfe.getMessage(), fnfe);
		} catch (UnsupportedEncodingException uee) {
			logger.error("[append] {}", uee.getMessage(), uee);
		}  catch (IOException ioe) {
			logger.error("[append] {}", ioe.getMessage(), ioe);
		}  finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();	
				}
			} catch (Exception e) {
				logger.error("[append] {}", e.getMessage(), e);
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				logger.error("[append] {}", e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * @description 创建 & 追加文件
	 * <p>〈功能详细描述〉</p>
	 *
	 * @auther  陈晨(17061934)
	 * @date    2020/1/4 15:56
	 * @param   content, filePath, charSet
	 */
	public static File createAndAppend(String content, String filePath, String... charSet) {
		try {
			create(filePath);
		} catch (Exception e) {
			logger.error("[append] {}", e.getMessage(), e);
		}
		return append(content, filePath, charSet);
	}
	
	/**
	 * 读取文件内容
	 * @param file
	 * @param charSet
	 * @return
	 * @author: 陈晨<br>
	 * @date: 2015-3-19<br>
	 */
	public static String read(File file, String... charSet) {
		StringBuilder content = new StringBuilder("");
		BufferedReader reader = null;
		try {
			if (charSet != null && charSet.length > 0) {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charSet[0]));
			} else {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			}
			int ch;
			while ((ch = reader.read()) != -1) {
				content.append((char)ch);
			}
		} catch (IOException ioe) {
			logger.error("[read] {}", ioe.getMessage(), ioe);
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (Exception e) {
				logger.error("[read] {}", e.getMessage(), e);
			}
		}
		return content.toString();
	}
	
	/**
	 * 读取文件内容
	 * @param filePath
	 * @param charSet
	 * @return
	 * @author: 陈晨<br>
	 * @date: 2015-3-19<br>
	 */
	public static String read(String filePath, String... charSet) {
		File readFile = new File(filePath);
		return read(readFile, charSet);
	}

}


