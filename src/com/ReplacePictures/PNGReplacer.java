package com.ReplacePictures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.Tools;
import com.DamonLiu.PNGSelector;


/**
 * 
 * @author damonliu
 * @version 将UI返回的图片替换回工程中
 */
public class PNGReplacer {
	static String piclistname = "./result.txt";
	static String newpicpath = "/Users/damonliu/Pictures/20KB";
	public static void main(String args[]){
		HashMap<String, String> map = new HashMap<String,String>();
		File  f = new File(piclistname);
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine())!=null){
//				System.out.println(line);
				String path = line.split(" size:")[0];
				String[] tmp = path.split("/");
				String filename = tmp[tmp.length-1];
				map.put(filename, path);
//				System.out.println(filename);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<File> newpics = Tools.getListFilesWithoutLimit(newpicpath, "png", true);
		int counter = 0;
		for(File newpic : newpics){
			String filename = newpic.getName();
			String toPath = map.get(filename);
			String fp = newpic.getAbsolutePath();
			String tp = "/Users/damonliu/Documents/Github/"+toPath;
			boolean tag = Tools.copyFile(fp,tp ,true);
			if(tag){
				counter++;
			}
		}
		System.out.println("success copy:"+counter+" png files");
	}
}
