package action;


import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import testcase.executeTest;

import java.util.ArrayList;

public class mail {

    public static void sendMail(String pathReport) throws AddressException {
        /*
         */

        // Recipient's email ID needs to be mentioned.
        //String to = "edwin.dwi@bcadigital.co.id, erik.chiesa@bcadigital.co.id";
        String to = "rimon_khoe@bca.co.id, setiawan.aliong@bcadigital.co.id, anthony.gunawan@bcadigital.co.id";
        InternetAddress [] toAdress = InternetAddress.parse(to, true);

        //CC
        //String cc = "ravy.sukmana@bcadigital.co.id";
        String cc = "jessica_belinda@bca.co.id, edwin.dwi@bcadigital.co.id, erik.chiesa@bcadigital.co.id, ravy.sukmana@bcadigital.co.id";

        InternetAddress [] ccAdress = InternetAddress.parse(cc, true);


        // Sender's email ID needs to be mentioned
        String from = "otomasibcad@gmail.com"; ////

        // Assuming you are sending email from localhost
        String host =  "smtp.gmail.com"; //"smtp.office365.com"; //
        String user = "otomasibcad@gmail.com"; // "edwin.dwi@bcadigital.co.id";//
        String pass = "Password123@"; // //

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable",true);
        properties.put("mail.smtp.port", "587");

        // Get the default Session object.
        //Session session = Session.getDefaultInstance(properties);
        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,pass);
                    }
                });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipients(Message.RecipientType.TO,toAdress);

            // Set CC: header field of the header.
            message.addRecipients(Message.RecipientType.CC,ccAdress);


            // Set Subject: header field
            //message.setSubject("TRANSACTION AUTOMATED TESTING BLU");
            message.setSubject("SCHEDULER TRANSACTION BLU TO ALTO");
            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            //messageBodyPart.setText("Dear All,\n\nFYI\nTransaksi Regresi blu telah dijalankan, berikut telah terlampir hasil laporan regresi blu.\n\nTerima kasih.\n\n\n\n\nSending Automatic from Testing Team - Automation");
            messageBodyPart.setText("Dear All,\n\nFYI\nScheduler transaksi blu ke alto telah dijalankan, berikut telah terlampir hasil laporan scheduler blu.\n\nTerima kasih.\n\n\n\n\nSending Automatic from Testing Team - Automation");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = pathReport;
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename.replace("C:\\CaptureScreen\\Report\\Blu\\",""));
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart );

            // Send message
            Transport.send(message, message.getAllRecipients());
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }



}
