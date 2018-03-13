package com.ht.connected.home.backend.controller.mqtt;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class MessageReceiveTest {

	@Test
	public void testPostMessage() throws Exception {
		while (true) {
			System.out.println("TestPost");
			postMessage();
		}
	}

	private void postMessage() throws Exception {
		Random rdm = new Random();

		List<String> productNames = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("hyr");
				add("tcl");
			}
		};

		List<String> suberIds = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("suber1");
				add("suber2");
				add("suber3");
			}
		};

		String productName = rdm.nextBoolean() ? productNames.get(rdm.nextInt(productNames.size())) : null;
		String topic = null;
		List<String> randomSubSuberIds = Lists.newArrayList();

		if (StringUtils.isNotBlank(productName)) {
			topic = "common";
		} else {
			List<String> list = Splitter.on(",").splitToList("all,individual");
			topic = list.get(rdm.nextInt(list.size()));
		}

		if (topic.equals("individual")) {
			Collections.shuffle(suberIds);
			randomSubSuberIds = suberIds.subList(0, rdm.nextInt(suberIds.size()) + 1);
		}

		StringBuilder message = new StringBuilder("您好！");

		if (CollectionUtils.isNotEmpty(randomSubSuberIds)) {
			message.append(randomSubSuberIds + "!");
		}

		if (StringUtils.isNotBlank(productName)) {
			message.append("您有一条来自 " + productName + " 的消息。");
		}

		Thread.sleep(2000);
	}

}