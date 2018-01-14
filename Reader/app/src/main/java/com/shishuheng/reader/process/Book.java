package com.shishuheng.reader.process;

import com.shishuheng.reader.datastructure.LineAndBytesCount;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by shishuheng on 2018/1/8.
 */

public class Book extends Thread{
    //文件路径
    private File filePath;
    //一行展示的字数
    private int lineCharacterNumber;
    //总共可显示的行数
    private int lineTotalNumber;
    //处理后的文本
    private ArrayList<String> book;
    //上次读到的点
    private long readPointer = 0;
    //随机读取
    RandomAccessFile raf;
    //整个屏幕字数
    public ArrayList<String> bookFullScreen;
    //读到的行数
    public int position = 0;
    //记录上一次翻页操作 1:nextPage 2:lastPage 0:unknown
    private int pagging = 1;

    public Book(File path, int lineTotalNumber, int lineCharacterNumber, long readPointer) {
        try {
            this.lineCharacterNumber = lineCharacterNumber;
            this.lineTotalNumber = lineTotalNumber;
            filePath = path;
            bookFullScreen = new ArrayList<>();
            book = new ArrayList<>();
            raf = new RandomAccessFile(filePath, "r");
            setReadPointer(readPointer);
        } catch (Exception e) {
            bookFullScreen.add("读取文件失败!");
        }
    }

    @Override
    public void run() {
//        processBook();
    }

    public String nextPage() {
        return readByte(readPointer);
    }

    public String lastPage() {
        if (readPointer < 860 && readPointer > 0) {
            String res = readByte(0);
            readPointer = 0;
            return res;
        }
        return reverseReadByte(readPointer);
    }

    /*
    public void openBookFile(int length) {
        try {
//            FileReader fr = new FileReader(filePath);
//            此处参考https://www.cnblogs.com/aipan/p/6561279.html
///            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "GBK"));
///            String line = "";
///            while ((line = br.readLine()) != null) {
///                bookAllLine.add(line);
///            }
            bookAllLine.clear();
            book.clear();
            readNext(length);
            splitBookLine(0, lineTotalNumber);
        }catch (Exception e) {
            bookAllLine.add("读取文件失败!");
        }
    }

    public String test() {
        try {
            byte[] bytes = new byte[2048];
            raf.seek(0);
            raf.read(bytes);
            String result = new String(bytes, "GBK");
            return result;
        } catch (Exception e) {
            return null;
        }
    }
    */

