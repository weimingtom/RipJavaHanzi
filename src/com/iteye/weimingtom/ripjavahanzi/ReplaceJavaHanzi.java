package com.iteye.weimingtom.ripjavahanzi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReplaceJavaHanzi {
	/**
	 * Main Entry
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: ReplaceJavaHanzi [path], now use default path: **output**");
			args = new String[]{"output"};
//			return;
		}
		ArrayList<String> fileNames = new ArrayList<String>();
		ListDir(fileNames, args[0]);
		List<String> replaceList = readReplaceFile("hanzi.txt");
//		for (String key : replaceList) {
//			System.out.println(key);
//		}
		Iterator<String> it = fileNames.iterator();
		while (it.hasNext()) {
			String fileName = it.next();
			System.out.println(fileName);
			ScanFile(fileName, replaceList);
		}
	}

	private static void ScanFile(String filename, List<String> replaceList) {
		InputStream istr = null;
		InputStreamReader reader = null;
		BufferedReader ibuf = null;
		List<String> output = new ArrayList<String>();
		try {
			istr = new FileInputStream(filename);
			reader = new InputStreamReader(istr, "utf-8");
			ibuf = new BufferedReader(reader);
			String line = null;
			while ((line = ibuf.readLine()) != null) {
				for (String str : replaceList) {
					line = line.replace(Native2AsciiUtil.native2Ascii(str), str);
				}
				output.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ibuf != null) {
				try {
					ibuf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (istr != null) {
				try {
					istr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		OutputStream ostr = null;
		OutputStreamWriter writer = null;
		BufferedWriter obuf = null;
		try {
			ostr = new FileOutputStream(filename);
			writer = new OutputStreamWriter(ostr, "utf-8");
			obuf = new BufferedWriter(writer);
			for (String line : output) {
				obuf.write(line);
				obuf.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (obuf != null) {
				try {
					obuf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ostr != null) {
				try {
					ostr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static List<String> readReplaceFile(String filename) {
		InputStream istr = null;
		InputStreamReader reader = null;
		BufferedReader ibuf = null;
		List<String> output = new ArrayList<String>();
		try {
			istr = new FileInputStream(filename);
			reader = new InputStreamReader(istr, "utf-8");
			ibuf = new BufferedReader(reader);
			String line = null;
			while ((line = ibuf.readLine()) != null) {
				output.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ibuf != null) {
				try {
					ibuf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (istr != null) {
				try {
					istr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return output;
	}

	private static void printHanzi(PrintStream out, List<String> outputList) {
		for (String str : outputList) {
			System.out.println(str);
			System.out.println(Native2AsciiUtil.native2Ascii(str));
			out.println(str);
		}
	}

	private static void ListDir(List<String> fileNames, String dirName) {
		ListFiles(fileNames, new File(dirName));
	}

	private static void ListFiles(List<String> fileNames, File dir) {
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return;
		}
		String separator = System.getProperty("file.separator");
		String[] files = dir.list();
		for (int i = 0; i < files.length; i++) {
			File file = new File(dir, files[i]);
			String fileName = dir + separator + file.getName();
			if (file.isFile()) {
				//System.out.println(fileName + "\t" + file.length());
				if (fileName != null && fileName.endsWith(".java")) {
					fileNames.add(fileName);
				}
			} else {
				// System.out.println(fileName + "\t<dir>");
				ListFiles(fileNames, file);
			}
		}
	}
}
