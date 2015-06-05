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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RipJavaHanzi {
	private static final boolean TEST_PRINT_PATH = false; 
	
	/**
	 * Main Entry
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: RipJavaHanzi [path], now use default path: **input**");
			args = new String[]{"input"};
//			return;
		}
		ArrayList<String> fileNames = new ArrayList<String>();
		ListDir(fileNames, args[0]);
		Iterator<String> it = fileNames.iterator();
		Set<String> output = new HashSet<String>();
		while (it.hasNext()) {
			String fileName = it.next();
			if (TEST_PRINT_PATH) {
				System.out.println(fileName);
			} else {
				ScanFile(fileName, output);
			}
		}
		List<String> outputList = new ArrayList<String>();
		outputList.addAll(output);
		Collections.sort(outputList, new Comparator<String>() {
			@Override
			public int compare(String arg0, String arg1) {
				int ret = 0;
				if (arg0 == null) {
					ret = -1;
				} else if(arg1 == null) {
					ret = 1;
				} else {
					if (arg0.length() < arg1.length()) {
						ret = -1;
					} else if (arg0.length() > arg1.length()) {
						ret = 1;
					} else {
						ret = arg0.compareTo(arg1);
					}
				}
				return -ret;
			}
		});
		printHanzi("hanzi.txt", outputList);
	}

	private static void ScanFile(String filename, Set<String> output) {
		InputStream istr = null;
		InputStreamReader reader = null;
		BufferedReader ibuf = null;
		try {
			istr = new FileInputStream(filename);
			reader = new InputStreamReader(istr, "utf-8");
			ibuf = new BufferedReader(reader);
			String line = null;
			while ((line = ibuf.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("/*") || line.startsWith("//")) {
					continue;
				}
				int lastPos = -1;
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < line.length(); i++) {
					char ch = line.charAt(i);
					if (isChinese(ch)) { //((int)ch > 255 || (int)ch < 0)
						if (i == lastPos + 1) {
							sb.append(ch);
						} else {
							if (sb.length() > 0) {
								output.add(sb.toString());
							}
							sb.setLength(0);
							sb.append(ch);
						}
						lastPos = i;
					}
				}
				if (sb.length() > 0) {
					output.add(sb.toString());
				}
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
	}

	private static void printHanzi(String filename, List<String> outputList) {
		OutputStream ostr = null;
		OutputStreamWriter writer = null;
		BufferedWriter obuf = null;
		try {
			ostr = new FileOutputStream(filename);
			writer = new OutputStreamWriter(ostr, "utf-8");
			obuf = new BufferedWriter(writer);
			for (String str : outputList) {
				System.out.println(str);
				System.out.println(Native2AsciiUtil.native2Ascii(str));
				obuf.write(str);
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
	
	/**
	 * @see http://www.cnblogs.com/jinc/archive/2013/02/26/2933766.html
	 * @param c
	 * @return
	 */
	private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
}
