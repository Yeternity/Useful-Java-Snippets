/*******************************************************************************
 * 复制PDF里的文字时，有时会每行结束会有多余的换行符，该代码批量将换行符去掉
 * 
 * 缺点：无法分辨一段的结束，因此段尾的换行符也会被去掉
 * 
 * 运行环境：
 *   Eclipse IDE for RCP and RAP Developers
 *   Version: 2019-12 (4.14.0)
 *   Build id: 20191212-1212
 *   OS: Windows 10, v.10.0, x86_64 / win32
 *   Java version: 1.8.0_241
 * 
 * 作者：Yeternity
 * 时间：2021/01/30
 * 文件编码格式：UTF-8
 *******************************************************************************/
package yeternity.java.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 文件编码格式转换，同时将Tab转换为4个空格，换行符可以选择"\n"(Unix)或"\r\n"(Windows)
 * 
 * @since 2020/02/06
 * @author Yeternity
 */
public class FileCharsetConversion2 {
    
    // 配置信息
    
    /** 选择要转换的目录 */
    private static String directoryToBeConverted = "C:\\Users\\yeternity\\Desktop\\test";
    
    /** 选择要转换的文件的后缀名，可以选择一个{".java"}，也可以选择多个{".java", ".c"} */
    private static String[] fileSuffix = {".txt"};
    
    /** 原文件编码格式 
     * 
     * @see #printAvailableCharsets()
     */
    private static String srcEncode = "GBK";
    
    /** 目标编码格式 
     * 
     * @see #printAvailableCharsets()
     */
    private static String destEncode = "GBK";
    
    /** 使用的换行符格式"\n"(Unix)或"\r\n"(Windows) */
    private static String lineDelimiter = "\r\n";
    
    // 非配置变量
    
    /** 需要转换编码格式的文件 */
    private static  ArrayList<File> fileArray = new ArrayList<File>();
    
    // 主函数
    public static void main( String[] args ) throws Exception {
        // 文件编码格式转换
        trans(directoryToBeConverted);
        System.out.println("Conversion Done");
        //打印支持的字符集
        //printAvailableCharsets();
        
        //open the new file
        try {
            Runtime.getRuntime().exec("cmd /c C:\\Users\\yeternity\\Desktop\\test\\ttt.txt /f");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * 将指定目录内的文件（指定文件后缀，如.java)从原编码格式转换为目标编码格式
     * 
     * @param directoryPath 需要转换的文件或文件夹(如： D:\\test)
     * @throws Exception
     * @since 2020/02/06
     * @author Yeternity
     */
    public static void trans(String directoryPath) throws Exception {
        getFileClass(new File(directoryPath));
        for (File file : fileArray) {
            transEncode(file.getAbsolutePath());
        }
    }
    
    /**
     * 遍历指定目录下的指定后缀的文件
     * 
     * @param directoryInstance 指定目录的实例
     * @since 2020/02/06
     * @author Yeternity
     */
    public static void getFileClass(File directoryInstance){
        File[] files = directoryInstance.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                getFileClass(f);
            } else {
                for (String s : fileSuffix) {
                    if (f.getName().endsWith(s)) {
                        System.out.println(f.getAbsolutePath());
                        fileArray.add(f);
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * 将指定的文件，从编码格式srcEncode转为编码格式destEncode（同时也会将文件中所有
     * 的Tab转换为4个空格，换行符改成"\n"(Unix)或者"\r\n"(Windows)
     * 
     * @param filePath 需要转换编码的文件路径(如:D:\\test\\Test.java)
     * @throws Exception
     * @since 2020/02/06
     * @author Yeternity
     */
    public static void transEncode(String filePath) throws Exception {
        // 以GBK格式，读取文件
        FileInputStream fis = new FileInputStream(filePath);
        InputStreamReader isr = new InputStreamReader(fis, srcEncode);
        BufferedReader br = new BufferedReader(isr);
        
        String str = null;
        // 创建StringBuffer字符串缓存区
        StringBuffer sb = new StringBuffer();
        // 通过readLine()方法遍历读取文件
        while ((str = br.readLine()) != null) {
            // 将所有Tab转换为4个空格（注字符串中的Tab也会被转换）
//            if (str.indexOf("\t") != -1) {
//                str = str.replaceAll("\t", "    ");
//            }
            // 使用readLine()方法无法进行换行，需要手动在原本输出的字符串后面加"\n"(Unix)或"\r\n"(Windows)
            str += " ";
            sb.append(str);
        }
        String str2 = sb.toString();
        
        // 以UTF-8格式写入文件，false代表不追加直接覆盖，true代表追加文件
        FileOutputStream fos = new FileOutputStream(filePath, false);
        OutputStreamWriter osw = new OutputStreamWriter(fos, destEncode);
        osw.write(str2);
        osw.flush();
        
        osw.close();
        fos.close();
        br.close();
        isr.close();
        fis.close();
    }
    
    /**
     * 打印出所有{@link java.nio.charset.Charset charset}支持的字符集
     * 
     * @since 2020/02/06
     * @author Yeternity
     */
    public static void printAvailableCharsets() {
        Map<String , Charset> map = Charset.availableCharsets();
        Set<Entry<String , Charset>> set = map.entrySet();
        for (Entry<String , Charset> entry : set) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }
}