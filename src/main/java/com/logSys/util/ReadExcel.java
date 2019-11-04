package com.logSys.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.logSys.entity.All_log;
import com.logSys.entity.Log_source;

/**  
 * @author liuchj
 * @version 1.0    
 */  
public class ReadExcel {  
    //总行数  
    private int totalRows = 0;    
    //总列数  
    private int totalColumns = 0;   
    //错误信息接收器  
    private String errorMsg;  
    //构造方法  
    public ReadExcel(){}  
    //获取总行数  
    public int getTotalRows()  { return totalRows;}   
    //获取总列数  
    public int getTotalCells() {  return totalColumns;}   
    //获取错误信息  
    public String getErrorInfo() { return errorMsg; }    
      
  /** 
   * 读EXCEL文件，获取信息集合 
   * @param fielName 
   * @return 
   */  
    public List<All_log> getExcelInfo(MultipartFile mFile) {  
        String fileName = mFile.getOriginalFilename();//获取文件名
        List<All_log> logList = null;
        try {  
            if (!validateExcel(fileName)) {// 验证文件名是否合格  
                throw new Exception();  
            }  
            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本  
            if (isExcel2007(fileName)) {  
                isExcel2003 = false;  
            }  
            logList = createExcel(mFile.getInputStream(), isExcel2003);  
        } catch (Exception e) {  
            e.printStackTrace(); 
            errorMsg = "导入失败：文件格式错误或者Excel内容数据格式不符合上传格式";
        }  
        return logList;  
    }  
    
  /** 
   * 根据excel里面的内容读取客户信息 
   * @param is 输入流 
   * @param isExcel2003 excel是2003还是2007版本 
   * @return 
   * @throws IOException 
   */  
    public List<All_log> createExcel(InputStream is, boolean isExcel2003) {  
    	List<All_log> logList = new ArrayList<All_log>();;
    	Workbook wb = null;
    	try{               
            if (isExcel2003) {// 当excel是2003时,创建excel2003  
                wb = new HSSFWorkbook(is);   //HSSFWorkbook是2003对象
            } else {// 当excel是2007时,创建excel2007  
                wb = new XSSFWorkbook(is);  //XSSFWorkbook是2007对象
            }  
            readExcelValue(wb,logList,0);  //读取Excel里面客户的信息  
        } catch (IOException e) {  
            e.printStackTrace();
            errorMsg = "导入失败：文件格式错误或者Excel内容数据格式不符合上传格式";
             
        }finally {
			try {
				if(is != null) {
					is.close();
				}
				if(wb != null) {
					wb.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
        return logList;  
    }  
    
  /** 
   * 读取Excel里面客户的信息 
   * @param wb 
   * @return 
   */  
    private void readExcelValue(Workbook wb,List<All_log> logList,int shellIndex) {  
    	//如果当前页数大于等于shell的总页数，递归结束
    	if(shellIndex >= wb.getNumberOfSheets()) return;
    	// 得到第一个shell  
        Sheet sheet = wb.getSheetAt(shellIndex);
        
        //if(sheet == null) return;
        // 得到Excel的行数  
        this.totalRows = sheet.getPhysicalNumberOfRows();  
        // 得到Excel的列数(前提是有行数)  
        if (totalRows > 1 && sheet.getRow(0) != null) {  
            this.totalColumns = sheet.getRow(0).getPhysicalNumberOfCells();  
        }    
        // 循环Excel行数  
        for (int r = 1; r < totalRows; r++) {  
            Row row = sheet.getRow(r);  
            if (row == null){  
                continue;  
            }  
            All_log al = new All_log();  
            // 循环Excel的列  
            for (int c = 0; c < this.totalColumns; c++) {  
                Cell cell = row.getCell(c);  
                if (null != cell) {  
                    if (c == 0) {  
                        al.setId(0); //设置id为零，防止插入数据库时出现主键重复
                    } else if (c == 1) {  
                        Log_source ls = new Log_source();
                        ls.setSource_name(cell.getStringCellValue());
                        al.setLs(ls);
                    } else if (c == 2){  
                        al.setLog_type(cell.getStringCellValue()); 
                    }else if(c == 3) {
                    	al.setOperator(cell.getStringCellValue());
                    }else if(c == 4) {
                    	al.setContent(cell.getStringCellValue());
                    }else if(c == 5) {
                    	al.setRemarks(cell.getStringCellValue());
                    }else if(c == 6) {
                    	try {
                    		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cell.getStringCellValue());
                    		al.setLog_date(date.getTime());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							errorMsg = "导入失败：文件格式错误或者Excel内容数据格式不符合上传格式";
							 
						}
                    	
                    }
           
                }  
            }  
            // 添加到list  
            logList.add(al);  
        }
        readExcelValue(wb, logList, shellIndex+1);
    }  
      
    /** 
     * 验证EXCEL文件 
     *  
     * @param filePath 
     * @return 
     */  
    public boolean validateExcel(String filePath) {  
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {  
            errorMsg = "导入失败：文件格式错误或者Excel内容数据格式不符合上传格式";  
            return false;  
        }  
        return true;  
    }  
     
    //.xls是2003之前的格式，.xlsx是2003以后格式
    // @描述：是否是2003的excel，返回true是2003   
    public static boolean isExcel2003(String filePath)  {    
         return filePath.matches("^.+\\.(?i)(xls)$");    
     }    
     
    //@描述：是否是2007的excel，返回true是2007   
    public static boolean isExcel2007(String filePath)  {    
         return filePath.matches("^.+\\.(?i)(xlsx)$");    
     }    
}  
