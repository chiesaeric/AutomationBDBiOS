package launcher;

import action.mail;
import datatable.readDatatable;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import report.ReportWP;
import testcase.executeTest;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/*
Maksud dari function ini sebagain main untuk di improve jika didalam satu main ingin menjalankan lebih dari 1
testcase kedepannya
 */

public class launch {

    public static HashMap<String, IOSElement> tempObject = new HashMap<String, IOSElement>();
    public static IOSDriver<IOSElement> driver;



    public static void main(String[] args) throws Exception {

        String[] strDateNow = java.time.LocalDateTime.now().toString().split("[.]");//(dtf.format(now).toString());
        String strDate = strDateNow[0].replace(":","-");
        String reportExcel = "C:\\CaptureScreen\\Report\\BluIOS\\" + strDate + "Report_Blu.xlsx";
        String datatablePath = System.getProperty("user.dir") +"\\dataTable";
        String datatableFile = "ExecuteTest.xlsx";
        String getTableTestCase;
        String getUDID;
        String getAppPackage;
        String getAppActivity;
        String getStartData;
        String getEndData, strKeterangan;

        readDatatable objReadDatatable = new readDatatable();
        Sheet sheetTestcase = objReadDatatable.readExecuteTest(datatablePath, datatableFile);
        Workbook workbookTestcase = sheetTestcase.getWorkbook();

        int rowCountData = sheetTestcase.getLastRowNum() - sheetTestcase.getFirstRowNum();
        executeTest.tempInteger.put("Data", 1);
        executeTest.tempInteger.put("glbTotalCasePass",0);
        executeTest.tempInteger.put("glbTotalCaseFail",0);
        executeTest.tempInteger.put("glbTotalCase",0);


        int n=0;
        for (int j = 1; j <= rowCountData+1; j++) {
            Row row = sheetTestcase.getRow(j);

            if (row!=null){
                 if (row.getCell(0) != null && row.getCell(2).getStringCellValue().equalsIgnoreCase("YES")) {
                    n=n+1;
                    getTableTestCase = row.getCell(1).getStringCellValue();
                    getUDID = row.getCell(3).getStringCellValue();
                    getAppPackage = row.getCell(4).getStringCellValue();
                    getAppActivity = row.getCell(5).getStringCellValue();
                    if (row.getCell(6) != null) {
                        getStartData = row.getCell(6).getStringCellValue();
                    }else {
                        getStartData = "";
                    }

                    if (row.getCell(7) != null) {
                        getEndData = row.getCell(7).getStringCellValue();
                    }else {
                        getEndData = "";
                    }

                    if (row.getCell(8) != null) {
                        strKeterangan = row.getCell(8).getStringCellValue();
                    }else{
                        strKeterangan = "";
                    }

                    if (n==1) {
                        //Set the Desired Capabilities
                        DesiredCapabilities caps = new DesiredCapabilities();
                        caps.setCapability("platformName","iOtomasi's iPhone");
                        caps.setCapability("deviceName","iPhone");
                        caps.setCapability("automationName","XCUITest");
                        caps.setCapability("udid","00008020-00063DA60C68002E");
                        caps.setCapability("bundleId","com.blu.stg1.Identify2");
                        caps.setCapability("platformVersion","14.2");
                        caps.setCapability("xcodeSigningId","iPhone Developer");
                        caps.setCapability("xcodeOrgId","L687CJT5NF");
                        caps.setCapability("wdaLocalPort","8101");
                        driver = new IOSDriver<IOSElement>(new URL("http://192.168.137.206:8080/wd/hub"), caps);
                    }
                    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

                    executeTest.execute(driver, getTableTestCase, getStartData, getEndData, strKeterangan);

//                    //SWITCH TO WEB VIEW
//                    Set<String> contextNames = driver.getContextHandles();
//                    for (String contextName : contextNames) {
//                        System.out.println(contextName);
//                    }
//                    driver.context((String) contextNames.toArray()[1]);

                }
            }else{
                break;
            }

        }

        ReportWP.CreateReportExcel(reportExcel);
        driver.quit();

        ///==================Send Email==============================
        Dimension screens = Toolkit.getDefaultToolkit().getScreenSize();
        Double dY = screens.getHeight();
        Double dX = screens.getWidth();

        JFrame Jf;
        Jf = new JFrame();
        Jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Jf.setLocation(dX.intValue()/2,dY.intValue()/2);
        Jf.setVisible(true);
        Jf.setAlwaysOnTop(true);

        int sendEmail =JOptionPane.showOptionDialog(Jf, "Kirim Email?", "Send Email",JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null,null);
        if (sendEmail == JOptionPane.YES_OPTION) {
            mail.sendMail(reportExcel);
        }
        Jf.dispose();
        ///==================Send Email==============================
        //driverServer.StopAppiumServer();


    }

}