package br.com.lucasferreira.sh.Models;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
public class Internacao {
    private Paciente paciente;
    private final Medico medicoResponsavel;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    public boolean ativa;
    private final int quarto;



    public Internacao(Paciente paciente, Medico medicoResponsavel, int quarto){
        this.paciente = paciente;
        this.medicoResponsavel = medicoResponsavel;
        this.dataEntrada = LocalDateTime.now();
        this.ativa = true;
        this.quarto = quarto;
    }
    public Internacao(Paciente paciente, Medico medicoResponsavel, int quarto, LocalDateTime dataEntrada, LocalDateTime dataSaida, boolean ativa) {
        this.paciente = paciente;
        this.medicoResponsavel = medicoResponsavel;
        this.quarto = quarto;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.ativa = ativa;
    }
    public long getDuracaoEmDias(){
        LocalDateTime dataFinal = (this.dataSaida == null) ? LocalDateTime.now() : this.dataSaida;
        //(condição) ? valor_se_verdadeiro : valor_se_falso;
        long dias = ChronoUnit.DAYS.between(dataEntrada, dataFinal);
        // se a diferenca da data de entrada e data final  ser igual a 0 ent retorna 1 dia se n, retorna os dias
        return dias == 0 ? 1 : dias;
    }
    public void darAlta() {
        this.dataSaida = LocalDateTime.now();
        this.ativa = false;
    }
    public Paciente getPaciente() {
        return paciente;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }
    public Medico getMedicoResponsavel() { return medicoResponsavel; }
    public int getQuarto() { return quarto; }
}
