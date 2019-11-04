package com.logSys.util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;

import com.logSys.entity.All_log;







public class DB2xls {

	/**
	 * 将数据库dname中的表名为tname的数据表写入Excel表格
	 * @param dname
	 * @param tname
	 */
	
	public DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public  void writeDbtoExcel(HSSFWorkbook wb,String dname,String tname,List<All_log> list,int page){
		//String path="E:/axls/"+tname+".xls"; //固定路径
		
		
		try {
			
			//Connection con;
			//con = dataSource.getConnection();
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dname+"?useUnicode=true&characterEncoding=utf-8","root","root");
			//Statement st=(Statement) con.createStatement();
			//String sql="select * from "+tname;
			//ResultSet rs=st.executeQuery(sql);
			//ResultSetMetaData rsmd=rs.getMetaData();//得到结果集的字段名
			//int c=rsmd.getColumnCount();//得到数据表的结果集的字段的数量
			
			//调整单元格宽度
			HSSFSheet sheet = wb.createSheet("表"+page);
			for (int i = 0; i < 9; i++) {
				sheet.setAutobreaks(true); 
				sheet.setColumnWidth(i, 5000);//设定单元格长度 
				sheet.autoSizeColumn((short) i);//自动根据长度调整单元格长度 				   
	        }
			
			
			
			HSSFCellStyle style = wb.createCellStyle();  
			  /**
	         * 单元格 样式
	         */						
			CellStyle cellStyle = wb.createCellStyle();
			

			 
	        cellStyle.setBorderTop(BorderStyle.MEDIUM);
	        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
	        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
	        cellStyle.setBorderRight(BorderStyle.MEDIUM);
	        cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
	        cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
	        cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
	        cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
	       // cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
	        //cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 上下居中

	        //zitiyangshi
	        HSSFFont font  = wb.createFont();     
	        font.setFontHeightInPoints((short) 11);//字号       
	       // font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//加粗       
	        style.setFont(font);    
			
			
			String[] rowname = {"ID","来源","类型","操作人","内容","备注","日期"}; 
	        //设置第一行的表头 
			//生成表单的第一行，即表头
			HSSFRow row0 = sheet.createRow(0);//创建第一行
			for(int i=0;i<rowname.length;i++){
				HSSFCell cel=row0.createCell(i);//创建第一行的第i列
				cel.setCellType(CellType.STRING);
				cel.setCellStyle(cellStyle); //设置样式
				
				cel.setCellValue(rowname[i]); //设置表头每列的名称

			}
			//将数据表中的数据按行导入进Excel表中
			int r=1;
			boolean flag;
			int max;
			//判断一页的行数是否超过65534行（exel限制每页的最大行数为65536）
			if((list.size() - (page-1)*65534)>65534) {				
				max = page*65534;
				flag = true;
			}else {
				max = list.size();
				flag = false;
			}
			
			//创建非第一行的其他行
			for(int i=(page-1)*65534;i<max;i++){//仍然是c列，导入第r行的第i列
				Object[] obj = {list.get(i).getId(),list.get(i).getLs().getSource_name(),list.get(i).getLog_type(),list.get(i).getOperator(),list.get(i).getContent(),list.get(i).getRemarks(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(list.get(i).getLog_date()))};
				HSSFRow row = sheet.createRow(r++);
				for(int j=0;j<obj.length;j++) {
					HSSFCell cel = row.createCell(j);
					
					cel.setCellType(CellType.STRING);
					cel.setCellStyle(cellStyle);
					if (obj[j] instanceof String) {
						if(obj[j] != null)
							cel.setCellValue((String)obj[j]);
					}else {
						if(obj[j] != null) cel.setCellValue((int)obj[j]);
						else cel.setCellValue("");
							
					}
					
				}
			}	
			if(flag) this.writeDbtoExcel(wb,"logsys", "all_log",list,page+1);
			else return;
					
			//设置格式
			//cel.setCellValue(rs.getString(i+1));
	
			
			//用文件输出流类创建名为table的Excel表格
			//FileOutputStream out=new FileOutputStream(path);
			//book.write(out);//将HSSFWorkBook中的表写入输出流中
			//book.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
