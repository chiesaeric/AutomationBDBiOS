package action;


//import org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle;
import log.appendLog;
import org.apache.poi.ss.usermodel.Row;
import testcase.executeTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class verify {

    public static void validate(String[] param, String pathLog, Row rowVerConf, String Keterangan) throws IOException {
        /*
        KEYWORD
        OPERATION
        VARIABLE
        BANDINGAN
        OTHER
        VARIABLE RESULT
        REMARK
         */

        String strKeyWord,strOperation,strVariable,strBanding,strOther,strVarResult,strRemark;
        strKeyWord = param[0];
        strOperation = param[1];
        strVariable = param[2];
        strBanding = param[3];
        strOther = param[4];
        strVarResult = param[5];
        strRemark = param[6];



        switch (strKeyWord.toUpperCase()) {
            case "SPLIT":
                //DECLARE VARIABLE
                String ArrTemp[],strTemp;

                //GET VARIABLE FROM GLOBAL
                strTemp = getVariable(strVariable);
                if (strTemp.equalsIgnoreCase("")){
                    //Exit
                    return;
                }

                ArrTemp = strTemp.split(strOther);

                executeTest.glbVarArray.put(strVarResult, new ArrayList<String>());
                for(int i=0; i <= ArrTemp.length-1; i++){
                    executeTest.glbVarArray.get(strVarResult).add(ArrTemp[i]);
                }

                break;
            case "REPLACE":
                //DECLARE VARIABLE
                String strTempRep, arrSplit[], other2;

                //GET VARIABLE FROM GLOBAL
                strTempRep = getVariable(strVariable);
                if (strTempRep.equalsIgnoreCase("")){
                    //Exit
                    return;
                }

                arrSplit = strOther.split(";");
                if(arrSplit[1].equalsIgnoreCase("kosong")){
                    other2 = "";
                }else{
                    other2 = arrSplit[1];
                }

                executeTest.glbVarString.put(strVarResult,strTempRep.replace(arrSplit[0],other2));


                break;
            case "REPLACE ALL":
                //DECLARE VARIABLE
                String strTempRepAll, arrSplitAll[], other2All;

                //GET VARIABLE FROM GLOBAL
                strTempRepAll = getVariable(strVariable);
                if (strTempRepAll.equalsIgnoreCase("")){
                    //Exit
                    return;
                }

                arrSplitAll = strOther.split(";");
                if(arrSplitAll[1].equalsIgnoreCase("kosong")){
                    other2All = "";
                }else{
                    other2All = arrSplitAll[1];
                }

                executeTest.glbVarString.put(strVarResult,strTempRepAll.replaceAll(arrSplitAll[0],other2All));


                break;
            case "TRIM":
                //DECLARE VARIABLE
                String  strTempTrim;

                //GET VARIABLE FROM GLOBAL
                strTempTrim = getVariable(strVariable);
                strTempTrim = strTempTrim.trim();
                if (strTempTrim.equalsIgnoreCase("")){
                    //Exit
                    return;
                }
                executeTest.glbVarString.put(strVarResult,strTempTrim);
                break;

            case "SUBSTR":
                //DECLARE VARIABLE
                String strTempSub, strTempHasil, arrSplitSub[];
                Integer sub1,sub2;

                //GET VARIABLE FROM GLOBAL
                strTempSub = getVariable(strVariable);
                if (strTempSub.equalsIgnoreCase("")){
                    //Exit
                    return;
                }


                if(strOther.contains(";")){
                        arrSplit = strOther.split(";");
                        if(arrSplit[0].equalsIgnoreCase("LEN")){
                            sub1 = strTempSub.length();
                        }else{
                            sub1 = Integer.parseInt(arrSplit[0]);
                        }

                        if(arrSplit[1].equalsIgnoreCase("LEN")){
                            sub2 = strTempSub.length();
                        }else{
                            sub2 = Integer.parseInt(arrSplit[1]);
                        }

                }else{
                    sub1 = Integer.parseInt(strOther);
                    sub2 = 0;
                }

                if(strOperation.equalsIgnoreCase("MID")){
                    strTempHasil = strTempSub.substring(sub1,sub2);
                }else if(strOperation.equalsIgnoreCase("LEFT")){
                    strTempHasil = strTempSub.substring(1,sub1);
                }else if(strOperation.equalsIgnoreCase("RIGHT")){
                    strTempHasil = strTempSub.substring(sub1,strTempSub.length());
                }else{
                    strTempHasil = "";
                }

                executeTest.glbVarString.put(strVarResult,strTempHasil);


                break;
            case "GET":
                //DECLARE VARIABLE
                Boolean ArrKetemu=false;
                String[] arrVariable = strVariable.replace("[","/").split("/");
                Integer paramArr = Integer.parseInt(arrVariable[1].replace("]",""));
                String strSplit = arrVariable[0];

                ArrKetemu = executeTest.glbVarArray.containsKey(strSplit);
                if(ArrKetemu==true){
                    executeTest.glbVarString.put(strVarResult,executeTest.glbVarArray.get(strSplit).get(paramArr));
                }else{
                    System.out.print("Variable "+ arrVariable +" tidak ditemukan");
                }


            break;

            case "OPERATION":
                //DECLARE VARIABLE
                Long var1,var2,hasil;
                String strTemp1,strTemp2;

                //GET VARIABLE FROM GLOBAL
                strTemp1 = getVariable(strVariable);
                strTemp2 = getVariable(strBanding);
                if (strTemp1.equalsIgnoreCase("")){
                    //Exit
                    return;
                }else if(strTemp2.equalsIgnoreCase("")) {
                    //Exit
                    return;
                }



                var1 = Long.parseLong(strTemp1);
                var2 = Long.parseLong(strTemp2);

                if(strOperation.equalsIgnoreCase("ADDITION")){
                    hasil = var1 + var2;
                }else if(strOperation.equalsIgnoreCase("SUBTRACTION")) {
                    hasil = var1 - var2;
                }else if(strOperation.equalsIgnoreCase("MULTIPLICATION")) {
                    hasil = var1 * var2;
                }else if(strOperation.equalsIgnoreCase("DIVISION")) {
                    hasil = var1 / var2;
                }else{
                    hasil = 0L;
                }
                executeTest.glbVarString.put(strVarResult,hasil.toString());
                appendLog.append(pathLog, "OPERATION -> " + var1 + " " + strOperation + " " + var2 + "=" + hasil);

                break;

            case "VERIFY":
                executeTest.tempInteger.put("rowVerifCount", executeTest.tempInteger.get("rowVerifCount")+1);
                String strVerfConf = rowVerConf.getCell(executeTest.tempInteger.get("rowVerifCount")).toString();

                if(strVerfConf.equalsIgnoreCase("Y") || strVerfConf.equalsIgnoreCase("")){

                    //DECLARE VARIABLE
                    String varVal1,varVal2;
                    executeTest.tempInteger.put("glbTotalCase",executeTest.tempInteger.get("glbTotalCase")+1);
                    executeTest.tempInteger.put("flgCase", executeTest.tempInteger.get("flgCase")+1);

                    //
                    System.out.println("Proses Verify");
                    System.out.println(strRemark);
                    String strTempRemark = strRemark;
                    Set<String> keys = executeTest.glbVarString.keySet();
                    for(String key: keys){
                        strTempRemark = strTempRemark.replace(key,executeTest.glbVarString.get(key));
                    }
                    strRemark = strTempRemark;
                    //

                    varVal1 = getVariable(strVariable);
                    varVal2 = getVariable(strBanding);
                    if (varVal1.equalsIgnoreCase("")){
                        //Exit
                        if(executeTest.glbVarString.get("reportResult").equalsIgnoreCase("") && Keterangan.equalsIgnoreCase("Berhasil")){
                            executeTest.tempInteger.put("flgCasePassed", executeTest.tempInteger.get("flgCasePassed")+1);
                        }
                        return;
                    }else if(varVal2.equalsIgnoreCase("")) {
                        varVal2 = strBanding;
                    }

                    //Validation
                    if(strOperation.equalsIgnoreCase("") || strOperation.equalsIgnoreCase("CONTAINS"))
                    {
                        if(!varVal1.equalsIgnoreCase(varVal2)){
                            if(!executeTest.glbVarString.get("reportResult").equalsIgnoreCase("")){
                                executeTest.glbVarString.put("reportResult",executeTest.glbVarString.get("reportResult")+"\n- "+strRemark);
                            }else{
                                executeTest.glbVarString.put("reportResult","- "+strRemark);
                            }
                            executeTest.tempInteger.put("flgCaseFailed", executeTest.tempInteger.get("flgCaseFailed")+1);
                        }else{
                            executeTest.tempInteger.put("flgCasePassed", executeTest.tempInteger.get("flgCasePassed")+1);
                        }

                    }else if(strOperation.equalsIgnoreCase("NOT CONTAINS")){
                        if(varVal1.equalsIgnoreCase(varVal2)){
                            if(!executeTest.glbVarString.get("reportResult").equalsIgnoreCase("")){
                                executeTest.glbVarString.put("reportResult",executeTest.glbVarString.get("reportResult")+"\n- "+strRemark);
                            }else{
                                executeTest.glbVarString.put("reportResult","- "+strRemark);
                            }
                            executeTest.tempInteger.put("flgCaseFailed", executeTest.tempInteger.get("flgCaseFailed")+1);
                        }else{
                            executeTest.tempInteger.put("flgCasePassed", executeTest.tempInteger.get("flgCasePassed")+1);
                        }
                    }
                    appendLog.append(pathLog, "VERIFY -> "+ strVariable + " = "+ varVal1 + " : "+ strBanding + " = " + varVal2);

                }


                break;

        }


    }



    private static String getVariable(String strTemp){

        //DECLARE VARIABLE
        String strKetemu = "";
        Boolean ketemu = false,stringKetemu,longKetemu = false;

        try{
            stringKetemu = executeTest.glbVarString.containsKey(strTemp);
            if (stringKetemu==true){
                strKetemu = executeTest.glbVarString.get(strTemp);
                ketemu = true;
            }
        }catch (Exception e){}

        try{
            if (ketemu == false){
                longKetemu = executeTest.glbVarString.containsKey(strTemp);
                if (longKetemu==true){
                    strKetemu = executeTest.glbVarLong.get(strTemp).toString();
                    ketemu = true;
                }

            }
        }catch (Exception e){}

        if(ketemu==false){
            System.out.print("Variable "+ strTemp +" tidak ditemukan \n");
        }

        return strKetemu;
    }


}
