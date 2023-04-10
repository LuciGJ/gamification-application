package com.luci.gamification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.luci.gamification.controller.AccountManagementController;
import com.luci.gamification.controller.AdminController;
import com.luci.gamification.controller.DataRecoveryController;
import com.luci.gamification.controller.LeaderboardController;
import com.luci.gamification.controller.LoginController;
import com.luci.gamification.controller.MainController;
import com.luci.gamification.controller.ProfileController;
import com.luci.gamification.controller.QuestController;
import com.luci.gamification.controller.RegistrationController;
import com.luci.gamification.controller.UserDetailController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

class ControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	AccountManagementController accountManagementController;

	@Autowired
	AdminController adminController;

	@Autowired
	DataRecoveryController dataRecoveryController;

	@Autowired
	LeaderboardController leaderboardController;

	@Autowired
	LoginController loginController;

	@Autowired
	MainController mainController;

	@Autowired
	ProfileController profileController;

	@Autowired
	QuestController questController;

	@Autowired
	RegistrationController registrationController;

	@Autowired
	UserDetailController userDetailController;

	@Test
	void contextLoads() {
		assertThat(accountManagementController).isNotNull();
		assertThat(adminController).isNotNull();
		assertThat(dataRecoveryController).isNotNull();
		assertThat(leaderboardController).isNotNull();
		assertThat(loginController).isNotNull();
		assertThat(mainController).isNotNull();
		assertThat(profileController).isNotNull();
		assertThat(questController).isNotNull();
		assertThat(registrationController).isNotNull();
		assertThat(userDetailController).isNotNull();
	}

	@Test
	void getRequestMainController() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("home"))
				.andExpect(content().contentType("text/html;charset=UTF-8"));
	}

	@Test
	void getRequestAccountManagementController() throws Exception {
		mockMvc.perform(get("/account/changeEmail")).andExpect(status().isFound());
	}

	@Test
	void getRequestAdminController() throws Exception {
		mockMvc.perform(get("/admin/administrationPage")).andExpect(status().isFound());
	}

	@Test
	void getRequestDataRecoveryController() throws Exception {
		mockMvc.perform(get("/recovery/forgotConfirmation")).andExpect(status().isOk())
				.andExpect(view().name("recovery/forgot-confirmation"))
				.andExpect(content().contentType("text/html;charset=UTF-8"));

	}

	@Test
	void getRequestLeaderboardController() throws Exception {
		mockMvc.perform(get("/leaderboard/viewLeaderboard")).andExpect(status().isFound());
	}

	@Test
	void getRequestLoginController() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login-page"))
				.andExpect(content().contentType("text/html;charset=UTF-8"));

	}

	@Test
	void getRequestProfileController() throws Exception {
		mockMvc.perform(get("/profile/viewProfile")).andExpect(status().isFound());
	}

	@Test
	void getRequestQuestController() throws Exception {
		mockMvc.perform(get("/quest/listQuests")).andExpect(status().isFound());
	}

	@Test
	void getRequestUserDetailController() throws Exception {
		mockMvc.perform(get("/userdata/listDetail")).andExpect(status().isFound());
	}

	@Test
	void getRequestRegisterController() throws Exception {
		mockMvc.perform(get("/register/registrationForm")).andExpect(status().isOk())
				.andExpect(view().name("registration-page"))
				.andExpect(content().contentType("text/html;charset=UTF-8"));

	}

}
