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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @WebMvcTest loads only the web layer (this controller + MVC infrastructure),
 * not the whole app -- no real database, no Tomcat. We @Import our REAL
 * SecurityConfig so these tests check our actual authorization rules, not
 * Spring Boot's generic defaults. MerchantVerificationService is @MockBean'd
 * since the controller needs *a* bean of that type to construct, but we don't
 * want a real one touching a database in a "web layer only" test.
 *
 * @WebMvcTest also loads @ControllerAdvice classes, so AdminNotificationAdvice
 * runs too -- it uses the same mocked MerchantVerificationService.
 */
@WebMvcTest(EmployeeMerchantController.class)
@Import({SecurityConfig.class, RoleBasedAuthenticationSuccessHandler.class})
class EmployeeMerchantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MerchantVerificationService merchantVerificationService;

    // @WithMockUser builds a generic Spring Security User as the principal --
    // not our AppUserPrincipal. That's fine for tests that only check role
    // gating, but methods that call principal.getId() need a REAL
    // AppUserPrincipal wrapping a PlatformEmployee, supplied via the
    // user(UserDetails) request post-processor instead.
    private AppUserPrincipal employee() {
        return new AppUserPrincipal(new PlatformEmployee("employee@mall.local", "hash", "Test Employee"));
    }

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
    @WithMockUser(roles = "CUSTOMER")
    void customerRole_cannotSeeAllShops() throws Exception {
        mockMvc.perform(get("/employee/merchants"))
                .andExpect(status().isForbidden());
    }

    @Test
    void employeeRole_canViewAllShops() throws Exception {
        when(merchantVerificationService.listAll()).thenReturn(List.of());

        mockMvc.perform(get("/employee/merchants").with(user(employee())))
                .andExpect(status().isOk());
    }

    @Test
    void employeeRole_canViewPendingList_andItIsMarkedSeen() throws Exception {
        AppUserPrincipal principal = employee();
        when(merchantVerificationService.listPending()).thenReturn(List.of());

        mockMvc.perform(get("/employee/merchants/pending").with(user(principal)))
                .andExpect(status().isOk());

        verify(merchantVerificationService).markPendingSeen(principal.getId());
    }

    @Test
    void employeeRole_canApprove_withCsrfToken() throws Exception {
        mockMvc.perform(post("/employee/merchants/1/approve").with(user(employee())).with(csrf()))
                .andExpect(redirectedUrl("/employee/merchants/pending"));
    }

    @Test
    void employeeRole_canReinstate_withCsrfToken() throws Exception {
        mockMvc.perform(post("/employee/merchants/1/reinstate").with(user(employee())).with(csrf()))
                .andExpect(redirectedUrl("/employee/merchants"));
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
