package report;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.charts.ChartData;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.charts.XSSFChartLegend;
import testcase.executeTest;

import java.io.*;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ReportWP {

    public ReportWP() {
    }

    public static void CreateWPExcel(String strPath, String strTitle, String strType) {


        int intWidth = 4;
        int intHeight = 25;
        int intSpace = 2;
        int intRowDescription = 2;
        int intMasterRow = intRowDescription + intSpace + 1;
        int intMasterColumn = intSpace - 1;

//        Get All File with specific extention
        File f = new File(strPath);
        if(f!=null){

            File[] matchingFiles = f.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith("jpeg");
//                return name.startsWith("xxx") && name.endsWith("jpeg");
                }
            });
            Arrays.sort(matchingFiles);

            /*Create WP*/
            try {

                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet(strTitle);
                /*HEADER*/
                XSSFFont headerFont = (XSSFFont) wb.createFont();
                headerFont.setBold(true);
                headerFont.setUnderline(XSSFFont.U_SINGLE);
                headerFont.setFontHeightInPoints((short) 24);

                XSSFCellStyle styleHeader = (XSSFCellStyle) wb.createCellStyle();
                styleHeader.setFont(headerFont);

                XSSFCellStyle styleDescription = (XSSFCellStyle) wb.createCellStyle();
                styleDescription.setFillForegroundColor(IndexedColors.BLUE.getIndex());
                styleDescription.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                Cell cellHeader = sheet.createRow(0).createCell(0);
                cellHeader.setCellValue(strTitle);
                cellHeader.setCellStyle(styleHeader);
                /*HEADER*/


                int strCodeDefault = 0;

                for (int i = 0; i < matchingFiles.length; i++) {
                    String[] strCode = matchingFiles[i].getName().split("_");
                    int intCode = Integer.parseInt((strCode[0]).replaceAll("^0+",""));
                    if (intCode > strCodeDefault) {


                        if (strType.equalsIgnoreCase("HORIZONTAL")) {
                            Row rowDescription = sheet.createRow(intRowDescription);
                            Cell cellDescription = rowDescription.createCell(0);

                            for (int j = 0; j <= 500; j++)
                                rowDescription.createCell(j).setCellStyle(styleDescription);
                            cellDescription.setCellValue("Data ke-" + intCode);
//                        cellDescription.setCellValue(hasListDescription.get(intCode));
                            /*DESCRIPTION*/
                            if (intCode != 1) {
                                intMasterRow = intRowDescription + intSpace + 1;
                                intMasterColumn = intSpace - 1;
                            }

                            intRowDescription += (intSpace * 2) + 1 + intHeight;
                        } else if (strType.equalsIgnoreCase("VERTICAL")) {
                            /*DESCRIPTION*/


                            if (intCode != 1) {
                                wb.createSheet("WP-" + intCode);
                                sheet = wb.getSheetAt(intCode - 1);
                                cellHeader = sheet.createRow(0).createCell(0);
                                cellHeader.setCellValue(strTitle);
                                cellHeader.setCellStyle(styleHeader);

                                intMasterRow = intRowDescription + intSpace + 1;
                                intMasterColumn = intSpace - 1;
                            } else {
                                wb.setSheetName(intCode - 1, "WP-1");
                            }
                            Row rowDescription = sheet.createRow(intRowDescription);
                            Cell cellDescription = rowDescription.createCell(0);

                            for (int j = 0; j <= 500; j++)
                                rowDescription.createCell(j).setCellStyle(styleDescription);
                            cellDescription.setCellValue("Data ke-" + intCode);
//                        cellDescription.setCellValue(hasListDescription.get(intCode));

                        }
                        strCodeDefault = intCode;

                    }


                    InputStream inputStream = new FileInputStream(matchingFiles[i]);
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
                    inputStream.close();

                    CreationHelper helper = wb.getCreationHelper();
                    Drawing drawing = sheet.createDrawingPatriarch();
                    ClientAnchor anchor = helper.createClientAnchor();

                    //create an anchor with upper left cell _and_ bottom right cell
                    anchor.setCol1(intMasterColumn); //Column B
                    anchor.setRow1(intMasterRow); //Row 3
                    anchor.setCol2(intMasterColumn + intWidth); //Column C
                    anchor.setRow2(intMasterRow + intHeight); //Row 4
                    if (strType.equalsIgnoreCase("HORIZONTAL")) {
                        intMasterColumn += intWidth + intSpace;
                    } else {
                        intMasterRow += intHeight + intSpace;
                    }

                    //Creates a picture
                    drawing.createPicture(anchor, pictureIdx);
                }


                //Write the Excel file
                FileOutputStream fileOut = null;
                fileOut = new FileOutputStream(strPath + "WP-" + strTitle + ".xlsx");
                wb.write(fileOut);
                fileOut.close();
            } catch (IOException ioex) {
            }


        }

    }


    public static void CreateReportExcel(String PathReport){

        /*Create WP*/
        try {

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Report");

            /*TITLE*/
            XSSFFont headerTitle = (XSSFFont) wb.createFont();
            headerTitle.setBold(true);
            headerTitle.setFontHeightInPoints((short) 15);
            headerTitle.setColor(IndexedColors.BLACK.getIndex());

            XSSFCellStyle styleTitle = (XSSFCellStyle) wb.createCellStyle();
            styleTitle.setFont(headerTitle);

            LocalDate today = LocalDate.now();
            String formattedDate = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));

            Row RowTitle = sheet.createRow(0);
            Cell title = RowTitle.createCell(3);
            title.setCellValue("DAILY REPORT AUTOMATION TESTING BLU "+formattedDate);
            title.setCellStyle(styleTitle);


            /*HEADER*/
            XSSFFont headerFont = (XSSFFont) wb.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            XSSFCellStyle styleHeader = (XSSFCellStyle) wb.createCellStyle();
            styleHeader.setFont(headerFont);
            styleHeader.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            styleHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
            styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
            styleHeader.setBorderBottom(BorderStyle.MEDIUM);
            styleHeader.setBorderLeft(BorderStyle.MEDIUM);
            styleHeader.setBorderRight(BorderStyle.MEDIUM);
            styleHeader.setBorderTop(BorderStyle.MEDIUM);
            styleHeader.setAlignment(HorizontalAlignment.CENTER);


            Row RowHeader = sheet.createRow(2);
            Cell cell1 = RowHeader.createCell(1);
            cell1.setCellValue("NO");
            cell1.setCellStyle(styleHeader);

            Cell cell2 = RowHeader.createCell(2);
            cell2.setCellValue("MENU");
            cell2.setCellStyle(styleHeader);

            Cell cell3 = RowHeader.createCell(3);
            cell3.setCellValue("SCENARIO DESCRIPTION");
            cell3.setCellStyle(styleHeader);

            Cell cell4 = RowHeader.createCell(4);
            cell4.setCellValue("DURATION");
            cell4.setCellStyle(styleHeader);

            Cell cell5 = RowHeader.createCell(5);
            cell5.setCellValue("ERROR DESC");
            cell5.setCellStyle(styleHeader);

            Cell cell6 = RowHeader.createCell(6);
            cell6.setCellValue("PASSED SUB SEC");
            cell6.setCellStyle(styleHeader);

            Cell cell7 = RowHeader.createCell(7);
            cell7.setCellValue("FAILED SUB SEC");
            cell7.setCellStyle(styleHeader);

            Cell cell8 = RowHeader.createCell(8);
            cell8.setCellValue("UNTESTED SUB SEC");
            cell8.setCellStyle(styleHeader);

            Cell cell9 = RowHeader.createCell(9);
            cell9.setCellValue("PRECENTAGE");
            cell9.setCellStyle(styleHeader);

            Cell cell10 = RowHeader.createCell(10);
            cell10.setCellValue("RESULT");
            cell10.setCellStyle(styleHeader);



            Cell cell12 = RowHeader.createCell(12);
            cell12.setCellValue("TOTAL DURATION");
            cell12.setCellStyle(styleHeader);

            Cell cell13 = RowHeader.createCell(13);
            cell13.setCellValue("TOTAL PASSED");
            cell13.setCellStyle(styleHeader);

            Cell cell14 = RowHeader.createCell(14);
            cell14.setCellValue("TOTAL FAILED");
            cell14.setCellStyle(styleHeader);

            Cell cell15 = RowHeader.createCell(15);
            cell15.setCellValue("TOTAL UNTESTED");
            cell15.setCellStyle(styleHeader);

            Cell cell16 = RowHeader.createCell(16);
            cell16.setCellValue("TOTAL PRECENTAGE");
            cell16.setCellStyle(styleHeader);


            /*BODY*/
            XSSFFont BodyFont = (XSSFFont) wb.createFont();
            BodyFont.setFontHeightInPoints((short) 11);
            BodyFont.setColor(IndexedColors.BLACK.getIndex());

            XSSFCellStyle styleBody = (XSSFCellStyle) wb.createCellStyle();
            styleBody.setFont(BodyFont);
            styleBody.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            styleBody.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            styleBody.setRightBorderColor(IndexedColors.BLACK.getIndex());
            styleBody.setTopBorderColor(IndexedColors.BLACK.getIndex());
            styleBody.setBorderBottom(BorderStyle.MEDIUM);
            styleBody.setBorderLeft(BorderStyle.MEDIUM);
            styleBody.setBorderRight(BorderStyle.MEDIUM);
            styleBody.setBorderTop(BorderStyle.MEDIUM);

            XSSFCellStyle styleBodyRed = (XSSFCellStyle) wb.createCellStyle();
            styleBodyRed.setFont(BodyFont);
            styleBodyRed.setFillForegroundColor(IndexedColors.RED.getIndex());
            styleBodyRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleBodyRed.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            styleBodyRed.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            styleBodyRed.setRightBorderColor(IndexedColors.BLACK.getIndex());
            styleBodyRed.setTopBorderColor(IndexedColors.BLACK.getIndex());
            styleBodyRed.setBorderBottom(BorderStyle.MEDIUM);
            styleBodyRed.setBorderLeft(BorderStyle.MEDIUM);
            styleBodyRed.setBorderRight(BorderStyle.MEDIUM);
            styleBodyRed.setBorderTop(BorderStyle.MEDIUM);
            styleBodyRed.setAlignment(HorizontalAlignment.CENTER);
            styleBodyRed.setVerticalAlignment(VerticalAlignment.CENTER);


            XSSFCellStyle styleBodyGreen = (XSSFCellStyle) wb.createCellStyle();
            styleBodyGreen.setFont(BodyFont);
            styleBodyGreen.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            styleBodyGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleBodyGreen.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            styleBodyGreen.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            styleBodyGreen.setRightBorderColor(IndexedColors.BLACK.getIndex());
            styleBodyGreen.setTopBorderColor(IndexedColors.BLACK.getIndex());
            styleBodyGreen.setBorderBottom(BorderStyle.MEDIUM);
            styleBodyGreen.setBorderLeft(BorderStyle.MEDIUM);
            styleBodyGreen.setBorderRight(BorderStyle.MEDIUM);
            styleBodyGreen.setBorderTop(BorderStyle.MEDIUM);
            styleBodyGreen.setAlignment(HorizontalAlignment.CENTER);
            styleBodyGreen.setVerticalAlignment(VerticalAlignment.CENTER);

            Integer Baris = 3;


            Integer TotalPass, TotalFailed, TotalScenario, TotalPrcentage;
            Long GetTotalDuration=0L;

            TotalPass = executeTest.tempInteger.get("glbTotalCasePass");
            TotalFailed = executeTest.tempInteger.get("glbTotalCaseFail");
            TotalScenario = executeTest.tempInteger.get("glbTotalCase");
            if(TotalScenario==0){
                TotalPrcentage = 0;
            }else {
                TotalPrcentage = (TotalPass * 100) / TotalScenario;
            }




            for(int ket = 1; ket <= executeTest.arrReport.size(); ket++){
                Integer isi = executeTest.arrReport.get("data "+ket).size()-1;
                Row RowBody = sheet.createRow(Baris);

                for(int ketIsi = 0; ketIsi <= isi; ketIsi++) {
                    Cell cellBody = RowBody.createCell(ketIsi+1);
                    if(ketIsi+1==4) {
                        if(!executeTest.arrReport.get("data " + ket).get(ketIsi).equalsIgnoreCase("")){
                        String times = convertTime(Long.parseLong(executeTest.arrReport.get("data " + ket).get(ketIsi)));
                        cellBody.setCellValue(times);
                        cellBody.setCellStyle(styleBody);
                        GetTotalDuration = GetTotalDuration + Long.parseLong(executeTest.arrReport.get("data "+ket).get(ketIsi));
                        }else{
                            cellBody.setCellValue("");
                            cellBody.setCellStyle(styleBody);
                        }
                    }else if(ketIsi+1==10){
                        cellBody.setCellValue(executeTest.arrReport.get("data " + ket).get(ketIsi));

                        //Coloring Report
                        //=====================================================================================
                        if (executeTest.arrReport.get("data " + ket).get(ketIsi).equalsIgnoreCase("PASSED")) {
                            cellBody.setCellStyle(styleBodyGreen);
                        } else if (executeTest.arrReport.get("data " + ket).get(ketIsi).equalsIgnoreCase("FAILED")) {
                            cellBody.setCellStyle(styleBodyRed);
                        }
                        //=====================================================================================

                    }else{
                        cellBody.setCellValue(executeTest.arrReport.get("data " + ket).get(ketIsi));
                        cellBody.setCellStyle(styleBody);
                    }


                }
                Baris = Baris + 1;


            }

            String time = convertTime(GetTotalDuration);

            Row getRow = sheet.getRow(3);
            Cell cellTotDu = getRow.createCell(12);
            cellTotDu.setCellValue(time);
            cellTotDu.setCellStyle(styleBody);

            Cell cellTotPass = getRow.createCell(13);
            cellTotPass.setCellValue(TotalPass+" Sub Scenario");
            cellTotPass.setCellStyle(styleBody);

            Cell cellTotFail = getRow.createCell(14);
            cellTotFail.setCellValue(TotalFailed+" Sub Scenario");
            cellTotFail.setCellStyle(styleBody);

            Cell cellTotun = getRow.createCell(15);
            cellTotun.setCellValue(TotalScenario-(TotalPass+TotalFailed)+" Sub Scenario");
            cellTotun.setCellStyle(styleBody);

            Cell cellTotPrcent = getRow.createCell(16);
            cellTotPrcent.setCellValue(TotalPrcentage+"%");
            cellTotPrcent.setCellStyle(styleBody);


            //=========CHART========================
            XSSFFont BodyFont2 = (XSSFFont) wb.createFont();
            BodyFont2.setFontHeightInPoints((short) 11);
            BodyFont2.setColor(IndexedColors.WHITE.getIndex());
            XSSFCellStyle styleTambahan = (XSSFCellStyle) wb.createCellStyle();
            styleTambahan.setFont(BodyFont2);

            if (sheet.getRow(4) == null) {
                Row getRow2 = sheet.createRow(4);

                Cell cellTotPass2 = getRow2.createCell(13);
                cellTotPass2.setCellValue(TotalPass);
                cellTotPass2.setCellStyle(styleTambahan);

                Cell cellTotFail2 = getRow2.createCell(14);
                cellTotFail2.setCellValue(TotalFailed);
                cellTotFail2.setCellStyle(styleTambahan);

                Cell cellTotUn2 = getRow2.createCell(15);
                cellTotUn2.setCellValue(TotalScenario-(TotalPass+TotalFailed));
                cellTotUn2.setCellStyle(styleTambahan);


            }else {
                Row getRow2 = sheet.getRow(4);

                Cell cellTotPass2 = getRow2.createCell(13);
                cellTotPass2.setCellValue(TotalPass);
                cellTotPass2.setCellStyle(styleTambahan);

                Cell cellTotFail2 = getRow2.createCell(14);
                cellTotFail2.setCellValue(TotalFailed);
                cellTotFail2.setCellStyle(styleTambahan);

                Cell cellTotUn2 = getRow2.createCell(15);
                cellTotUn2.setCellValue(TotalScenario-(TotalPass+TotalFailed));
                cellTotUn2.setCellStyle(styleTambahan);

            }

            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 12, 7, 16, 21);

            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("Running Percetage");
            chart.setTitleOverlay(false);

            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP_RIGHT);
            XDDFDataSource<String> header = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(2, 2, 13, 15));

            XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(4, 4, 13, 15));

            XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
            //XDDFChartData data = new XDDFPieChartData(chart.getCTChart().getPlotArea().addNewPieChart());

            data.setVaryColors(true);
            data.addSeries(header, values);
            chart.plot(data);
            //=========CHART========================

            //AUTO FIT SIZE
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            sheet.autoSizeColumn(8);
            sheet.autoSizeColumn(10);
            sheet.autoSizeColumn(11);
            sheet.autoSizeColumn(12);
            sheet.autoSizeColumn(13);
            sheet.autoSizeColumn(14);
            sheet.autoSizeColumn(15);
            sheet.autoSizeColumn(16);

            //Write the Excel file
            FileOutputStream fileOut = null;
            fileOut = new FileOutputStream(PathReport);
            wb.write(fileOut);
            fileOut.close();
        } catch (IOException ioex) {
        }


    }

    public static String convertTime(Long GetTotalDuration){
        long second = (GetTotalDuration / 1000) % 60;
        long minute = (GetTotalDuration / (1000 * 60)) % 60;
        long hour = (GetTotalDuration / (1000 * 60 * 60)) % 24;

        String time = String.format("%02d:%02d:%02d", hour, minute, second);

        return time;
    }

}
