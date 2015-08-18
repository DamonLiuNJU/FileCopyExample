package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Tools {
	/**
	 * 复制单个文件
	 * 
	 * @param srcFilePath
	 *            待复制的文件名
	 * @param destFilePath
	 *            目标文件名
	 * @param overlay
	 *            如果目标文件存在，是否覆盖
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFile(String srcFilePath, String destFilePath, boolean overlay) {
		// 判断原文件是否存在
		File srcFile = new File(srcFilePath);
		if (!srcFile.exists()) {
			System.out.println("复制文件失败：原文件" + srcFilePath + "不存在！");
			return false;
		} else if (!srcFile.isFile()) {
			System.out.println("复制文件失败：" + srcFilePath + "不是一个文件！");
			return false;
		}
		// 判断目标文件是否存在
		File destFile = new File(destFilePath);
		if (destFile.exists()) {
			// 如果目标文件存在，而且复制时允许覆盖。
			if (overlay) {
				// 删除已存在的目标文件，无论目标文件是目录还是单个文件
				
				if(srcFile.length() == destFile.length()){
					System.out.println("文件大小一致，不予覆盖！"+srcFile.getAbsolutePath());
					return false;
				}
//				System.out.println("目标文件已存在，准备删除它！");
				// if(!DeleteFileUtil.delete(destFileName)){
				// System.out.println("复制文件失败：删除目标文件" + destFileName + "失败！");
				// return false;
				// } else {
				// System.out.println("复制文件失败：目标文件" + destFileName + "已存在！");
				// return false;
				// }
			}
		} else {
			if (!destFile.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建目录
				System.out.println("目标文件所在的目录不存在，准备创建它！");
				if (!destFile.getParentFile().mkdirs()) {
					System.out.println("复制文件失败：创建目标文件所在的目录失败！");
					return false;
				}
			}
		}
		// 准备复制文件
		int byteread = 0;// 读取的位数
		InputStream in = null;
		OutputStream out = null;
		try {
			// 打开原文件
			in = new FileInputStream(srcFile);
			// 打开连接到目标文件的输出流
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			// 一次读取1024个字节，当byteread为-1时表示文件已经读完
			while ((byteread = in.read(buffer)) != -1) {
				// 将读取的字节写入输出流
				out.write(buffer, 0, byteread);
			}
//			System.out.println("复制单个文件" + srcFileName + "至" + destFileName + "成功！");
			return true;
		} catch (Exception e) {
			System.out.println("复制文件失败：" + e.getMessage());
			return false;
		} finally {
			// 关闭输入输出流，注意先关闭输出流，再关闭输入流
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	public static List<File> getListFiles(String path, String suffix, boolean isdepth) {
		List<File> lstFileNames = new ArrayList<File>();
		File file = new File(path);
		return listFile(lstFileNames, file, suffix, isdepth);
	}
	
	public static List<File> getListFilesWithoutLimit(String path, String suffix, boolean isdepth) {
		List<File> lstFileNames = new ArrayList<File>();
		File file = new File(path);
		return listFileWithoutLimit(lstFileNames, file, suffix, isdepth);
	}
	
	
	
	public static List<File> listFileWithoutLimit(List<File> lstFileNames, File f, String suffix, boolean isdepth) {
		// 若是目录, 采用递归的方法遍历子目录
				if (f.isDirectory()) {
					File[] t = f.listFiles();

					for (int i = 0; i < t.length; i++) {
						if (isdepth || t[i].isFile()) {
							listFileWithoutLimit(lstFileNames, t[i], suffix, isdepth);
						}
					}
				} else {
					String filePath = f.getAbsolutePath();
					if (!suffix.equals("")) {
						int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
						String tempsuffix = "";

						if (begIndex != -1) {
							tempsuffix = filePath.substring(begIndex + 1, filePath.length());
							if (tempsuffix.equals(suffix)) {
								try {
									// BufferedImage image = ImageIO.read(f);
									lstFileNames.add(f);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					} else {
						// lstFileNames.add(filePath);
					}
				}
				return lstFileNames;
	}

	private static List<File> listFile(List<File> lstFileNames, File f, String suffix, boolean isdepth) {
		// 若是目录, 采用递归的方法遍历子目录
		HashSet<String> ignores = Tools.getCompressedPNGName();
		if (f.isDirectory()) {
			File[] t = f.listFiles();

			for (int i = 0; i < t.length; i++) {
				if (isdepth || t[i].isFile()) {
					listFile(lstFileNames, t[i], suffix, isdepth);
				}
			}
		} else {
			String filePath = f.getAbsolutePath();
			if (!suffix.equals("")) {
				int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
				String tempsuffix = "";

				if (begIndex != -1) {
					tempsuffix = filePath.substring(begIndex + 1, filePath.length());
					if (tempsuffix.equals(suffix)) {
						try {
							// BufferedImage image = ImageIO.read(f);
							if (isSelected(f.length())&&!ignores.contains(f.getName())) {
								lstFileNames.add(f);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} else {
				// lstFileNames.add(filePath);
			}
		}
		return lstFileNames;
	}
	
	private static boolean isSelected(long l) {
		return l > SIZE_LIMIT && l <= MAX_LIMIT;
	}
	
	static int m = 20; // smaller than m KB
	private static final int MAX_LIMIT = 1024 * m; 												// root
	static int n = 10; // bigger than n KB
	private static final int SIZE_LIMIT = 1024 * n; // image larger than 10K
	
	
	public static HashSet<String> getCompressedPNGName(){
		HashSet<String> name = new HashSet<>();
		File  f = new File("./compressedPNG");
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine())!=null){
//				System.out.println(line);
				String path = line.split(" size:")[0];
				String[] tmp = path.split("/");
				String filename = tmp[tmp.length-1];
//				map.put(filename, path);
				name.add(filename);
//				System.out.println(filename);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}

}
