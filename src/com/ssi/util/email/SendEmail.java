package com.ssi.util.email;
import com.ssi.main.SSIConfig;
public class SendEmail {

    /*** 以后需要两个参数：接收方地址 、 内容 ***/
    public static void send(String subject, String toaddress, String content, String attachement) throws Exception {

        String hostName = SSIConfig.get("email.smtp");
        String fromAddress = SSIConfig.get("email.address");
        String fromAPass = SSIConfig.get("email.password");

        EmailHandle emailHandle = new EmailHandle(hostName);
        emailHandle.setFrom(fromAddress);
        emailHandle.setNeedAuth(true);
        emailHandle.setSubject(subject);
        emailHandle.setBody(content);
        emailHandle.setTo(toaddress);
        emailHandle.setFrom(fromAddress);
        if(attachement != null){
        	emailHandle.addFileAffix(attachement);// 附件文件路径
        }
        emailHandle.setNamePass(fromAddress, fromAPass);
        emailHandle.sendEmail();
    }

    public static void main(String[] args) {
        try {
            SendEmail.send("带附件的邮件测试", SSIConfig.get("email.recipients"), "测试内容<a href='http://www.crazyiter.com'>疯狂的IT人</a>", "res/template/Book1.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
