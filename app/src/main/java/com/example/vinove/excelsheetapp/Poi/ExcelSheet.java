package com.example.vinove.excelsheetapp.Poi;

import android.content.Context;
import android.util.Log;

import com.example.vinove.excelsheetapp.Model.PaperSheet;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelSheet {

    private Context context;
    private InputStream inputStream;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private PaperSheet[] ps;

    public ExcelSheet(Context context){
        this.context=context;
    }

    public PaperSheet[] readExcelSheet(InputStream inputStream ){

        boolean result=false;
        try {

            workbook=new XSSFWorkbook(inputStream);
            sheet=workbook.getSheetAt(0);
            int rows=sheet.getPhysicalNumberOfRows();
            ps=new PaperSheet[rows];
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            for(int i=1;i<rows;i++){
                Row row=sheet.getRow(i);
                ps[i]=new PaperSheet();
                int cellsCount = row.getPhysicalNumberOfCells();
                for (int c = 0; c<cellsCount; c++) {
                    String value = getCellAsString(row, c, formulaEvaluator);
                    /*String cellInfo = "row:"+i+"; column:"+c+"; value:"+value;*/
                    switch(c){
                        case 0:
                            ps[i].setQuestions(value);
                            break;
                        case 1:
                            ps[i].setResponseTypes(value);
                            break;
                        default:
                            ps[i].setValidRespList(value);
                    }
                }
                Log.d("ps",ps[i].toString());
                System.out.println(ps[i]);
            }
            return ps;
        }catch(Exception e){
            Logger.getLogger(ExcelSheet.class.getName()).log(Level.SEVERE, null, e);
            result=false;
        }
        finally{
            inputStream=null;
        }
        return ps;
    }


    public void writeExcelSheet(String fn){

        XSSFWorkbook workbook=new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("mysheet"));
        sheet.setDefaultColumnWidth(1000);

        for (int i=0;i<10;i++) {
            Row row = sheet.createRow(i);
            switch(row.getRowNum()){
                case 0:
                    for(int j=1;j<10;j++){
                        Cell cell=row.createCell(j-1);
                        XSSFCellStyle style2 = workbook.createCellStyle();
                        XSSFFont font = workbook.createFont();
                        font.setFontHeightInPoints((short)11);
                        font.setFontName("custom_sheet");
                        font.setItalic(true);
                        font.setColor(HSSFColor.GREY_50_PERCENT.index);
                        style2.setFont(font);
                        style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                        style2.setVerticalAlignment(
                                XSSFCellStyle.VERTICAL_CENTER);
                        cell.setCellValue(j);
                        cell.setCellStyle(style2);

                    }
                    break;
                default:{

                }
            }
        }
        String outFileName = fn;
        try {
            File outFilePath = new File(outFileName);
            outFilePath.mkdirs();
            File outFile=new File(outFilePath,"Output.xlsx");
            OutputStream outputStream = new FileOutputStream(outFile.getAbsolutePath());
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    protected String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);

            if(cellValue!=null){
                switch (cellValue.getCellType()) {

                    case Cell.CELL_TYPE_NUMERIC:
                        double numericValue = cellValue.getNumberValue();
                        value = ""+numericValue;
                        break;
                    case Cell.CELL_TYPE_STRING:

                        value = ""+cellValue.getStringValue();
                        break;
                    default:
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return value;
    }
}
