package com.sliit.se2030.mall.user.controller;

import com.sliit.se2030.mall.config.SecurityConfig;
import com.sliit.se2030.mall.security.AppUserPrincipal;
import com.sliit.se2030.mall.security.RoleBasedAuthenticationSuccessHandler;
import com.sliit.se2030.mall.user.entity.PlatformEmployee;
import com.sliit.se2030.mall.user.service.MerchantVerificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @WebMvcTest loads only the web layer (this controller + MVC infrastructure),
 * not the whole app -- no real database, no Tomcat. We @Import our REAL
 * SecurityConfig so these tests check our actual authorization rules, not
 * Spring Boot's generic defaults. MerchantVerificationService is @MockBean'd
 * since the controller needs *a* bean of that type to construct, but we don't
 * want a real one touching a database in a "web layer only" test.
 */
@WebMvcTest(EmployeeMerchantController.class)
@Import({SecurityConfig.class, RoleBasedAuthenticationSuccessHandler.class})
class EmployeeMerchantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MerchantVerificationService merchantVerificationService;

    @Test
    void anonymousUser_isRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/employee/merchants/pending"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void customerRole_isForbidden() throws Exception {
        mockMvc.perform(get("/employee/merchants/pending"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "PLATFORM_EMPLOYEE")
    void employeeRole_canViewPendingList() throws Exception {
        when(merchantVerificationService.listPending()).thenReturn(List.of());

        mockMvc.perform(get("/employee/merchants/pending"))
                .andExpect(status().isOk());
    }

    // @WithMockUser builds a generic Spring Security User as the principal --
    // not our AppUserPrincipal. That's fine for tests that only check role
    // gating, but this controller method calls principal.getId(), so it needs
    // a REAL AppUserPrincipal wrapping a PlatformEmployee, supplied via the
    // user(UserDetails) request post-processor instead.
    @Test
    void employeeRole_canApprove_withCsrfToken() throws Exception {
        AppUserPrincipal principal = new AppUserPrincipal(
                new PlatformEmployee("employee@mall.local", "hash", "Test Employee"));

        mockMvc.perform(post("/employee/merchants/1/approve").with(user(principal)).with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "PLATFORM_EMPLOYEE")
    void approve_withoutCsrfToken_isForbidden() throws Exception {
        mockMvc.perform(post("/employee/merchants/1/approve"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MERCHANT")
    void merchantRole_cannotApprove() throws Exception {
        mockMvc.perform(post("/employee/merchants/1/approve").with(csrf()))
                .andExpect(status().isForbidden());
    }
}