    //通过byte读取文件
    public String readByte(long pointer) {
        bookFullScreen.clear();
        try {
            //每行字数
            int countC = 0;
            //行数
            int lineC = 0;
            //读取的byte数
            int byteC = 0;
            StringBuilder sb = new StringBuilder("");
            byte[] bytes = new byte[2];
            raf.seek(pointer);
            while (lineC < lineTotalNumber) {
                if (countC < lineCharacterNumber) {
                    raf.read(bytes);
                    if (bytes[0] == (byte)'\r' && bytes[1] == (byte)'\n') {
                        sb.append('\n');
                        byteC += 2;
                        countC = 0;
                        lineC++;
                        bookFullScreen.add(sb.toString());
                        sb = new StringBuilder();
                    } else if (bytes[0] == (byte)'\n') {
                        sb.append('\n');
                        byteC++;
                        lineC++;
                        countC = 0;
                        raf.seek(raf.getFilePointer()-1);
                        bookFullScreen.add(sb.toString());
                        sb = new StringBuilder();
                    } //else if (bytes[0] == (byte)' ' || bytes[0] == (byte)'`' || bytes[0] == (byte)'~' || bytes[0] == (byte)'1' || bytes[0] == (byte)'2' || bytes[0] == (byte)'3' || bytes[0] == (byte)'4' || bytes[0] == (byte)'5' || bytes[0] == (byte)'7' || bytes[0] == (byte)'8' || bytes[0] == (byte)'9' || bytes[0] == (byte)'0' || bytes[0] == (byte)'!' || bytes[0] == (byte)'@' || bytes[0] == (byte)'#' || bytes[0] == (byte)'$' || bytes[0] == (byte)'%' || bytes[0] == (byte)'^' || bytes[0] == (byte)'&' || bytes[0] == (byte)'*' || bytes[0] == (byte)'(' || bytes[0] == (byte)')' || bytes[0] == (byte)'-' || bytes[0] == (byte)'_' || bytes[0] == (byte)'=' || bytes[0] == (byte)'+' || bytes[0] == (byte)'[' || bytes[0] == (byte)'{' || bytes[0] == (byte)']' || bytes[0] == (byte)'}' || bytes[0] == (byte)'|' || bytes[0] == (byte)'\\' || bytes[0] == (byte)';' || bytes[0] == (byte)':' || bytes[0] == (byte)'\'' || bytes[0] == (byte)'"' || bytes[0] == (byte)'<' || bytes[0] == (byte)',' || bytes[0] == (byte)'>' || bytes[0] == (byte)'.' || bytes[0] == (byte)'?' || bytes[0] == (byte)'/') {
                        else if (bytes[0] >=0 && bytes[0] < 128) {
                        sb.append((char)bytes[0]);
                        byteC++;
                        raf.seek(raf.getFilePointer()-1);
                    } else {
                        sb.append(new String(bytes, "GBK"));
                        byteC += 2;
                        countC++;
                    }
                } else {
                    lineC++;
                    bookFullScreen.add(sb.toString());
                    sb = new StringBuilder();
                    countC = 0;
                }
            }
            String result = "";
            for (int i = 0; i < bookFullScreen.size(); i++) {
                result += bookFullScreen.get(i);
            }
            readPointer += byteC;
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    //通过byte读取文件
    public String reverseReadByte(long pointer) {
        bookFullScreen.clear();
        try {
            //每行字数
            int countC = 0;
            //行数
            int lineC = 0;
            //读取的byte数
            int byteC = 0;
            StringBuilder sb = new StringBuilder("");
            byte[] bytes = new byte[2];
            long position = 0;
            while (lineC < lineTotalNumber) {
                position = pointer-byteC-2;
                if (position >= 0) {
                    raf.seek(position);
                    if (countC < lineCharacterNumber) {
                        raf.read(bytes);
                        if (bytes[0] == (byte) '\r' && bytes[1] == (byte) '\n') {
                            sb.append('\n');
                            byteC += 2;
                            countC = 0;
                            lineC++;
                            bookFullScreen.add(sb.toString());
                            sb = new StringBuilder();
                        } else if (bytes[0] == (byte) '\n') {
                            sb.append('\n');
                            byteC++;
                            lineC++;
                            countC = 0;
                            raf.seek(raf.getFilePointer() - 1);
                            bookFullScreen.add(sb.toString());
                            sb = new StringBuilder();
                        } //else if (bytes[0] == (byte)' ' || bytes[0] == (byte)'`' || bytes[0] == (byte)'~' || bytes[0] == (byte)'1' || bytes[0] == (byte)'2' || bytes[0] == (byte)'3' || bytes[0] == (byte)'4' || bytes[0] == (byte)'5' || bytes[0] == (byte)'7' || bytes[0] == (byte)'8' || bytes[0] == (byte)'9' || bytes[0] == (byte)'0' || bytes[0] == (byte)'!' || bytes[0] == (byte)'@' || bytes[0] == (byte)'#' || bytes[0] == (byte)'$' || bytes[0] == (byte)'%' || bytes[0] == (byte)'^' || bytes[0] == (byte)'&' || bytes[0] == (byte)'*' || bytes[0] == (byte)'(' || bytes[0] == (byte)')' || bytes[0] == (byte)'-' || bytes[0] == (byte)'_' || bytes[0] == (byte)'=' || bytes[0] == (byte)'+' || bytes[0] == (byte)'[' || bytes[0] == (byte)'{' || bytes[0] == (byte)']' || bytes[0] == (byte)'}' || bytes[0] == (byte)'|' || bytes[0] == (byte)'\\' || bytes[0] == (byte)';' || bytes[0] == (byte)':' || bytes[0] == (byte)'\'' || bytes[0] == (byte)'"' || bytes[0] == (byte)'<' || bytes[0] == (byte)',' || bytes[0] == (byte)'>' || bytes[0] == (byte)'.' || bytes[0] == (byte)'?' || bytes[0] == (byte)'/') {
                        else if (bytes[1] >= 0 && bytes[1] < 128) {
                            sb.append((char) bytes[1]);
                            byteC++;
                            raf.seek(raf.getFilePointer() - 1);
                        } else {
                            sb.append(new String(bytes, "GBK"));
                            byteC += 2;
                            countC++;
                        }
                    } else {
                        lineC++;
                        bookFullScreen.add(sb.toString());
                        sb = new StringBuilder();
                        countC = 0;
                    }
                } else {
                    bookFullScreen.add(sb.toString());
                    break;
                }
            }
            String result = "";
            for (int i = 0; i < bookFullScreen.size(); i++) {
                result += bookFullScreen.get(i);
            }
            readPointer -= (byteC+2);
            StringBuilder stringBuilder = new StringBuilder(result);
            result = new String(stringBuilder.reverse());
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /*
    //加载整本书
    public void processBook() {
        try {
            raf.seek(0);
            String t = new String((raf.readLine().getBytes("ISO8859-1")), "GBK");
            int span;
            while (t != "" && t.length() != 0) {
                String a;
                if (t.length() < lineCharacterNumber) {
                    a = t.substring(0);
                    book.add(a);
                } else {
                    a = t.substring(0, lineCharacterNumber);
                    book.add(a);
                    t = t.substring(lineCharacterNumber);
                }
            }
        } catch (Exception e) {
            book.add("打开书籍失败");
        }
    }

    public void readNext(int length) {
        try {
            String line;
            raf.seek(readPointer);
            pointers = new long[length];
            for (int i = 0; i < length; i++) {
                pointers[i] = raf.getFilePointer();
                line = new String((raf.readLine().getBytes("ISO8859-1")), "GBK");
                if (raf.readByte() == 13)
                    codingStyle = 1;
                raf.seek(raf.getFilePointer() - 1);
                bookAllLine.add(line);
            }
            readPointer = raf.getFilePointer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void splitBookLine(int start, int end) {
        if (end-start > 0 && bookAllLine.size() >= start && bookAllLine.size() != 0 && bookAllLine.get(0) != "读取文件失败!") {
            if (bookAllLine.size() < end)
                end = bookAllLine.size();
            for (int i = start; i < end; i++) {
//                String regex = ".{1,"+lineCharacterNumber+"}";
//                String list[] = bookAllLine.get(i).split(regex);
                int span = lineCharacterNumber;
                String t = bookAllLine.get(i);
//                for (int j = 0; j < list.length; j++) {
//                    book.add(list[j]);
//                }
                while (t != "" && t.length() != 0) {
                    String a;
                    if (t.length() < span) {
                        a = t.substring(0, t.length());
                        book.add(a);
                        if (codingStyle == 1)
                            book.add("\n");
                    } else {
                        a = t.substring(0, span);
                        book.add(a);
                    }
                    t = t.replace(a, "");
                }
            }
            splitPointer = end;
        }
    }

    public synchronized ArrayList<String> getBook() {
        return book;
    }
    */

    public long getReadPointer() {
        return readPointer;
    }

    public void setReadPointer(long readPointer) {
        this.readPointer = readPointer;
    }

    /*
    public void setSplitPointer(long splitPointer) {
        this.splitPointer = splitPointer;
    }

    public long getSplitPointer() {
        return splitPointer;
    }

    public ArrayList<String> getBookAllLine() {
        return bookAllLine;
    }

    public void setBookAllLine(ArrayList<String> bookAllLine) {
        this.bookAllLine = bookAllLine;
    }

    public long[] getPointers() {
        return pointers;
    }

    public long getPointer(long bookFirstLineIndex, long length) {
        String sum = "";
        if (book.size() > bookFirstLineIndex) {
            if (book.size() - length < bookFirstLineIndex) {
                length = book.size() - bookFirstLineIndex;
            }
            for (int k = (int) bookFirstLineIndex; k < length-1; k++) {
                sum += book.get(k);
            }
            return sum.length();
        } else
            return -1;

        int index = bookAllLine.size()-length;
        String line = book.get((int)bookFirstLineIndex);
        for (int i = index; i < bookAllLine.size(); i++) {
            if (bookAllLine.get(i).indexOf(line) >= 0) {
                readPointer = pointers[i-index];
                return readPointer;
            }
        }
    }
    */
}
