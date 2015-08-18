package com.DamonLiu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.Tools;
/**
 * 
 * @author damonliu
 * @version 将所有指定图片复制到当前工程目录下
 *
 */
public class PNGSelector {
	
	static String path = "/Users/damonliu/Documents/Github/tuniuapp-iphone";// the
	
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return size + "Byte(s)";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
	}

	


//	public static void copy(File orig, File dest) { // 用于改后缀后复制
//		BufferedReader buf = null;
//		PrintWriter out = null;
//		;
//		try {
//			buf = new BufferedReader(new InputStreamReader(new FileInputStream(orig))); // 读取流，提示阅读效率
//			out = new PrintWriter(dest + "/" + orig.getName());
//		} catch (IOException e) {
//			// System.out.println("文件orig或者dest异常");
//			// System.out.println(e.printStackTrace(););
//			e.printStackTrace();
//		}
//		String line = null;
//		try {
//			// while ((line = buf.readLine()) != null) {
//			// out.println(line);
//			// }
//			int n;
//			char[] cbuf = new char[1024];
//			while ((n = buf.read(cbuf)) != -1) {
//				out.write(cbuf, 0, n);
//			}
//
//		} catch (IOException e) {
//
//		} finally {
//			try {
//				if (buf != null)
//					buf.close();
//				if (out != null)
//					out.close();
//			} catch (Exception e2) {
//
//			}
//		}
//	}

	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		// 创建目录
		if (dir.mkdirs()) {
			System.out.println("创建目录" + destDirName + "成功！");
			return true;
		} else {
			System.out.println("创建目录" + destDirName + "失败！");
			return false;
		}
	}

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<File> tmp = Tools.getListFiles(path, "png", true);
		// String desti = "./Pictures";
		// File file = new File(desti);
		Collections.sort(tmp, new Comparator<File>() {
			@Override
			public int compare(File o2, File o1) {
				// TODO Auto-generated method stub
				String path1 = o1.getPath().substring(path.length() + 6);
				String path2 = o2.getPath().substring(path.length() + 6);
				int v = path1.compareTo(path2);
				if (v == 0)
					return (int) (o1.length() - o2.length());
				else {
					return v;
				}
			}
		});
		FileOutputStream fos;
		String newline = System.getProperty("line.separator");
		// String toPath = "pics";
		String toPath = "./pictures/";
		PNGSelector.createDir(toPath);
		int size = 0;
		try {

			fos = new FileOutputStream(new File("result.txt"), false);
			for (File file : tmp) {
				size += file.length();
				String info = file.getAbsolutePath().substring(path.length() - 15) + " size:"
						+ PNGSelector.getFormatSize(file.length());
				fos.write(info.getBytes());
				fos.write(newline.getBytes());
				// copy(file, new File(toPath));
				String path1 = file.getAbsolutePath();
				String path2 = toPath + file.getName();
				Tools.copyFile(path1, path2, true);
				// System.out.println();
			}
			System.out.println(getFormatSize(size));
			System.out.println(tmp.size() + " files");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
