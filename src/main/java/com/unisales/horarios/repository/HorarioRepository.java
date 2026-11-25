package com.unisales.horarios.repository;

import com.unisales.horarios.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByProfessorId(Long professorId);

    List<Horario> findByTurmaId(Long turmaId);

    boolean existsByProfessorId(Long professorId);
    boolean existsByDisciplinaId(Long disciplinaId);
    boolean existsByTurmaId(Long turmaId);

    /*
     * Dois intervalos se sobrepõem quando: inicioExistente < fimNovo E fimExistente > inicioNovo.
     * "ignorarId" permite ignorar o próprio registro ao editar (use -1 ao criar).
     */
    @Query("""
            select count(h) > 0 from Horario h
            where h.professor.id = :professorId
              and h.diaSemana = :dia
              and h.horaInicio < :fim
              and h.horaFim > :inicio
              and h.id <> :ignorarId
            """)
    boolean existeConflitoProfessor(@Param("professorId") Long professorId,
                                    @Param("dia") DayOfWeek dia,
                                    @Param("inicio") LocalTime inicio,
                                    @Param("fim") LocalTime fim,
                                    @Param("ignorarId") Long ignorarId);

    @Query("""
            select count(h) > 0 from Horario h
            where h.turma.id = :turmaId
              and h.diaSemana = :dia
              and h.horaInicio < :fim
              and h.horaFim > :inicio
              and h.id <> :ignorarId
            """)
    boolean existeConflitoTurma(@Param("turmaId") Long turmaId,
                                @Param("dia") DayOfWeek dia,
                                @Param("inicio") LocalTime inicio,
                                @Param("fim") LocalTime fim,
                                @Param("ignorarId") Long ignorarId);
}
