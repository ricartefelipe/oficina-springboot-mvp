package br.com.oficina.ordemservico.application;

import br.com.oficina.ordemservico.infra.persistence.OrdemServicoJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.OffsetDateTime;

@Service
public class MetricasService {

    private final OrdemServicoJpaRepository osRepository;

    public MetricasService(OrdemServicoJpaRepository osRepository) {
        this.osRepository = osRepository;
    }

    @Transactional(readOnly = true)
    public TempoMedioExecucao calcularTempoMedioExecucao(OffsetDateTime from, OffsetDateTime to) {
        Double avgSeconds = osRepository.avgExecutionSeconds(from, to);
        Long count = osRepository.countExecutionMeasured(from, to);

        if (avgSeconds == null || count == null || count == 0) {
            return new TempoMedioExecucao(from, to, 0L, BigDecimal.ZERO, "PT0S");
        }

        BigDecimal avgMinutes = BigDecimal.valueOf(avgSeconds / 60.0)
                .setScale(2, RoundingMode.HALF_UP);

        Duration d = Duration.ofSeconds(Math.round(avgSeconds));
        return new TempoMedioExecucao(from, to, count, avgMinutes, d.toString());
    }

    public record TempoMedioExecucao(
            OffsetDateTime from,
            OffsetDateTime to,
            Long countConsidered,
            BigDecimal averageMinutes,
            String averageDurationIso
    ) {
    }
}
