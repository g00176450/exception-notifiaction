package com.marvin.util;

import com.marvin.model.DingContent;
import com.marvin.model.DingdingNotice;
import com.marvin.model.PiracyNotice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Describe: 发送钉钉通知的组件
 * @Date: 2021/03/01
 * @Author: Marvin
 */
@Slf4j
public class DingNoticeSendComponent<T extends PiracyNotice> implements NoticeSendComponent<PiracyNotice>{

	@Autowired
	private DingDingProperty dingDingProperty;

	DingContent content;
	
	private final PiracyNoticeTextResolver<PiracyNotice> resolver;
	
	private final Client client;

	public DingNoticeSendComponent(PiracyNoticeTextResolver<PiracyNotice> resolver,Client client) {
		this.client = client;
		this.resolver = resolver;
	}

	//将异常结构体组装好 
	@Override
	public void send(PiracyNotice exceptionNotice) {
		try {
			log.info("--------------->>>>>send<<<<<<<<-----------------------");
			String text = resolver.resolve(exceptionNotice);
			content = new DingContent();
			content.setContent(text);
			DingdingNotice dingdingNotice = dingDingProperty.createDingdingNotice(content);
			((DingdingNotice) dingdingNotice).setText(content);
			client.doSend(dingdingNotice);
		} catch (Exception e) {
			log.info(e.getCause().toString()+"\n \n");//TODO:加本项目特有的异常，然后用日志打印
			e.printStackTrace();
		}
	}
}
