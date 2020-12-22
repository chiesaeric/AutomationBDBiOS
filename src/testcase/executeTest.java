package testcase;

import action.keyword;
import action.verify;
import datatable.readDatatable;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;
import log.appendLog;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.*;
import report.ReportWP;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class executeTest {

    public static HashMap<String, Integer> tempInteger = new HashMap<String, Integer>();
    public static HashMap<String, Boolean> tempValidation = new HashMap<String, Boolean>();
    public static HashMap<String, String> tempVar = new HashMap<String, String>();
    public static HashMap<String, String> glbVarString = new HashMap<String, String>();
    public static HashMap<String, List<String>> glbVarArray = new HashMap<String,List<String>>();
    public static HashMap<String, List<String>> glbVarLong = new HashMap<String,List<String>>();
    public static HashMap<String, List<String>> arrReport = new HashMap<String, List<String>>();
    public static HashMap<String, List<String>> arrKeterangan = new HashMap<String, List<String>>();
    public static Sheet sheetLabel,sheetLabel2,sheetLabel3,sheetVerify,sheetVerifyConf;
    public static Workbook workbookLabelcase,workbookLabelcase2,workbookLabelcase3,workbookVerify,workbookVerifyConf;


    public static void execute(IOSDriver driver, String DataTable, String strStartData, String strEndData, String keterangan) throws Exception {
        //DECLARE VARIABLE
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String now = LocalDate.now().toString();
        String[] strDateNow = LocalDateTime.now().toString().split("[.]");//(dtf.format(now).toString());
        String strDate = strDateNow[0].replace(":","-");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String AppName = "BLUIOS";
        String pathPicture = "C:\\CaptureScreen\\" + AppName +"\\"+strDate+"_"+DataTable+"\\";
        String fileLog = "C:\\CaptureScreen\\Log\\BluIOS\\" +strDate+".txt";
        String datatablePath = System.getProperty("user.dir") +"\\dataTable";
        String datatableFile = DataTable+".xlsx";
        String[] NamaDataTable = datatableFile.split("[.]");
        String[] param = new String[8];
        Integer glbNumImage = 0;
        tempVar.put("Switch","");
        int endData, startData;

        arrKeterangan.put("Keterangan", new ArrayList<String>());
        StopWatch stopWatch = new StopWatch();

        appendLog.append(fileLog, "Automation test started");
        appendLog.append(fileLog, NamaDataTable[0]);

        //READ DATATABLE
        keyword objKeyword = new keyword(driver);
        readDatatable objReadDatatable = new readDatatable();
        Sheet sheetTestcase = objReadDatatable.readTestcase(datatablePath, datatableFile);
        Sheet sheetDatatable = objReadDatatable.readDatatable(datatablePath, datatableFile);
        sheetLabel = objReadDatatable.readLabel(datatablePath, datatableFile);
        sheetLabel2 = objReadDatatable.readLabel2(datatablePath, datatableFile);
        sheetLabel3 = objReadDatatable.readLabel3(datatablePath, datatableFile);
        sheetVerify = objReadDatatable.readVerify(datatablePath, datatableFile);
        sheetVerifyConf = objReadDatatable.readVerifyConf(datatablePath, datatableFile);
        Workbook workbookTestcase = sheetTestcase.getWorkbook();
        workbookLabelcase = sheetLabel.getWorkbook();
        workbookLabelcase2 = sheetLabel2.getWorkbook();
        workbookLabelcase3 = sheetLabel3.getWorkbook();
        workbookVerify = sheetVerify.getWorkbook();
        workbookVerifyConf = sheetVerifyConf.getWorkbook();

        //LOOP DATA IN DATA TABLE
        int rowCountData = sheetDatatable.getLastRowNum() - sheetDatatable.getFirstRowNum();

        if (!strStartData.equalsIgnoreCase("")){
            startData = Integer.parseInt(strStartData);
        }else {
            startData = 1;
        }

        if (!strEndData.equalsIgnoreCase("")) {
            endData = Integer.parseInt(strEndData);
        }else {
            endData = rowCountData;
        }
        for (int j = startData; j <= endData; j++) {

            //DECLEAR VARIABLE GLOBAL
            glbVarString.clear();
            glbVarArray.clear();
            glbVarLong.clear();
            tempInteger.put("glbDataWp", j);
            tempInteger.put("glbNumImage", 0);
            tempInteger.put("flgCasePassed", 0);
            tempInteger.put("flgCaseFailed", 0);
            tempInteger.put("flgCase", 0);
            tempValidation.put("ifexist",false);
            tempValidation.put("result",true);
            glbVarString.put("reportResult","");
            Row rowVerf = sheetVerifyConf.getRow(j);



            stopWatch.reset();
            stopWatch.start();


            int rowCount = sheetTestcase.getLastRowNum() - sheetTestcase.getFirstRowNum();


            try{

                Row headRow = sheetDatatable.getRow(j);

                if(headRow==null || headRow.getCell(0).getNumericCellValue()<1){
                    break;
                }

                appendLog.append(fileLog, "Run Data : "+j +" of "+endData);
                Thread.sleep(3000);

                for (int i = 1; i < rowCount + 1; i++) {
                    Row row = sheetTestcase.getRow(i);

                    if(row==null){
                        break;
                    }

                    if (row.getCell(0) != null && !row.getCell(0).getStringCellValue().equalsIgnoreCase("")) {

                        appendLog.append(fileLog, row.getCell(0).toString());

                    } else if (row.getCell(1) != null && !row.getCell(1).getStringCellValue().equalsIgnoreCase("")) {

                        param[0] = row.getCell(1).toString();
                        param[1] = (row.getCell(2) == null) ? "" : row.getCell(2).toString();
                        param[2] = (row.getCell(3) == null) ? "" : row.getCell(3).toString();
                        param[3] = (row.getCell(4) == null) ? "" : row.getCell(4).toString();
                        Cell cell = row.getCell(5);

                        if (row.getCell(5) == null) {
                            param[4] = "";
                        } else {

                            //READ TABLE MAIN ACTIVITY
                            if (row.getCell(5).getCellType() == CellType.FORMULA) {
                                FormulaEvaluator evaluator = workbookTestcase.getCreationHelper().createFormulaEvaluator();
                                String strFormula = cell.getCellFormula();
                                String strSheetDatatable = ExtractFormulaSheet(strFormula);
                                int intIndexDatatable = ExtractFormulaAddress(strFormula);
                                String strColumn = CellReference.convertNumToColString(intIndexDatatable);

                                cell.setCellFormula(strSheetDatatable + "!" + strColumn + (j+1));
                                CellValue cellValue = evaluator.evaluate(cell);

                                if (GetCellValue(cellValue).equalsIgnoreCase("0.0")) {
                                    param[4] = "";
                                }else {
                                    param[4] = GetCellValue(cellValue);
                                }


                            } else {
                                param[4] = cell.toString();
                            }

                        }

                        objKeyword.perform(param, fileLog, pathPicture);

                        if (tempValidation.get("ifexist") == true){
                            runCaseLabel(objKeyword, fileLog, pathPicture, param[4], j);
                            tempValidation.put("ifexist",false);
                        }

                    }


                }



            }catch (Exception e){
                takeSnapShot(driver,pathPicture);
                appendLog.append(fileLog, "Error testing in data "+(j)+"\nError: "+e);
                verifyTestCase(j, fileLog, rowVerf,"Gagal");
                if(executeTest.glbVarString.get("reportResult").equalsIgnoreCase("")){
                    executeTest.glbVarString.put("reportResult","- Error Automation, Object tidak ditemukan");
                }
                createReport(DataTable,stopWatch.getTime(),"FAILED",keterangan);
                stopWatch.stop();
                driver.closeApp();
                Thread.sleep(5000);
                driver.launchApp();
                Thread.sleep(5000);
                tempValidation.put("result",false);
            }

            //REPORT SUCCESS
            if(tempValidation.get("result")==true) {
                verifyTestCase(j, fileLog, rowVerf,"Berhasil");
                String Ket = "";
                if(glbVarString.get("reportResult").equalsIgnoreCase("")){
                    Ket = "PASSED";
                }else{
                    Ket = "FAILED";
                }
                createReport(DataTable, stopWatch.getTime(), Ket,keterangan);
                stopWatch.stop();

            }



        }
        //Crate WP
       ReportWP.CreateWPExcel(pathPicture, NamaDataTable[0], "HORIZONTAL");

    }


    private static String ExtractFormulaSheet(String strFormula) {
        String strSheet[] = strFormula.split("!");

        return strSheet[0];
    }

    private static int ExtractFormulaAddress(String strFormula) {

        String strSheet[] = strFormula.split("!");
        CellReference cellReference = new CellReference(strSheet[1]);

        return cellReference.getCol();
    }

    private static String GetCellValue(CellValue cellValue) {

        CellType cellType = cellValue.getCellType();
        String strResult = "";

        if (cellType ==CellType.NUMERIC) {
            strResult = cellValue.getNumberValue() + "";
        }else if (cellType ==CellType.STRING) {
            strResult = cellValue.getStringValue() + "";
        }

//        switch (cellType) {
//            case 0:
//                strResult = cellValue.getNumberValue() + "";
//                break;
//            case 1:
//                strResult = cellValue.getStringValue() + "";
//                break;
//        }

        if (strResult.equals("00")) {
            strResult = "";
        }

        return strResult;
    }

    private static void runCaseLabel(keyword objKeyword, String fileLog, String pathPicture, String Label, Integer j) throws Exception {

        //DECLARE LOCAL VARIABLE
        int rowCount = sheetLabel.getLastRowNum() - sheetLabel.getFirstRowNum();
        String[] param = new String[8];
        Boolean flgKetemu = false;
        Boolean flgLabel = false;
        String strLabel = "";
        tempValidation.put("ifexist",false);

        for (int i = 1; i < rowCount + 1; i++) {
            Row row = sheetLabel.getRow(i);

            if(row==null){
                flgKetemu = false;
                flgLabel = false;
                break;
            }else{
                flgKetemu = true;
            }

            //LABEL FLAG
            if (row!=null){
                if (row.getCell(0)==null){
                    strLabel = "";
                } else {
                    strLabel = row.getCell(0).getStringCellValue();
                }
            }else{
                strLabel = "";
            }

            if (Label.equals(strLabel)){
                flgLabel=true;
            }else if(strLabel.equals("") && flgLabel==true && flgKetemu == true){
                flgLabel=true;
            }else if(!strLabel.equals("") && flgLabel==true){
                break;
            }else{
                flgLabel=false;
            }


            //MATCH LABEL
            if (flgLabel==true) {



                if (row.getCell(0) != null && !row.getCell(0).getStringCellValue().equalsIgnoreCase("")) {

                    appendLog.append(fileLog, row.getCell(0).toString());

                } else if (row.getCell(1) != null && !row.getCell(1).getStringCellValue().equalsIgnoreCase("")) {

                    param[0] = row.getCell(1).toString();
                    param[1] = (row.getCell(2) == null) ? "" : row.getCell(2).toString();
                    param[2] = (row.getCell(3) == null) ? "" : row.getCell(3).toString();
                    param[3] = (row.getCell(4) == null) ? "" : row.getCell(4).toString();
                    Cell cell = row.getCell(5);

                    if (row.getCell(5) == null) {
                        param[4] = "";
                    } else {

                        //READ TABLE MAIN ACTIVITY
                        if (row.getCell(5).getCellType() == CellType.FORMULA) {
                            FormulaEvaluator evaluator = workbookLabelcase.getCreationHelper().createFormulaEvaluator();
                            String strFormula = cell.getCellFormula();
                            String strSheetDatatable = ExtractFormulaSheet(strFormula);
                            int intIndexDatatable = ExtractFormulaAddress(strFormula);
                            String strColumn = CellReference.convertNumToColString(intIndexDatatable);

                            cell.setCellFormula(strSheetDatatable + "!" + strColumn + (j+1));
                            CellValue cellValue = evaluator.evaluate(cell);

                            if (GetCellValue(cellValue).equalsIgnoreCase("0.0")) {
                                param[4] = "";
                            }else {
                                param[4] = GetCellValue(cellValue);
                            }
                        } else {
                            param[4] = cell.toString();
                        }

                    }

                    objKeyword.perform(param, fileLog, pathPicture);

                    if (tempValidation.get("ifexist") == true){
                        runCaseLabel2(objKeyword, fileLog, pathPicture, param[4], j);
                        tempValidation.put("ifexist",false);
                    }


                }



               }


            }


        }

    private static void runCaseLabel2(keyword objKeyword, String fileLog, String pathPicture, String Label, Integer j) throws Exception {

        //DECLARE LOCAL VARIABLE
        int rowCount = sheetLabel2.getLastRowNum() - sheetLabel2.getFirstRowNum();
        String[] param = new String[8];
        Boolean flgKetemu = false;
        Boolean flgLabel = false;
        String strLabel = "";
        tempValidation.put("ifexist",false);

        for (int i = 1; i < rowCount + 1; i++) {
            Row row = sheetLabel2.getRow(i);

            if(row==null){
                flgKetemu = false;
                flgLabel = false;
                break;
            }else{
                flgKetemu = true;
            }

            if (row!=null){
                if (row.getCell(0)==null){
                    strLabel = "";
                } else {
                    strLabel = row.getCell(0).getStringCellValue();
                }
            }else{
                strLabel = "";
            }
            //LABEL FLAG
            if (row.getCell(0)==null || row==null){
                strLabel = "";
            }else{
                strLabel = row.getCell(0).getStringCellValue();
            }

            if (Label.equals(strLabel)){
                flgLabel=true;
            }else if(strLabel.equals("") && flgLabel==true && flgKetemu == true){
                flgLabel=true;
            }else if(!strLabel.equals("") && flgLabel==true){
                break;
            }else{
                flgLabel=false;
            }


            //MATCH LABEL
            if (flgLabel==true) {



                if (row.getCell(0) != null && !row.getCell(0).getStringCellValue().equalsIgnoreCase("")) {

                    appendLog.append(fileLog, row.getCell(0).toString());

                } else if (row.getCell(1) != null && !row.getCell(1).getStringCellValue().equalsIgnoreCase("")) {

                    param[0] = row.getCell(1).toString();
                    param[1] = (row.getCell(2) == null) ? "" : row.getCell(2).toString();
                    param[2] = (row.getCell(3) == null) ? "" : row.getCell(3).toString();
                    param[3] = (row.getCell(4) == null) ? "" : row.getCell(4).toString();
                    Cell cell = row.getCell(5);

                    if (row.getCell(5) == null) {
                        param[4] = "";
                    } else {

                        //READ TABLE MAIN ACTIVITY
                        if (row.getCell(5).getCellType() == CellType.FORMULA) {
                            FormulaEvaluator evaluator = workbookLabelcase2.getCreationHelper().createFormulaEvaluator();
                            String strFormula = cell.getCellFormula();
                            String strSheetDatatable = ExtractFormulaSheet(strFormula);
                            int intIndexDatatable = ExtractFormulaAddress(strFormula);
                            String strColumn = CellReference.convertNumToColString(intIndexDatatable);

                            cell.setCellFormula(strSheetDatatable + "!" + strColumn + (j+1));
                            CellValue cellValue = evaluator.evaluate(cell);

                            if (GetCellValue(cellValue).equalsIgnoreCase("0.0")) {
                                param[4] = "";
                            }else {
                                param[4] = GetCellValue(cellValue);
                            }
                        } else {
                            param[4] = cell.toString();
                        }

                    }

                    objKeyword.perform(param, fileLog, pathPicture);

                    if (tempValidation.get("ifexist") == true){
                        runCaseLabel3(objKeyword, fileLog, pathPicture, param[4], j);
                        tempValidation.put("ifexist",false);
                    }
                }



            }


        }


    }

    private static void runCaseLabel3(keyword objKeyword, String fileLog, String pathPicture, String Label, Integer j) throws Exception {

        //DECLARE LOCAL VARIABLE
        int rowCount = sheetLabel3.getLastRowNum() - sheetLabel3.getFirstRowNum();
        String[] param = new String[8];
        Boolean flgKetemu = false;
        Boolean flgLabel = false;
        String strLabel = "";
        tempValidation.put("ifexist",false);

        for (int i = 1; i < rowCount + 1; i++) {
            Row row = sheetLabel3.getRow(i);

            if(row==null){
                flgKetemu = false;
                flgLabel = false;
                break;
            }else{
                flgKetemu = true;
            }

            if (row!=null){
                if (row.getCell(0)==null){
                    strLabel = "";
                } else {
                    strLabel = row.getCell(0).getStringCellValue();
                }
            }else{
                strLabel = "";
            }
            //LABEL FLAG
            if (row.getCell(0)==null || row==null){
                strLabel = "";
            }else{
                strLabel = row.getCell(0).getStringCellValue();
            }

            if (Label.equals(strLabel)){
                flgLabel=true;
            }else if(strLabel.equals("") && flgLabel==true && flgKetemu == true){
                flgLabel=true;
            }else if(!strLabel.equals("") && flgLabel==true){
                break;
            }else{
                flgLabel=false;
            }


            //MATCH LABEL
            if (flgLabel==true) {



                if (row.getCell(0) != null && !row.getCell(0).getStringCellValue().equalsIgnoreCase("")) {

                    appendLog.append(fileLog, row.getCell(0).toString());

                } else if (row.getCell(1) != null && !row.getCell(1).getStringCellValue().equalsIgnoreCase("")) {

                    param[0] = row.getCell(1).toString();
                    param[1] = (row.getCell(2) == null) ? "" : row.getCell(2).toString();
                    param[2] = (row.getCell(3) == null) ? "" : row.getCell(3).toString();
                    param[3] = (row.getCell(4) == null) ? "" : row.getCell(4).toString();
                    Cell cell = row.getCell(5);

                    if (row.getCell(5) == null) {
                        param[4] = "";
                    } else {

                        //READ TABLE MAIN ACTIVITY
                        if (row.getCell(5).getCellType() == CellType.FORMULA) {
                            FormulaEvaluator evaluator = workbookLabelcase3.getCreationHelper().createFormulaEvaluator();
                            String strFormula = cell.getCellFormula();
                            String strSheetDatatable = ExtractFormulaSheet(strFormula);
                            int intIndexDatatable = ExtractFormulaAddress(strFormula);
                            String strColumn = CellReference.convertNumToColString(intIndexDatatable);

                            cell.setCellFormula(strSheetDatatable + "!" + strColumn + (j+1));
                            CellValue cellValue = evaluator.evaluate(cell);

                            if (GetCellValue(cellValue).equalsIgnoreCase("0.0")) {
                                param[4] = "";
                            }else {
                                param[4] = GetCellValue(cellValue);
                            }
                        } else {
                            param[4] = cell.toString();
                        }

                    }

                    objKeyword.perform(param, fileLog, pathPicture);

                }



            }


        }


    }

    private static void takeSnapShot(IOSDriver driver,String Path) throws Exception{

        //Ambil data global dari execute test
        Integer dataWp,NumImage;
        String right,strNumb, fileWithPath;

        dataWp = executeTest.tempInteger.get("glbDataWp");
        NumImage = executeTest.tempInteger.get("glbNumImage");
        NumImage = NumImage + 1;
        right = "000" + NumImage.toString();
        strNumb = right.substring(right.length() - 4, right.length());

        //pathnya belum kebuat nih cui
        fileWithPath = Path + dataWp+"_"+strNumb+".jpeg";

        try{
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile = new File(fileWithPath);
            FileUtils.copyFile(SrcFile, DestFile);
        }catch (Exception e){

        }
        executeTest.tempInteger.put("glbNumImage",NumImage);

    }

    private static void  createReport(String DataTable, Long durasi,String keterangan,String menu){
        String TampKeterangan = "";
        Integer totalCasePass = executeTest.tempInteger.get("flgCasePassed");
        Integer totalCaseFail = executeTest.tempInteger.get("flgCaseFailed");
        Integer totalCase = executeTest.tempInteger.get("flgCase");
        Integer totalUnTested = totalCase-(totalCasePass+totalCaseFail);


        //Validasi untested
//        if(keterangan.equalsIgnoreCase("PASSED")){
//            totalCase = totalCase - totalUnTested;
//            totalUnTested = 0;
//            executeTest.tempInteger.put("glbTotalCase",executeTest.tempInteger.get("glbTotalCase")-totalUnTested);
//        }


        tempInteger.put("glbTotalCasePass",tempInteger.get("glbTotalCasePass")+totalCasePass);
        tempInteger.put("glbTotalCaseFail",tempInteger.get("glbTotalCaseFail")+totalCaseFail);


        Integer totalPrcentage;
        //Validation total pass prcentage
        if(totalCase==0){
            totalPrcentage = 0;
        }else{
            totalPrcentage = (tempInteger.get("flgCasePassed")*100)/totalCase;
        }

        if(arrKeterangan.get("Keterangan").size()>0) {
            for (int ket = 0; ket <= arrKeterangan.get("Keterangan").size()-1; ket++) {
                if (TampKeterangan.equalsIgnoreCase("")) {
                    TampKeterangan = arrKeterangan.get("Keterangan").get(ket);
                } else {
                    TampKeterangan = TampKeterangan + " " + arrKeterangan.get("Keterangan").get(ket);
                }
            }
        }
        arrKeterangan.get("Keterangan").clear();
        arrReport.put("data "+tempInteger.get("Data"), new ArrayList<String>());
        arrReport.get("data "+tempInteger.get("Data")).add(tempInteger.get("Data").toString());
        arrReport.get("data "+tempInteger.get("Data")).add(menu);
        arrReport.get("data "+tempInteger.get("Data")).add(TampKeterangan);
        arrReport.get("data "+tempInteger.get("Data")).add(durasi.toString());
        arrReport.get("data "+tempInteger.get("Data")).add(glbVarString.get("reportResult"));
        arrReport.get("data "+tempInteger.get("Data")).add(totalCasePass.toString());
        arrReport.get("data "+tempInteger.get("Data")).add(totalCaseFail.toString());
        arrReport.get("data "+tempInteger.get("Data")).add(totalUnTested.toString());
        arrReport.get("data "+tempInteger.get("Data")).add(totalPrcentage.toString()+"%");
        arrReport.get("data "+tempInteger.get("Data")).add(keterangan);

        tempInteger.put("Data",tempInteger.get("Data")+1);

    }

    private static void verifyTestCase(Integer j, String strFileLog, Row rowVerf, String Ket){
        //DECLARE LOCAL VARIABLE
        int rowCount = sheetVerify.getLastRowNum() - sheetVerify.getFirstRowNum();
        tempInteger.put("rowVerifCount", 0);

        for (int i = 1; i < rowCount + 1; i++) {
            Row row = sheetVerify.getRow(i);
            String[] param = new String[8];

                try{

                    if (row == null) {
                        break;
                    }else{
                        /*
                        KEYWORD
                        OPERATION
                        VARIABLE
                        BANDINGAN
                        OTHER
                        TIPE VARIABLE
                        RESULT
                        VARIABLE RESULT
                        REMARK
                         */
                        param[0] = (row.getCell(0) == null) ? "" : row.getCell(0).toString();
                        param[1] = (row.getCell(1) == null) ? "" : row.getCell(1).toString();
                        param[2] = (row.getCell(2) == null) ? "" : row.getCell(2).toString();
                        param[4] = (row.getCell(4) == null) ? "" : row.getCell(4).toString();
                        param[5] = (row.getCell(5) == null) ? "" : row.getCell(5).toString();
                        param[6] = (row.getCell(6) == null) ? "" : row.getCell(6).toString();

                        Cell cell = row.getCell(3);

                        if (row.getCell(3) == null) {
                            param[3] = "";
                        } else {

                            //READ TABLE MAIN ACTIVITY
                            if (row.getCell(3).getCellType() == CellType.FORMULA) {
                                FormulaEvaluator evaluator = workbookVerify.getCreationHelper().createFormulaEvaluator();
                                String strFormula = cell.getCellFormula();
                                String strSheetDatatable = ExtractFormulaSheet(strFormula);
                                int intIndexDatatable = ExtractFormulaAddress(strFormula);
                                String strColumn = CellReference.convertNumToColString(intIndexDatatable);

                                cell.setCellFormula(strSheetDatatable + "!" + strColumn + (j+1));
                                CellValue cellValue = evaluator.evaluate(cell);

                                if (GetCellValue(cellValue).equalsIgnoreCase("0.0")) {
                                    param[3] = "";
                                }else {
                                    param[3] = GetCellValue(cellValue);
                                }
                            } else {
                                param[3] = cell.toString();
                            }

                        }

                        verify.validate(param, strFileLog, rowVerf, Ket);

                    }

                }catch (Exception e){
                    System.out.print("ERROR VERIFY: \n"+e +" \n ");
                }

        }


    }




    }






