package br.com.oficina.shared.security;

import br.com.oficina.cadastros.cliente.api.admin.AdminClienteController;
import br.com.oficina.cadastros.cliente.application.ClienteService;
import br.com.oficina.cadastros.cliente.domain.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Garante 401/403/200 nos endpoints admin com OAuth2 Resource Server (JWT),
 * sem subir PostgreSQL (corre no perfil {@code ci}).
 */
@WebMvcTest(controllers = AdminClienteController.class)
@Import({SecurityConfig.class, AdminJwtSecurityWebMvcTest.ClienteServiceStubConfig.class})
@EnableConfigurationProperties(SecurityJwtProperties.class)
@TestPropertySource(properties = {
        "security.jwt.jwk-set-uri=http://localhost/dummy",
        "security.jwt.allowed-issuers="
})
class AdminJwtSecurityWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarSemTokenRetorna401() throws Exception {
        mockMvc.perform(get("/admin/clientes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listarComJwtSemRoleAdminRetorna403() throws Exception {
        mockMvc.perform(get("/admin/clientes")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void listarComRoleAdminRetorna200() throws Exception {
        mockMvc.perform(get("/admin/clientes")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    /**
     * Stub sem Mockito (compatível com JDK 21+ onde inline mock pode falhar).
     */
    static class ClienteServiceStubConfig {

        @Bean
        @Primary
        ClienteService clienteServiceStub() {
            return new ClienteService(null) {
                @Override
                public List<Cliente> listar() {
                    return List.of();
                }
            };
        }
    }
}
