package action;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.IOException;

public class driverServer {
    private static AppiumDriverLocalService serviceAppium;

    public static void startAppiumServer(String IP, String port) {
        Runtime runtime = Runtime.getRuntime();
        String a = "cmd.exe /c start cmd.exe /k \"appium -a " + IP + " -p " + port +" --session-override";
        try {
            runtime.exec(a);
            Thread.sleep(10000);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopAppiumServer() {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("taskkill /F /IM node.exe");
            runtime.exec("taskkill /F /IM cmd.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
