package com.unisales.horarios;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "app.seed.enabled=false"
})
@AutoConfigureMockMvc
class HorarioApiTests {

    private static final RequestPostProcessor ADMIN = httpBasic("admin", "admin123");

    @Autowired
    MockMvc mvc;

    @Test
    void semCredenciaisRetorna401() throws Exception {
        mvc.perform(get("/api/professores")).andExpect(status().isUnauthorized());
    }

    @Test
    void professorInvalidoRetorna400() throws Exception {
        mvc.perform(post("/api/professores").with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\",\"email\":\"invalido\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void conflitoDeHorarioRetorna409EAgendaFunciona() throws Exception {
        long prof = criar("/api/professores", "{\"nome\":\"Teste\",\"email\":\"teste@unisales.br\"}");
        long disc = criar("/api/disciplinas", "{\"nome\":\"Redes\",\"cargaHoraria\":60}");
        long turma = criar("/api/turmas", "{\"nome\":\"T1\",\"semestre\":\"2026/2\"}");

        String horario = """
                {"diaSemana":"MONDAY","horaInicio":"08:00","horaFim":"10:00",
                 "professorId":%d,"disciplinaId":%d,"turmaId":%d}
                """.formatted(prof, disc, turma);

        mvc.perform(post("/api/horarios").with(ADMIN).contentType(MediaType.APPLICATION_JSON).content(horario))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/horarios").with(ADMIN).contentType(MediaType.APPLICATION_JSON).content(horario))
                .andExpect(status().isConflict());

        mvc.perform(get("/api/professores/" + prof + "/horarios").with(ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aulas", hasSize(1)))
                .andExpect(jsonPath("$.disciplinas[0].nome").value("Redes"));
        mvc.perform(get("/api/turmas/" + turma + "/horarios").with(ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.professores[0].nome").value("Teste"));
    }

    private long criar(String url, String json) throws Exception {
        String body = mvc.perform(post(url).with(ADMIN).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(body, "$.id")).longValue();
    }
}
