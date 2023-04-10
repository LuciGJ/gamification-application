package com.luci.gamification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.luci.gamification.utility.FileUploadUtil;
import com.luci.gamification.utility.QuestFilter;
import com.luci.gamification.utility.RandomStringBuilder;

class UtilityTests {

	@Test
	void generateToken() {
		String token = RandomStringBuilder.buildRandomString(30);
		assertThat(token.length()).isEqualTo(30);
	}

	@Test
	void getFileFormat() {
		String format = FileUploadUtil.getFileExtension("test.jpg");
		assertThat(format).isEqualTo(".jpg");
	}

	@Test
	void noFileFormat() {
		String format = FileUploadUtil.getFileExtension("test");
		assertThat(format).isEqualTo("");
	}

	@Test
	void questFilterFalseTest() {
		QuestFilter filter = new QuestFilter();
		filter.setAcceptedByString("");
		;
		assertThat(filter.isAccepted()).isFalse();
	}

	@Test
	void questFilterTrueTest() {
		QuestFilter filter = new QuestFilter();
		filter.setAcceptedByString("on");
		assertThat(filter.isAccepted()).isTrue();

	}
}
