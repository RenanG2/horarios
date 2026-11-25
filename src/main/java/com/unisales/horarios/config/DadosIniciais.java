package com.unisales.horarios.config;

import com.unisales.horarios.model.*;
import com.unisales.horarios.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.DayOfWeek;
import java.time.LocalTime;

/** Popula o banco com dados de exemplo na primeira execução (útil para os prints do relatório). */
@Configuration
public class DadosIniciais {

    @Bean
    CommandLineRunner seed(@Value("${app.seed.enabled:true}") boolean ativo,
                           ProfessorRepository professores, DisciplinaRepository disciplinas,
                           TurmaRepository turmas, HorarioRepository horarios) {
        return args -> {
            if (!ativo || professores.count() > 0) return;

            Professor ana = professor(professores, "Ana Souza", "ana.souza@unisales.br");
            Professor carlos = professor(professores, "Carlos Lima", "carlos.lima@unisales.br");

            Disciplina web = disciplina(disciplinas, "Programação Web", 80);
            Disciplina bd = disciplina(disciplinas, "Banco de Dados", 60);
            Disciplina es = disciplina(disciplinas, "Engenharia de Software", 60);

            Turma ads = turma(turmas, "ADS 3º Período", "2026/2");
            Turma si = turma(turmas, "SI 5º Período", "2026/2");

            horario(horarios, DayOfWeek.MONDAY, "19:00", "20:40", ana, web, ads);
            horario(horarios, DayOfWeek.WEDNESDAY, "19:00", "20:40", ana, web, si);
            horario(horarios, DayOfWeek.TUESDAY, "19:00", "20:40", carlos, bd, ads);
            horario(horarios, DayOfWeek.THURSDAY, "20:50", "22:30", carlos, es, si);
            horario(horarios, DayOfWeek.FRIDAY, "19:00", "20:40", carlos, es, ads);
        };
    }

    private Professor professor(ProfessorRepository r, String nome, String email) {
        Professor p = new Professor();
        p.setNome(nome);
        p.setEmail(email);
        return r.save(p);
    }

    private Disciplina disciplina(DisciplinaRepository r, String nome, int carga) {
        Disciplina d = new Disciplina();
        d.setNome(nome);
        d.setCargaHoraria(carga);
        return r.save(d);
    }

    private Turma turma(TurmaRepository r, String nome, String semestre) {
        Turma t = new Turma();
        t.setNome(nome);
        t.setSemestre(semestre);
        return r.save(t);
    }

    private void horario(HorarioRepository r, DayOfWeek dia, String ini, String fim,
                         Professor p, Disciplina d, Turma t) {
        Horario h = new Horario();
        h.setDiaSemana(dia);
        h.setHoraInicio(LocalTime.parse(ini));
        h.setHoraFim(LocalTime.parse(fim));
        h.setProfessor(p);
        h.setDisciplina(d);
        h.setTurma(t);
        r.save(h);
    }
}
