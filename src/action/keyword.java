package action;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.HideKeyboardStrategy;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import launcher.launch;
import log.appendLog;
import objectRepository.readObjectRepository;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import testcase.executeTest;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class keyword {

    io.appium.java_client.ios.IOSDriver AndroidDriver;
    public static String strXmlName, strGlbValue;
    public static IOSElement objValue;



    public keyword(IOSDriver driver) {
        this.AndroidDriver = driver;
    }

    public void perform(String[] param, String pathLog, String pathPicture) throws Exception {
        /*
        param0: keyword
        param1: pagename
        param2: objectname
        param3: objecttype
        param4: value
        */

        String strKeyword, strPageName, strObjectName, strObjectType, strValue;
        strKeyword = param[0];
        strPageName = param[1];
        strObjectName = param[2];
        strObjectType = param[3];
        strValue = param[4];

        //READ OBJECT REPOSITORY
        readObjectRepository objRepository = new readObjectRepository();
        Document xmlObjectRepository = objRepository.read(strPageName);
        if(!strPageName.equalsIgnoreCase("")) {
            String[] arrXmlName = xmlObjectRepository.getDocumentURI().split("/");
            strXmlName = arrXmlName[arrXmlName.length - 1].replace(".xml", "");
            strGlbValue = strValue;
        }else{
            strXmlName = "";
            strGlbValue = "";
        }


        objValue = getObject(strPageName,strObjectName,strValue,xmlObjectRepository,strObjectType,strKeyword,pathLog,"AWAL");

        if (objValue==null){
            if(strKeyword.equalsIgnoreCase("IF EXIST") || strKeyword.equalsIgnoreCase("LOOP WHILE EXIST")) {
                //System.out.print("MASUK PAK EKO! \n");
                return;
            }else{
                objValue = null;
            }
        }




        switch (strKeyword.toUpperCase()) {

            case "SETTEXT":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                Thread.sleep(300);
                if (!strValue.trim().equals("")) {
                    objValue.clear();
                }

                    objValue.sendKeys(strValue);

                appendLog.append(pathLog, "Set text -> " + strObjectName + ": " + strValue);
                break;
            case "CLEARTEXT":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                Thread.sleep(300);
                objValue.clear();
                appendLog.append(pathLog, "Set text -> " + strObjectName + ": " + strValue);
                break;
            case "CLICK":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                objValue.click();
                appendLog.append(pathLog, "Click -> " + strObjectName + ": " + strValue);
                break;
            case "SELECT":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);

                Thread.sleep(300);
                String getLastText = "";
                String getCurrentText = "";

                Dimension windowSize = AndroidDriver.manage().window().getSize();


                Integer width = windowSize.getWidth();
                Integer height = windowSize.getHeight();

                Integer xDevice = width/3-width/5;
                Integer yDevice = height/2+height/4;

                Boolean ketemu = false;

                do{
                    getLastText = objValue.getText();


                    if(getLastText.equalsIgnoreCase(strValue)){
                        ketemu = true;
                    }else{

                        objValue.click();
                        Thread.sleep(1000);


                        new TouchAction((PerformsTouchActions) AndroidDriver)
                                .press(PointOption.point(xDevice, yDevice))
                                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                                .moveTo(PointOption.point(xDevice, yDevice-80))
                                .release().perform();
                        Thread.sleep(1000);
                        AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btnPilih", strObjectType)).click();

                        getCurrentText = objValue.getText();

                        if(getCurrentText.equalsIgnoreCase(strValue) || getCurrentText.equalsIgnoreCase(getLastText)){
                            ketemu = true;
                        }else{
                            ketemu = false;
                        }

                    }


                }while(ketemu==false);

                appendLog.append(pathLog, "SELECT -> " + strObjectName + ": " + strValue);
                break;
            case "SETDATE":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                Thread.sleep(300);
                objValue.click();
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                String getDate = objValue.getText();
                String[] arrDate = getDate.split(" ");
                String[] arrDateTbl = strValue.split("/");
                Integer intGetTanggalTbl,intGetBulanTbl,intGetTahunTbl,intLoopSwipeTgl,intLoopSwipeBulan,intLoopSwipeTahun;
                Boolean SwipePlusTgl,SwipePlusBulan,SwipePlusTahun;

                //GET TANGGAL
                Integer intGetTanggal = Integer.parseInt(arrDate[arrDate.length-3]);

                if (arrDateTbl[0]!=null) {
                    intGetTanggalTbl = Integer.parseInt(arrDateTbl[0]);
                }else{
                    intGetTanggalTbl = 0;
                }

                if (intGetTanggal <= intGetTanggalTbl){
                    intLoopSwipeTgl = intGetTanggalTbl-intGetTanggal;
                    SwipePlusTgl = false;
                }else{
                    intLoopSwipeTgl = intGetTanggal-intGetTanggalTbl;
                    SwipePlusTgl = true;
                }


                //GET BULAN
                String strGetBulan = arrDate[arrDate.length-2].toString();
                Integer intGetBulan;
                intGetBulan = getMonthInteger(strGetBulan);


                if (arrDateTbl[1]!=null) {
                    intGetBulanTbl = Integer.parseInt(arrDateTbl[1]);
                }else{
                    intGetBulanTbl = 0;
                }

                if (intGetBulan <= intGetBulanTbl){
                    intLoopSwipeBulan = intGetBulanTbl-intGetBulan;
                    SwipePlusBulan = false;
                }else{
                    intLoopSwipeBulan = intGetBulan-intGetBulanTbl;
                    SwipePlusBulan = true;
                }


                //TAHUN
                Integer intGetTahun = Integer.parseInt(arrDate[arrDate.length-1]);
                if (arrDateTbl[2]!=null) {
                    intGetTahunTbl = Integer.parseInt(arrDateTbl[2]);
                }else{
                    intGetTahunTbl = 0;
                }

                if (intGetTahun <= intGetTahunTbl){
                    intLoopSwipeTahun = intGetTahunTbl-intGetTahun;
                    SwipePlusTahun = false;
                }else{
                    intLoopSwipeTahun = intGetTahun-intGetTahunTbl;
                    SwipePlusTahun = true;
                }

                //SWIPING AREA
                Dimension windowSizeDate = AndroidDriver.manage().window().getSize();

                Integer widthDev = windowSizeDate.getWidth();
                Integer heightDev = windowSizeDate.getHeight();

                //SwipeTanggal
                Integer xDate = widthDev/3-widthDev/5;
                Integer yDate = heightDev/2+heightDev/4;

                //Bulan
                Integer xMonth = (widthDev/3*2)-widthDev/5;
                Integer yMonth = heightDev/2+heightDev/4;

                //Tahun
                Integer xYear = widthDev-widthDev/5;
                Integer yYear = heightDev/2+heightDev/4;

                //SWIPING
                Integer SwipeTgl,SwipeBulan,SwipeTahun;

                if (SwipePlusTgl==true){
                    SwipeTgl = yDate + 80;
                }else{
                    SwipeTgl = yDate - 80;
                }

                if (SwipePlusBulan==true){
                    SwipeBulan = yMonth + 80;
                }else{
                    SwipeBulan = yMonth - 80;
                }

                if (SwipePlusTahun==true){
                    SwipeTahun = yYear + 80;
                }else{
                    SwipeTahun = yYear - 80;
                }


                //TOUCH ACTION
                //YEAR
                for (int tahun = 0; tahun <= intLoopSwipeTahun-1; tahun++) {

                    new TouchAction((PerformsTouchActions) AndroidDriver)
                            .press(PointOption.point(xYear, yYear))
                            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                            .moveTo(PointOption.point(xYear, SwipeTahun))
                            .release().perform();
                }

                //Month
                for (int bulan = 0; bulan <= intLoopSwipeBulan-1; bulan++) {

                    new TouchAction((PerformsTouchActions) AndroidDriver)
                            .press(PointOption.point(xMonth, yMonth))
                            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                            .moveTo(PointOption.point(xMonth, SwipeBulan))
                            .release().perform();
                }

                //Date
                for (int tgl = 0; tgl <= intLoopSwipeTgl-1; tgl++) {

                    new TouchAction((PerformsTouchActions) AndroidDriver)
                            .press(PointOption.point(xDate, yDate))
                            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                            .moveTo(PointOption.point(xDate, SwipeTgl))
                            .release().perform();
                }

                AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btnPilih", strObjectType)).click();

                appendLog.append(pathLog, "Set date -> " + strObjectName + ": " + strValue);
                break;
            case "SETMONTH":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                Thread.sleep(300);
                objValue.click();
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                String getMonth = objValue.getText();
                String[] arrMonth = getMonth.split(" ");
                String[] arrMonthTbl = strValue.split("/");
                Integer intGetMonthTbl,intGetYearTbl,intLoopSwipeMonth,intLoopSwipeYear;
                Boolean SwipePlusMonth,SwipePlusYear;


                //GET BULAN
                String strGetBulan2 = arrMonth[0].toString();
                Integer intGetBulan2;

                intGetBulan2 = getMonthInteger(strGetBulan2);


                if (arrMonthTbl[0]!=null) {
                    intGetBulanTbl = Integer.parseInt(arrMonthTbl[0]);
                }else{
                    intGetBulanTbl = 0;
                }

                if (intGetBulan2 <= intGetBulanTbl){
                    intLoopSwipeMonth = intGetBulanTbl-intGetBulan2;
                    SwipePlusMonth = false;
                }else{
                    intLoopSwipeMonth = intGetBulan2-intGetBulanTbl;
                    SwipePlusMonth = true;
                }


                //TAHUN
                Integer intGetTahun2 = Integer.parseInt(arrMonth[1]);
                if (arrMonthTbl[1]!=null) {
                    intGetYearTbl = Integer.parseInt(arrMonthTbl[1]);
                }else{
                    intGetYearTbl = 0;
                }

                if (intGetTahun2 <= intGetYearTbl){
                    intLoopSwipeYear = intGetYearTbl-intGetTahun2;
                    SwipePlusYear = false;
                }else{
                    intLoopSwipeYear = intGetTahun2-intGetYearTbl;
                    SwipePlusYear = true;
                }

                //SWIPING AREA
                Dimension windowSizeMonth = AndroidDriver.manage().window().getSize();

                Integer widthMonth = windowSizeMonth.getWidth();
                Integer heightMonth = windowSizeMonth.getHeight();

                //Bulan
                Integer xMonth2 = (widthMonth/2)-widthMonth/5;
                Integer yMonth2 = heightMonth/2+heightMonth/4;

                //Tahun
                Integer xYear2 = widthMonth-widthMonth/5;
                Integer yYear2 = heightMonth/2+heightMonth/4;

                //SWIPING
                Integer SwipeBulan2,SwipeTahun2;


                if (SwipePlusMonth==true){
                    SwipeBulan2 = yMonth2 + 80;
                }else{
                    SwipeBulan2 = yMonth2 - 80;
                }

                if (SwipePlusYear==true){
                    SwipeTahun2 = yYear2 + 80;
                }else{
                    SwipeTahun2 = yYear2 - 80;
                }


                //TOUCH ACTION
                //YEAR
                for (int tahun = 0; tahun <= intLoopSwipeYear-1; tahun++) {

                    new TouchAction((PerformsTouchActions) AndroidDriver)
                            .press(PointOption.point(xYear2, yYear2))
                            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                            .moveTo(PointOption.point(xYear2, SwipeTahun2))
                            .release().perform();
                }

                //Month
                for (int bulan = 0; bulan <= intLoopSwipeMonth-1; bulan++) {

                    new TouchAction((PerformsTouchActions) AndroidDriver)
                            .press(PointOption.point(xMonth2, yMonth2))
                            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                            .moveTo(PointOption.point(xMonth2, SwipeBulan2))
                            .release().perform();
                }

                AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btnPilih", strObjectType)).click();

                appendLog.append(pathLog, "Set Month -> " + strObjectName + ": " + strValue);
                break;
            case "BACK":
                Thread.sleep(500);
                AndroidDriver.navigate().back();
                appendLog.append(pathLog, "BACK");
                break;
            case "SWIPE BACK":

                Thread.sleep(500);
                //SWIPING AREA
                Dimension windowSizePopBack = AndroidDriver.manage().window().getSize();

                Integer widthPopBack = windowSizePopBack.getWidth();
                Integer heightPopBack = windowSizePopBack.getHeight();

                Integer xPopUpBack = widthPopBack/2+widthPopBack/3;
                Integer yPopUpBack = heightPopBack/2;


                new TouchAction((PerformsTouchActions) AndroidDriver)
                        .press(PointOption.point(0, yPopUpBack))
                        .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                        .moveTo(PointOption.point(xPopUpBack, yPopUpBack))
                        .release().perform();


                appendLog.append(pathLog, "SWIPE BACK -> " + strObjectName + ": " + strValue);


                break;
            case "HIDE KEYBOARD":
                Thread.sleep(500);
                AndroidDriver.hideKeyboard();
                appendLog.append(pathLog, "HIDE KEYBOARD");
                break;
            case "OTP BLU":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);

//                //get dimension
//                Dimension winSizeOTP = AndroidDriver.manage().window().getSize();
//                Integer screenX = winSizeOTP.height/2;
//                Integer screenY = winSizeOTP.width/2;
//
//                new TouchAction((PerformsTouchActions) AndroidDriver)
//                        .press(PointOption.point(screenX, 31))
//                        .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
//                        .moveTo(PointOption.point(screenX, screenY))
//                        .release()
//                        .perform();
//
//                loopWhile(xmlObjectRepository, "lblSPRINT", strObjectType,strValue);
//                AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "lblSPRINT", strObjectType)).click();
//
//                List<WebElement> elementName = AndroidDriver.findElements(By.xpath("//../android.widget.TextView[contains(@text,'OTP')]"));
//                WebElement pesanTerakhir = elementName.get(elementName.size()-1);
//                String pesan = pesanTerakhir.getText();
//                String[] arrPesan = pesan.split("OTP");
//                String strKodeOTP = arrPesan[2].trim().substring(0,4);
                String strKodeOTP = strValue;



                for (int digit = 0; digit<=strValue.length()-1; digit++){
                    Thread.sleep(500);

                    if (digit == strValue.length()-1){
                        takeSnapShot(AndroidDriver,pathPicture);
                    }

                    pushButtonOTP(strValue.substring(digit,digit+1));
                }
                appendLog.append(pathLog, "SETTEXT OTP -> " + strObjectName + ": " + strValue);
                break;
            case "PIN BLU":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                for (int digit = 0; digit<=strValue.length()-1; digit++){


                    if (digit == strValue.length()-1){
                        takeSnapShot(AndroidDriver,pathPicture);
                    }

                    pushButtonPassword(xmlObjectRepository,strValue.substring(digit,digit+1));
                    Thread.sleep(1000);
                }
                Thread.sleep(4000);
                appendLog.append(pathLog, "Input PIN -> :" + strValue);

                break;
            case "SWIPE INTO VIEW":
                Thread.sleep(500);
                AndroidDriver.findElement(MobileBy.IosUIAutomation("new UiScrollable(new UiSelector().scrollable(true).instance(1))" + ".scrollIntoView(new UiSelector().text(\""+strValue+"\").instance(0)"));
                appendLog.append(pathLog, "Scroll -> " + strObjectName + ": " + strValue);
                Thread.sleep(1000);
                break;
            case "SWIPE POPUP UP":
                Thread.sleep(500);
                //SWIPING AREA
                Dimension windowSizePopUp = AndroidDriver.manage().window().getSize();

                Integer widthPopUp = windowSizePopUp.getWidth();
                Integer heightPopUp = windowSizePopUp.getHeight();

                //Bulan
                Integer xPopUp = (widthPopUp/3*2)-widthPopUp/5;
                Integer yPopUp = heightPopUp/2+heightPopUp/4;


                new TouchAction((PerformsTouchActions) AndroidDriver)
                        .press(PointOption.point(xPopUp, yPopUp))
                        .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                        .moveTo(PointOption.point(xPopUp, yPopUp+150))
                        .release().perform();


                appendLog.append(pathLog, "Scroll UP -> " + strObjectName + ": " + strValue);
                break;
            case "SWIPE POPUP DOWN":
                Thread.sleep(500);
                //SWIPING AREA
                Dimension windowSizePopDown = AndroidDriver.manage().window().getSize();

                Integer widthPopDown = windowSizePopDown.getWidth();
                Integer heightPopDown = windowSizePopDown.getHeight();

                //Bulan
                Integer xPopDown = (widthPopDown/3*2)-widthPopDown/5;
                Integer yPopDown = heightPopDown/2+heightPopDown/4;


                new TouchAction((PerformsTouchActions) AndroidDriver)
                        .press(PointOption.point(xPopDown, yPopDown))
                        .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                        .moveTo(PointOption.point(xPopDown, yPopDown-heightPopDown/2))
                        .release().perform();


                appendLog.append(pathLog, "Scroll DOWN -> " + strObjectName + ": " + strValue);
                break;
            case "SWIPE OBJECT RIGHT":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                WebElement slider;
                slider = objValue;

                new TouchAction((PerformsTouchActions) AndroidDriver)
                        .press(PointOption.point(slider.getSize().getWidth()+slider.getSize().getWidth()/4,slider.getLocation().getY()))
                        .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                        .moveTo(PointOption.point(0,slider.getLocation().getY()))
                        .release().perform();
                appendLog.append(pathLog, "Swipe object Right -> " + strObjectName + ": " + strValue);

                break;
            case "SWIPE OBJECT LEFT":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                WebElement slider2;
                Dimension fullsize = AndroidDriver.manage().window().getSize();
                slider2 = objValue;

                new TouchAction((PerformsTouchActions) AndroidDriver)
                        .press(ElementOption.element(slider2))
                        .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                        .moveTo(PointOption.point(fullsize.getWidth(),slider2.getLocation().getY()))
                        .release().perform();
                appendLog.append(pathLog, "Swipe object Left -> " + strObjectName + ": " + strValue);

                break;

            case "TAP OBJECT":
                (new TouchActions(AndroidDriver)).singleTap(objValue);
                appendLog.append(pathLog, "Tap Object -> " + strObjectName + ": " + strValue);

                break;
            case "SWIPE PAID":
                    Thread.sleep(300);
                    Dimension windowSizeSwipe = AndroidDriver.manage().window().getSize();
                    Integer widthSwipe = windowSizeSwipe.getWidth();
                    Integer heightSwipe = windowSizeSwipe.getHeight();
                    Integer xTang = widthSwipe/10;
                    Integer yTang = heightSwipe-70;

                    new TouchAction((PerformsTouchActions) AndroidDriver)
                            .press(PointOption.point(xTang, yTang))
                            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                            .moveTo(PointOption.point(xTang*9, yTang))
                            .release().perform();
                break;

            case "IF EXIST":
                Boolean exist = loopWhileExist(objValue);
                if (exist==true){
                    executeTest.tempValidation.put("ifexist",true);
                }else{
                    executeTest.tempValidation.put("ifexist",false);
                }
                appendLog.append(pathLog, "IF EXIST -> " + strObjectName + ": " + strValue + " " +exist);
                break;

            case "SWITCH":
                executeTest.tempValidation.put("ifexist",false);
                if (!strObjectName.equalsIgnoreCase("")){
                    AndroidDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
                    Boolean intObjectExist = false;
                    try {
                        intObjectExist = objValue.isDisplayed();
                    }catch (Exception e){
                        intObjectExist = false;
                    }
                    AndroidDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                    if(intObjectExist==true){
                        executeTest.tempVar.put("Switch", "EXIST");
                        appendLog.append(pathLog, "SWITCH -> " + strObjectName + ": " + strValue + " EXIST");
                    }else{
                        executeTest.tempVar.put("Switch", "NOT EXIST");
                        appendLog.append(pathLog, "SWITCH -> " + strObjectName + ": " + strValue + " NOT EXIST");
                    }
                }else {

                    if (strValue.length() > 2){ //&& strValue.substring(0,3).contains("var")){
                        if (strValue.substring(0,3).contains("var")) {
                            String strValueTemp = executeTest.glbVarString.get(strValue);
                            if (strValueTemp.equalsIgnoreCase("TRUE")){
                                strValue = "EXIST";
                            }else{
                                strValue = "NOT EXIST";
                            }
                        }
                    }
                    executeTest.tempVar.put("Switch", strValue);
                    appendLog.append(pathLog, "SWITCH -> " + strValue);

                }
                break;

            case "CASE":
                //Jika casenya ada var
                if(!strObjectName.equalsIgnoreCase("") && strObjectName.length() > 2) {
                    if (strObjectName.substring(0, 3).contains("var")) {
                        strObjectName = executeTest.glbVarString.get(strObjectName);
                    }
                }

                if (executeTest.tempVar.get("Switch").equalsIgnoreCase(strObjectName)){
                    executeTest.tempValidation.put("ifexist",true);
                    executeTest.tempValidation.put("default",false);
                    appendLog.append(pathLog, "CASE -> Exist " + "Case 1:" + executeTest.tempVar.get("Switch") + " Case 2:" + strObjectName );


                }else{
                    executeTest.tempValidation.put("ifexist",false);
                    executeTest.tempValidation.put("default",true);
                    appendLog.append(pathLog, "CASE -> No Exist");
                    appendLog.append(pathLog, "CASE -> Exist " + "Case 1:" + executeTest.tempVar.get("Switch") + " Case 2:" + strObjectName );

                }
                break;

            case "DEFAULT":
                if (executeTest.tempValidation.get("default")==true){
                    executeTest.tempValidation.put("ifexist",true);
                    appendLog.append(pathLog, "Default -> Exist");
                }
                break;

            case "BREAK":
                executeTest.tempVar.put("Switch","");
                executeTest.tempValidation.put("ifexist",false);
                break;

            case "SCREENSHOT":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                takeSnapShot(AndroidDriver,pathPicture);
                appendLog.append(pathLog, "SCREENSHOT -> " + strObjectName + ": " + strValue);
                break;

            case "WAIT":
                Integer intWait = Integer.parseInt(strValue.replace(".0",""));
                Thread.sleep(intWait*1000);
                appendLog.append(pathLog, "WAIT -> " + strObjectName + ": " + strValue);
                break;

            case "LOOPCLICK":
                if (Integer.parseInt(strValue) > 1 ) {
                    for (Integer i=2; i<=Integer.parseInt(strValue);i++) {
                        objValue.click();
                        Thread.sleep(300);
                        appendLog.append(pathLog, "LOOPCLICK -> " + strObjectName + ": " + strValue);
                    }
                }
                break;

            case "SWIPE RIGHT":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                Dimension winSizeSwipe = AndroidDriver.manage().window().getSize();
                Integer screenWidth = winSizeSwipe.width;
                Integer screenWidth3 = screenWidth/3;

                Integer objY =objValue.getLocation().getY() ; //AndroidDriver.findElement(By.xpath("//../android.widget.TextView[@text='Total saldo simpanan']")).getLocation().getY();
                new TouchAction((PerformsTouchActions) AndroidDriver)
                        .press(PointOption.point((screenWidth3*2), objY))
                        .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                        .moveTo(PointOption.point(screenWidth3 - (screenWidth3/2), objY))
                        .release()
                        .perform();
                appendLog.append(pathLog, "SWIPE RIGHT -> " + strObjectName + ": " + strValue);
                break;

            case "SWIPE LEFT":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                Dimension winSizeSwipe2 = AndroidDriver.manage().window().getSize();
                Integer screenWidthh = winSizeSwipe2.width;
                Integer screenWidthh3 = screenWidthh/3;

                Integer objY2 = objValue.getLocation().getY() ; //AndroidDriver.findElement(By.xpath("//../android.widget.TextView[@text='Total saldo simpanan']")).getLocation().getY();
                new TouchAction((PerformsTouchActions) AndroidDriver)
                        .press(PointOption.point((screenWidthh3), objY2))
                        .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                        .moveTo(PointOption.point((screenWidthh3*2 + (screenWidthh3/2)), objY2))
                        .release()
                        .perform();
                appendLog.append(pathLog, "SWIPE LEFT -> " + strObjectName + ": " + strValue);
                break;

            case "LOOP WHILE EXIST":
                loopUntilNotExist(objValue);
                appendLog.append(pathLog, "LOOP WHILE EXIST -> " + strObjectName + ": " + strValue);
                break;
            case "LOOP UNTIL EXIST":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                appendLog.append(pathLog, "LOOP UNTIL EXIST -> " + strObjectName + ": " + strValue);
                break;
            case "MESSAGE BOX":
                JFrame Jf;
                Jf = new JFrame();
                Jf.setVisible(true);
                Jf.setAlwaysOnTop(true);
                JOptionPane.showMessageDialog(Jf, strValue);
                appendLog.append(pathLog, "SHOW MESSAGE BOX -> : " + strValue);
                break;

            case "GETTEXT":
                String Temp="";
                try{
                    if(objValue == null){
                        executeTest.glbVarString.put(strValue,"Object Tidak ditemukan");
                    }else{
                        Temp = objValue.getText();
                        executeTest.glbVarString.put(strValue,Temp);

                    }
                }catch (Exception e){
                    executeTest.glbVarString.put(strValue,"Object Tidak ditemukan");
                }
                appendLog.append(pathLog, "GETTEXT -> " + strObjectName + ": " + strValue + ":" + Temp);
                Temp = "";
                break;
            case "GETEXIST":
                Boolean exists = loopWhileGetExist(objValue);
                if(exists == false){
                    executeTest.glbVarString.put(strValue,"FALSE");
                }else{
                    executeTest.glbVarString.put(strValue,"TRUE");
                }
                appendLog.append(pathLog, "GETEXIST -> " + strObjectName + ": " + strValue +" : "+exists);
                break;
            case "RESULT":
                executeTest.arrKeterangan.get("Keterangan").add(strValue);
                break;
            case "RESTART APP":
                AndroidDriver.closeApp();
                Thread.sleep(5000);
                AndroidDriver.launchApp();
                AndroidDriver.activateApp("com.blu.stg1.Identifier6");
                break;
            case "SLIDER TENOR DEPOSIT":
                loopWhile(objValue, strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog);
                messageHandle(AndroidDriver,pathPicture);
                Thread.sleep(300);

                Integer intValue = Integer.parseInt(strValue);
                int spare;

                int y = objValue.getLocation().getY();
                String strBound = objValue.getAttribute("bounds").replaceAll("\\D"," ");
                String [] arrBound = strBound.split(" ");
                int x1 = Integer.parseInt(arrBound[1]);
                int y1 = Integer.parseInt(arrBound[2]);
                int tmp=0;
                if (intValue == 12){
                    tmp = 8;
                }
                int x2 = Integer.parseInt(arrBound[4]) - tmp;
                int y2 = Integer.parseInt(arrBound[5]);
                spare = ((x2 - x1) / 12);
                if (intValue < 12){
                    spare = (spare + (spare/4)) * intValue;
                }else {
                    spare = x2;
                }

                new TouchAction((PerformsTouchActions) AndroidDriver)
//                        .tap(PointOption.point(x2, y+10)) //378, 1288
                        .tap(PointOption.point(spare, y+10)) //378, 1288
                        .perform();
                appendLog.append(pathLog, "Tenor bluDeposit -> " + strObjectName + ": " + strValue + " bulan");
                break;
        }


    }

    private By getObjectBy(Document xmlObjectRepository, String objectName, String objectType) throws Exception {
        try{
            if(objectType.equalsIgnoreCase("XPATH")) {
                if (strXmlName.equalsIgnoreCase("custome")){
                    return By.xpath(getObjectXml(xmlObjectRepository, objectName).replace("strValue",strGlbValue));
                }else{
                    return By.xpath(getObjectXml(xmlObjectRepository, objectName));
                }
            }
            else if(objectType.equalsIgnoreCase("ID")){
                if (strXmlName.equalsIgnoreCase("custome")){
                    return By.id(getObjectXml(xmlObjectRepository, objectName).replace("strValue",strGlbValue));
                }else{
                    return By.id(getObjectXml(xmlObjectRepository, objectName));
                }
            }else if(objectType.equalsIgnoreCase("ACCESSIBILITY ID")){
                return MobileBy.AccessibilityId(getObjectXml(xmlObjectRepository, objectName));
            }else{
                throw new Exception("Wrong object type");
            }
        }catch (Exception e){
            throw new Exception("Wrong object type");
        }

    }

    private String getObjectXml(Document xmlObjectRepository, String objectName) {
        String strTextContent = "";

        NodeList nodeList = xmlObjectRepository.getElementsByTagName(objectName);
        for(int i=0; i<nodeList.getLength(); i++) {
            Node node =nodeList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                NodeList childList = node.getChildNodes();
                for(int j=0; j<childList.getLength(); j++) {
                    Node childNode =childList.item(j);
                    strTextContent = childNode.getTextContent();
                    break;
                }
            }
        }

        return strTextContent;
    }


    private IOSElement getObject(String strPageName, String strObjectName, String strValue, Document xmlObjectRepository, String strObjectType, String strKeyword, String pathLog, String strKeterangan) throws Exception{
        //TAMPUNG OBJECT DIHASMAP TEMPOBJECT DARI LAUNCH
        IOSElement objValue=null;
        if(!strPageName.equalsIgnoreCase("")) {
            String dtTitle;
            //STRING CUSTOME
            if (strPageName.equalsIgnoreCase("custome")) {
                dtTitle = strPageName+"_"+strObjectName+"_"+strValue.replace(" ","_");
            }else{
                dtTitle = strPageName+"_"+strObjectName;
            }

//            && dtTitle !="bluObjectLogin_txtPassword"
            if (launch.tempObject.containsKey(dtTitle)==true && strKeterangan.equalsIgnoreCase("AWAL") && !strKeyword.equalsIgnoreCase("IF EXIST") ){
                objValue = launch.tempObject.get(dtTitle);
            }else{
                Boolean objFound;
                if (strPageName.equalsIgnoreCase("custome")) {
                    objFound = loopWhileObject(xmlObjectRepository, strObjectName.replace("strValue", strValue), strObjectType,strValue,strKeyword);
                }else{
                    objFound = loopWhileObject(xmlObjectRepository, strObjectName, strObjectType,strValue,strKeyword);
                }
                IOSElement objValueTemp = null;

                //VALIDASI JIKA OBJECT KETEMU
                if(objFound==true){
                    if (strPageName.equalsIgnoreCase("custome")) {
                        objValueTemp = (IOSElement) AndroidDriver.findElement(getObjectBy(xmlObjectRepository, strObjectName.replace("strValue", strValue), strObjectType));
                    }else {
                        objValueTemp = (IOSElement) AndroidDriver.findElement(getObjectBy(xmlObjectRepository, strObjectName, strObjectType));
                    }

                    launch.tempObject.replace(dtTitle,launch.tempObject.get(dtTitle),objValueTemp);
                    objValue = objValueTemp;

                }else{
                    appendLog.append(pathLog, "Object "+ strPageName + ":" + strObjectName + ":"+ strValue +" tidak didapatkan");
                    objValue = null;
                }


            }

        }

        return objValue;

    }


    private Boolean loopWhileObject(Document xmlObjectRepository, String objectName, String objectType, String ObjValue, String keyword) throws Exception {
        boolean bolObjectEnable = false;
        boolean bolObjectDisplay = false;
        int intObjectExist = 0;
        AndroidDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

            //BUAT FLAG IF EXIST
            Integer intFlagLimit;
            if (keyword.equalsIgnoreCase("IF EXIST") || keyword.equalsIgnoreCase("LOOP WHILE EXIST") || keyword.equalsIgnoreCase("GETTEXT") || keyword.equalsIgnoreCase("GETEXIST") || keyword.equalsIgnoreCase("SWITCH")){
                intFlagLimit = 5;
            }else{
                intFlagLimit = 10;
            }

            int intFlag = 0;
            do{

                try{

                    if (strXmlName.equalsIgnoreCase("custome")){

                        intObjectExist =  AndroidDriver.findElements(getObjectBy(xmlObjectRepository, objectName, objectType)).size();

                        if(intObjectExist > 0) {
                            bolObjectEnable = AndroidDriver.findElement(getObjectBy(xmlObjectRepository, objectName.replace("strValue",ObjValue), objectType)).isEnabled();
                            bolObjectDisplay = AndroidDriver.findElement(getObjectBy(xmlObjectRepository, objectName.replace("strValue",ObjValue), objectType)).isDisplayed();
                        }else{
                            bolObjectEnable = false;
                            bolObjectDisplay = false;
                        }


                    }else{

                        intObjectExist =  AndroidDriver.findElements(getObjectBy(xmlObjectRepository, objectName, objectType)).size();

                        if(intObjectExist > 0) {
                            bolObjectEnable = AndroidDriver.findElement(getObjectBy(xmlObjectRepository, objectName, objectType)).isEnabled();
                            bolObjectDisplay = AndroidDriver.findElement(getObjectBy(xmlObjectRepository, objectName, objectType)).isDisplayed();
                        }else{
                            bolObjectEnable = false;
                            bolObjectDisplay = false;
                        }


                    }


                }catch (Exception e){
                    Thread.sleep(1000);
                    System.out.print("Error get Object \n");
                }


                if(intFlag == intFlagLimit){
                    intObjectExist  = 1;
                    bolObjectEnable = true;
                    bolObjectDisplay = true;
                }


                intFlag = intFlag + 1;
            }while(intObjectExist == 0 && bolObjectEnable == false && bolObjectDisplay == false);
            AndroidDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

            if (bolObjectEnable==true && bolObjectDisplay==true && intFlag < intFlagLimit+1){
                return true;
            }else{
                return false;
            }


    }

    private void loopWhile(IOSElement objectRepository, String strPageName, String strObjectName, String strValue, Document xmlObjectRepository, String strObjectType, String strKeyword, String pathLog) throws Exception {
        boolean bolObjectDisplay = false;
        Integer loopUntil = 10;

        AndroidDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        int intFlag = 0;
        do{
            try {
                bolObjectDisplay = objectRepository.isDisplayed();
                if(! strKeyword.equalsIgnoreCase("LOOP WHILE EXIST") && ! strKeyword.equalsIgnoreCase("LOOP UNTIL EXIST") && intFlag == 8 ){
                    IOSElement objectRepositoryTamp = getObject(strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog, "GAGAL");
                    appendLog.append(pathLog, "Kena error does not exist in DOM anymore");
                    if (objectRepositoryTamp!=null){
                        objectRepository = objectRepositoryTamp;
                    }
                    loopUntil = 2;
                }
            } catch (Exception e){
                if(e.getMessage().contains("element is no longer attached to the DOM") || e.getMessage().contains("does not exist in DOM anymore") && ! strKeyword.equalsIgnoreCase("LOOP WHILE EXIST") && ! strKeyword.equalsIgnoreCase("LOOP UNTIL EXIST")){
                    IOSElement objectRepositoryTamp = getObject(strPageName, strObjectName, strValue, xmlObjectRepository, strObjectType, strKeyword, pathLog, "GAGAL");
                    appendLog.append(pathLog, "Kena error does not exist in DOM anymore");
                    if (objectRepositoryTamp!=null){
                        objectRepository = objectRepositoryTamp;
                        objValue = objectRepositoryTamp;
                    }
                    loopUntil = 2;
                }
                Thread.sleep(1000);
            }
            intFlag = intFlag + 1;

        }while(bolObjectDisplay == false && intFlag <= loopUntil);

        AndroidDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

    }

    private void loopUntilNotExist(IOSElement objectRepository) throws Exception {
        boolean bolObjectDisplay = true;
        int intFlag = 0;
        int intSpare = 0;

        AndroidDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);


            do{
                try{
                    bolObjectDisplay = objectRepository.isDisplayed();
                    System.out.print("Loop ketemu\n");
                    Thread.sleep(1000);
                }catch (Exception e){
                    intSpare = intSpare + 1;
                    System.out.print("Loop tidak ketemu\n");
                }
                if(intSpare==3){
                    bolObjectDisplay = false;
                }
                intFlag = intFlag + 1;

            }while(bolObjectDisplay == true && intFlag <= 20) ;

        AndroidDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    private Boolean loopWhileExist(IOSElement objectRepository) throws Exception {
        boolean bolObjectDisplay = false;

        int intFlag = 0;

        AndroidDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);


            do{
                try {
                    bolObjectDisplay = objectRepository.isDisplayed();
                }catch (Exception e){
                    Thread.sleep(1000);
                    System.out.print("Gak dislpay di if exist \n");
                }
                intFlag = intFlag + 1;

            }while(bolObjectDisplay == false && intFlag <= 3) ;


            AndroidDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        if(bolObjectDisplay==true && intFlag<=5){
            return true;
        }else{
            return false;
        }


    }

    private Boolean loopWhileGetExist(IOSElement objectRepository) throws Exception {
        boolean bolObjectDisplay = false;

        int intFlag = 0;

        AndroidDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);


        do{
            try {
                bolObjectDisplay = objectRepository.isDisplayed();
            }catch (Exception e){
                Thread.sleep(1000);
                System.out.print("Gak dislpay di if exist \n");
            }
            intFlag = intFlag + 1;

        }while(bolObjectDisplay == false && intFlag <= 2) ;


        AndroidDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        if(bolObjectDisplay==true && intFlag<=2){
            return true;
        }else{
            return false;
        }


    }



    private void takeSnapShot(IOSDriver driver,String Path) throws Exception{

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

        TakesScreenshot scrShot =((TakesScreenshot)driver);
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        File DestFile=new File(fileWithPath);
        FileUtils.copyFile(SrcFile, DestFile);

        executeTest.tempInteger.put("glbNumImage",NumImage);

    }

    private void pushButtonPassword(Document xmlObjectRepository, String digit) throws Exception {

        Integer x = null,y = null;
        Integer widthDev = AndroidDriver.manage().window().getSize().getWidth();


        switch (digit.toUpperCase()) {


            case "0":
                //x = (AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn0", "XPATH")).getSize().getWidth()/2);
                x = widthDev/2;
                y = AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn0", "XPATH")).getLocation().getY()+AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn0", "XPATH")).getSize().getHeight()/2;
                break;
            case "1":
                x = (widthDev/3)-(widthDev/4);
                y = AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn1", "XPATH")).getLocation().getY()+AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn1", "XPATH")).getSize().getHeight()/2;
                break;
            case "2":
                x = widthDev/2;
                y = AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn1", "XPATH")).getLocation().getY()+AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn1", "XPATH")).getSize().getHeight()/2;
                break;
            case "3":
                x = (widthDev)-(widthDev/5);
                y = AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn1", "XPATH")).getLocation().getY()+AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn1", "XPATH")).getSize().getHeight()/2;
                break;
            case "4":
                x = (widthDev/3)-(widthDev/4);
                y = AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn4", "XPATH")).getLocation().getY()+AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn4", "XPATH")).getSize().getHeight()/2;
                break;
            case "5":
                x = widthDev/2;
                y = AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn4", "XPATH")).getLocation().getY()+AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn4", "XPATH")).getSize().getHeight()/2;
                break;
            case "6":
                x = (widthDev)-(widthDev/5);
                y = AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn4", "XPATH")).getLocation().getY()+AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn4", "XPATH")).getSize().getHeight()/2;
                break;
            case "7":
                x = (widthDev/3)-(widthDev/4);
                y = AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn7", "XPATH")).getLocation().getY()+AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn7", "XPATH")).getSize().getHeight()/2;
                break;
            case "8":
                x = widthDev/2;
                y = AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn7", "XPATH")).getLocation().getY()+AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn7", "XPATH")).getSize().getHeight()/2;
                break;
            case "9":
                x = (widthDev)-(widthDev/5);
                y = AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn7", "XPATH")).getLocation().getY()+AndroidDriver.findElement(getObjectBy(xmlObjectRepository, "btn7", "XPATH")).getSize().getHeight()/2;
                break;

        }

        TouchAction touchAction=new TouchAction(AndroidDriver);
        touchAction.tap(PointOption.point(x,y)).perform();
    }


    private void pushButtonOTP(String digit) throws Exception {

        switch (digit.toUpperCase()) {

            case "0":
                AndroidDriver.findElement(MobileBy.AccessibilityId("0")).click();
                break;
            case "1":
                AndroidDriver.findElement(MobileBy.AccessibilityId("1")).click();
                break;
            case "2":
                AndroidDriver.findElement(MobileBy.AccessibilityId("2")).click();
                break;
            case "3":
                AndroidDriver.findElement(MobileBy.AccessibilityId("3")).click();
                break;
            case "4":
                AndroidDriver.findElement(MobileBy.AccessibilityId("4")).click();
                break;
            case "5":
                AndroidDriver.findElement(MobileBy.AccessibilityId("5")).click();
                break;
            case "6":
                AndroidDriver.findElement(MobileBy.AccessibilityId("6")).click();
                break;
            case "7":
                AndroidDriver.findElement(MobileBy.AccessibilityId("7")).click();
                break;
            case "8":
                AndroidDriver.findElement(MobileBy.AccessibilityId("8")).click();
                break;
            case "9":
                AndroidDriver.findElement(MobileBy.AccessibilityId("9")).click();
                break;

        }

    }


    public void messageHandle(IOSDriver driver, String pathPicture) throws Exception {
        Boolean bolObjMess = false;


        AndroidDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

            try{
                //DEFINE OBJECT MESSAGE
                if (! launch.tempObject.containsKey("ErrorMsg")) {
                    IOSElement objValueTemp = (IOSElement) AndroidDriver.findElement(By.xpath("//../android.widget.TextView[@text='Coba Lagi']"));
                    launch.tempObject.put("ErrorMsg", objValueTemp);
                }
            }catch (Exception e){
            }

            if (launch.tempObject.containsKey("ErrorMsg")){
                Integer hitung = 1;
                do{
                    try {
                        bolObjMess = launch.tempObject.get("ErrorMsg").isDisplayed();
                    }catch (Exception e){
                        bolObjMess = false;
                    }

                    if(bolObjMess==true){
                        takeSnapShot(driver,pathPicture);
                        launch.tempObject.get("ErrorMsg").click();
                    }
                    hitung = hitung + 1;

                }while (bolObjMess==false && hitung<=3);

            }

        AndroidDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

    }


    public Integer getMonthInteger(String strGetBulan){

        Integer intGetBulan;
        switch (strGetBulan){
            case "Januari":
                intGetBulan = 1;
                break;
            case "Februari":
                intGetBulan = 2;
                break;
            case "Maret":
                intGetBulan = 3;
                break;
            case "April":
                intGetBulan = 4;
                break;
            case "Mei":
                intGetBulan = 5;
                break;
            case "Juni":
                intGetBulan = 6;
            case "Juli":
                intGetBulan = 7;
                break;
            case "Agustus":
                intGetBulan = 8;
                break;
            case "September":
                intGetBulan = 9;
                break;
            case "Oktober":
                intGetBulan = 10;
                break;
            case "November":
                intGetBulan = 11;
                break;
            case "Desember":
                intGetBulan = 12;
                break;
            default:
                intGetBulan = 0;
        }

        return intGetBulan;

    }


}


