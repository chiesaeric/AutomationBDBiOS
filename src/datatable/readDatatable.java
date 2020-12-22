package datatable;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class readDatatable {

    public Sheet readExecuteTest(String datatablePath, String datatableFile) throws IOException {
        File file = new File(datatablePath + "\\" +datatableFile);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook;
        String fileExtension = datatableFile.substring(datatableFile.indexOf("."));

        if(fileExtension.equals(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }
        else if(fileExtension.equals(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        }
        else {
            workbook = null;
        }

        Sheet sheet = workbook.getSheet("Execute");
        return sheet;
    }


    public Sheet readTestcase(String datatablePath, String datatableFile) throws IOException {
        File file = new File(datatablePath + "\\" +datatableFile);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook;
        String fileExtension = datatableFile.substring(datatableFile.indexOf("."));

        if(fileExtension.equals(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }
        else if(fileExtension.equals(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        }
        else {
            workbook = null;
        }

        Sheet sheet = workbook.getSheet("TestCase");
        return sheet;
    }

    public Sheet readDatatable(String datatablePath, String datatableFile) throws IOException {
        File file = new File(datatablePath + "\\" +datatableFile);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook;
        String fileExtension = datatableFile.substring(datatableFile.indexOf("."));

        if(fileExtension.equals(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }
        else if(fileExtension.equals(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        }
        else {
            workbook = null;
        }

        Sheet sheet = workbook.getSheet("MainActivity");
        return sheet;
    }

    public Sheet readLabel(String datatablePath, String datatableFile) throws IOException {
        File file = new File(datatablePath + "\\" +datatableFile);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook;
        String fileExtension = datatableFile.substring(datatableFile.indexOf("."));

        if(fileExtension.equals(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }
        else if(fileExtension.equals(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        }
        else {
            workbook = null;
        }

        Sheet sheet = workbook.getSheet("LabelCase");
        return sheet;
    }

    public Sheet readLabel2(String datatablePath, String datatableFile) throws IOException {
        File file = new File(datatablePath + "\\" +datatableFile);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook;
        String fileExtension = datatableFile.substring(datatableFile.indexOf("."));

        if(fileExtension.equals(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }
        else if(fileExtension.equals(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        }
        else {
            workbook = null;
        }

        Sheet sheet = workbook.getSheet("LabelCaseLvl2");
        return sheet;
    }

    public Sheet readLabel3(String datatablePath, String datatableFile) throws IOException {
        File file = new File(datatablePath + "\\" +datatableFile);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook;
        String fileExtension = datatableFile.substring(datatableFile.indexOf("."));

        if(fileExtension.equals(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }
        else if(fileExtension.equals(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        }
        else {
            workbook = null;
        }

        Sheet sheet = workbook.getSheet("LabelCaseLvl3");
        return sheet;
    }

    public Sheet readVerify(String datatablePath, String datatableFile) throws IOException {
        File file = new File(datatablePath + "\\" +datatableFile);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook;
        String fileExtension = datatableFile.substring(datatableFile.indexOf("."));

        if(fileExtension.equals(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }
        else if(fileExtension.equals(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        }
        else {
            workbook = null;
        }

        Sheet sheet = workbook.getSheet("Verify");
        return sheet;
    }

    public Sheet readVerifyConf(String datatablePath, String datatableFile) throws IOException {
        File file = new File(datatablePath + "\\" +datatableFile);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook;
        String fileExtension = datatableFile.substring(datatableFile.indexOf("."));

        if(fileExtension.equals(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }
        else if(fileExtension.equals(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        }
        else {
            workbook = null;
        }

        Sheet sheet = workbook.getSheet("VerifyConfig");
        return sheet;
    }


}
