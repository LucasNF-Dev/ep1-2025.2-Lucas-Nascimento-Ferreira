package br.com.lucasferreira.sh.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Hospital {
    private List<Paciente> pacientes;
    private List<Medico> medicos;
    private Agenda agenda;
    private List<Internacao> internacoes;
    private List<PlanoDeSaude> planos;
    private List<Integer> quartosDisponiveis;
    private List<Integer> quartosOcupados;

    public Hospital() {
        this.pacientes = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.agenda = new Agenda();
        this.internacoes = new ArrayList<>();
        this.planos = new ArrayList<>();
        this.quartosDisponiveis = new ArrayList<>();
        this.quartosOcupados = new ArrayList<>();
        for (int i = 101; i <= 110; i++) {
            this.quartosDisponiveis.add(i);
        }
    }

    //cadastro
    public void cadastrarPaciente(Paciente p) {
        this.pacientes.add(p);
        System.out.println("Paciente " + p.getNome() + " cadastrado com sucesso.");
    }
    public void cadastrarMedico(Medico m) {
        this.medicos.add(m);
        System.out.println("Médico " + m.getNome() + " cadastrado com sucesso.");
    }
    public void cadastrarPlano(PlanoDeSaude plano) {
        this.planos.add(plano);
        System.out.println("Plano de Saúde '" + plano.getNome() + "' cadastrado com sucesso.");
    }


    // carregamento
    public void carregarMedicos(List<Medico> medicos) {
        this.medicos.addAll(medicos);
    }
    public void carregarPacientes(List<Paciente> pacientes) {
        this.pacientes.addAll(pacientes);
    }
    public void carregarPlanos(List<PlanoDeSaude> planos) {
        this.planos.addAll(planos);
    }

    //buscas
    public boolean pacienteJaExiste(String cpfParaVerificar) {
        String cpfLimpoParaVerificar = cpfParaVerificar.replaceAll("[^0-9]", "");
        for (Paciente pacienteExistente : this.pacientes) {
            String cpfLimpoExistente = pacienteExistente.getCpf().replaceAll("[^0-9]", "");
            if (cpfLimpoExistente.equals(cpfLimpoParaVerificar)) {
                return true;
            }
        }
        return false;
    }

    public Paciente buscarPacientePorCpf(String cpf) {
        String cpfLimpoBusca = cpf.replaceAll("[^0-9]", "");
        for (Paciente paciente : this.pacientes) {
            String cpfLimpoPaciente = paciente.getCpf().replaceAll("[^0-9]", "");
            if (cpfLimpoPaciente.equals(cpfLimpoBusca)) {
                return paciente;
            }
        }
        return null;
    }

    // getters
    public List<Paciente> getPacientes() {
        return this.pacientes;
    }
    public List<Medico> getMedicos() {
        return this.medicos;
    }
    public List<PlanoDeSaude> getPlanos() {
        return this.planos;
    }
    public Agenda getAgenda() {
        return this.agenda;
    }
    public List<Internacao> getInternacoes() {
        return this.internacoes;
    }
    public void ocuparQuarto(int numeroQuarto) {
        quartosDisponiveis.remove(Integer.valueOf(numeroQuarto));
        quartosOcupados.add(numeroQuarto);
    }

    public void liberarQuarto(int numeroQuarto) {
        quartosOcupados.remove(Integer.valueOf(numeroQuarto));
        quartosDisponiveis.add(numeroQuarto);
        Collections.sort(quartosDisponiveis); // Opcional, para manter a lista ordenada
    }

    public List<Integer> getQuartosDisponiveis() {
        return this.quartosDisponiveis;
    }
    public void iniciarInternacao(Paciente paciente, Medico medico, int quarto) {
        if (quartosDisponiveis.contains(quarto)) {
            Internacao novaInternacao = new Internacao(paciente, medico, quarto);
            this.internacoes.add(novaInternacao);
            ocuparQuarto(quarto);
            System.out.println("INFO: Internação iniciada para " + paciente.getNome() + " no quarto " + quarto + ".");
        } else {
            System.out.println("ERRO: O quarto " + quarto + " não está disponível.");
        }
    }

    public void finalizarInternacao(Internacao internacao, double custoDiaria) {
        internacao.darAlta();

        liberarQuarto(internacao.getQuarto());

        long diasInternado = internacao.getDuracaoEmDias();
        Paciente paciente = internacao.getPaciente();
        PlanoDeSaude plano = paciente.getPlano();
        double custoFinal = 0;

        if (plano != null && plano.isCobreInternacaoCurta()) {
            if (diasInternado <= 7) {
                custoFinal = 0;
                System.out.println("INFO: Plano de saúde cobriu integralmente a internação.");
            } else {
                custoFinal = (diasInternado - 7) * custoDiaria;
            }
        } else {
            custoFinal = diasInternado * custoDiaria;
        }

        System.out.println("\n--- ALTA PROCESSADA COM SUCESSO ---");
        System.out.println("Paciente: " + paciente.getNome());
        System.out.println("Quarto liberado: " + internacao.getQuarto());
        System.out.println("Dias internado: " + diasInternado);
        System.out.printf("Custo final da internação: R$ %.2f\n", custoFinal);
        System.out.println("------------------------------------");
    }
    public void sincronizarQuartos() {
        this.quartosOcupados.clear();
        this.quartosDisponiveis.clear();
        for (int i = 101; i <= 110; i++) {
            this.quartosDisponiveis.add(i);
        }
        for (Internacao internacao : this.internacoes) {
            if (internacao.isAtiva()) {
                ocuparQuarto(internacao.getQuarto());
            }
        }
    }
}