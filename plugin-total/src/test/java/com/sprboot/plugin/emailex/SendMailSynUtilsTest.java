package com.sprboot.plugin.emailex;

import com.sprboot.plugin.emailex.bean.ServiceResponse;
import com.sprboot.plugin.emailex.bean.param.SendMailParam;
import com.sprboot.plugin.emailex.util.MailParamUtils;
import com.sprboot.plugin.emailex.util.SendMailSynUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 编  号：
 * 名  称：SendMailSynUtilsTest
 * 描  述：SendMailUtil测试
 * 完成日期：2020/3/29 19:27
 * @author：felix.shao
 */
@Slf4j
public class SendMailSynUtilsTest {

	public static final String ACCOUNT = "shjLife@163.com";
	
	public static final String PASSWORD = "a1321916356";
	
	public static final String RECIPIENT = "728060301@qq.com";
	
	@Test
	public void sendMultipleEmailTest(){
		List<String> addressList = new ArrayList<String>();
		addressList.add(RECIPIENT);
		
		//发送人,收件人地址
		InternetAddress sender = null;
		InternetAddress[] toRecipients = null;
		try {
			sender = new InternetAddress(ACCOUNT);
			sender.setPersonal("发件人别名");
			toRecipients = MailParamUtils.parseInternetAddress(addressList);
		} catch (AddressException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//邮件附件
//		List<AttachFileParam> attachFiles = new ArrayList<AttachFileParam>();
//		
//		AttachFileParam file1 = AttachFileParam.Builder
//				.path("D:\\attach1.txt")
//				.name("attach1.txt");
//		attachFiles.add(file1);
//		
//		AttachFileParam file2 = AttachFileParam.Builder
//				.path("D:\\attach2.txt")
//				.name("attach2.txt");
//		attachFiles.add(file2);
		
		//邮件正文
		StringBuffer contentBuf = new StringBuffer();
		contentBuf.append(
				"<span style='color:red;'>这是我自己用java mail发送的邮件哦...</span>");
		
		SendMailParam param = SendMailParam.builder()
				.protocol("smtp")
				.host("smtp.163.com")
				.port("25")
				.isAuth("true")
				.isEnabledDebugMod("true")
				.sender(sender) //发件人及发件人账号,请用自己的账号,密码
				.account(ACCOUNT)
				.password(PASSWORD) //发件人密码,请用自己的账号,密码
				.sentDate(new Date())
				.subject("使用JavaMail发送混合组合类型的邮件测试主题")
//				.attachFiles(attachFiles)
				.content(contentBuf.toString())
				.toRecipients(toRecipients).build();
		
		param.initDefaultProps();
		
//		ZipParam zipParam = ZipParam.Builder
//				.name("附件列表.zip")
//				.path("C:/Users/openstack/Desktop/temp/zipTemp/附件列表.zip");
//		
//		param.zipFlag(true);
//		param.zipParam(zipParam);
		
		ServiceResponse<String> serviceResponse = SendMailSynUtils.sendEmail(param);
		
		log.info("\n-->errorNO = "+ serviceResponse.getErrorNO());
		log.info("\n-->errorMsg = "+ serviceResponse.getErrorMsg());
	}
	
}
