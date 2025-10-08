package br.com.lucasferreira.sh;

import br.com.lucasferreira.sh.Models.*;
import br.com.lucasferreira.sh.enums.Especialidade;
import br.com.lucasferreira.sh.enums.TipoPlano;
import br.com.lucasferreira.sh.utils.CPFUtil;
import br.com.lucasferreira.sh.utils.GerenciadorDeDados;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Hospital hospital = new Hospital();
    private static Paciente pacienteLogado = null;
    private static boolean isAdminLogado = false;


    private static final String ARQUIVO_PLANOS = "database/planos.csv";
    private static final String ARQUIVO_MEDICOS = "database/medicos.csv";
    private static final String ARQUIVO_PACIENTES = "database/pacientes.csv";
    private static final String ARQUIVO_CONSULTAS = "database/consultas.csv";
    private static final String ARQUIVO_INTERNACOES = "database/internacoes.csv";


    public static void main(String[] args) {
        carregarDadosIniciais();
        System.out.println("🏥 Olá, bem-vindo ao Sistema de Gerenciamento Hospitalar! 🏥");

        while (true) {
            if (pacienteLogado != null) {
                exibirMenuPrincipalPaciente();
            } else if (isAdminLogado) {
                exibirMenuPrincipalAdmin();
            } else {
                exibirMenuLogin();
            }
        }
    }

    private static void carregarDadosIniciais() {
        System.out.println("Carregando dados do sistema...");
        List<PlanoDeSaude> planos = GerenciadorDeDados.carregarPlanos(ARQUIVO_PLANOS);
        hospital.carregarPlanos(planos);

        List<Medico> medicos = GerenciadorDeDados.carregarMedicos(ARQUIVO_MEDICOS);
        hospital.carregarMedicos(medicos);

        List<Paciente> pacientes = GerenciadorDeDados.carregarPacientes(ARQUIVO_PACIENTES, planos);
        hospital.carregarPacientes(pacientes);

        List<Consulta> consultas = GerenciadorDeDados.carregarConsultas(ARQUIVO_CONSULTAS, medicos, pacientes);
        hospital.getAgenda().getConsultas().addAll(consultas);

        List<Internacao> internacoes = GerenciadorDeDados.carregarInternacoes(ARQUIVO_INTERNACOES, pacientes, medicos);
        hospital.getInternacoes().addAll(internacoes);
        hospital.sincronizarQuartos();

        System.out.println("Dados carregados com sucesso!\n");
    }

    private static void salvarDadosFinais() {
        System.out.println("\nSalvando todas as alterações...");
        GerenciadorDeDados.salvarPlanos(ARQUIVO_PLANOS, hospital.getPlanos());
        GerenciadorDeDados.salvarMedicos(ARQUIVO_MEDICOS, hospital.getMedicos());
        GerenciadorDeDados.salvarPacientes(ARQUIVO_PACIENTES, hospital.getPacientes());
        GerenciadorDeDados.salvarConsultas(ARQUIVO_CONSULTAS, hospital.getAgenda().getConsultas());
        GerenciadorDeDados.salvarInternacoes(ARQUIVO_INTERNACOES, hospital.getInternacoes());
        System.out.println("Dados salvos com sucesso!");
    }

    private static void exibirMenuLogin() {
        System.out.println("\nPor favor, entre na sua conta.");
        System.out.println("1. Já sou cliente (Login)");
        System.out.println("2. Quero me cadastrar");
        System.out.println("3. Entrar como Administrador"); // NOVA OPÇÃO
        System.out.println("0. Sair do Sistema");
        System.out.print("Escolha uma opção: ");

        String input = scanner.nextLine();
        try {
            int opcao = Integer.parseInt(input);
            switch (opcao) {
                case 1: realizarLogin(); break;
                case 2: realizarCadastro(); break;
                case 3: entrarComoADM(); break; // NOVA CHAMADA
                case 0:
                    salvarDadosFinais();
                    System.out.println("Obrigado por usar nosso sistema. Até logo!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida. Por favor, digite um dos números do menu.");
        }
    }
    private static void entrarComoADM() {
        System.out.print("\nDigite a senha de Administrador: ");
        String senha = scanner.nextLine();
        if ("adm".equalsIgnoreCase(senha)) {
            isAdminLogado = true;
            System.out.println("\nLogin de administrador realizado com sucesso!");
        } else {
            System.out.println("\nSenha incorreta.");
        }
    }
    private static void realizarLogin() {
        System.out.print("\nDigite seu CPF para entrar: ");
        String cpf = scanner.nextLine();
        Paciente paciente = hospital.buscarPacientePorCpf(cpf);
        if (paciente != null) {
            pacienteLogado = paciente;
            System.out.println("\nLogin realizado com sucesso! Bem-vindo(a), " + pacienteLogado.getNome() + ".");
        } else {
            System.out.println("\nCPF não encontrado. Tente novamente ou registre-se.");
        }
    }

    private static void realizarCadastro() {
        System.out.println("\nOpa! Parece que você ainda não tem cadastro. Vamos começar!");
        System.out.print("Digite seu CPF: ");
        String cpf = scanner.nextLine();

        if (!CPFUtil.validarCPF(cpf)) {
            System.out.println("ERRO: CPF inválido!");
            return;
        }
        if (hospital.pacienteJaExiste(cpf)) {
            System.out.println("ERRO: CPF já cadastrado. Por favor, faça o login.");
            return;
        }

        System.out.print("Digite seu nome completo: ");
        String nome = scanner.nextLine();
        System.out.print("Digite sua data de nascimento (AAAA-MM-DD): ");
        LocalDate dataNascimento = LocalDate.parse(scanner.nextLine());
        System.out.print("Você é PCD (Pessoa com Deficiência)? (true/false): ");
        boolean pcd = scanner.nextBoolean();
        scanner.nextLine();

        System.out.println("Planos disponíveis:");
        List<PlanoDeSaude> planos = hospital.getPlanos();
        for (int i = 0; i < planos.size(); i++) {
            System.out.println((i + 1) + ". " + planos.get(i).getNome());
        }
        System.out.println("0. Não tenho plano");
        System.out.print("Escolha seu plano: ");
        int escolhaPlano = scanner.nextInt();
        scanner.nextLine();

        PlanoDeSaude planoEscolhido = null;
        if (escolhaPlano > 0 && escolhaPlano <= planos.size()) {
            planoEscolhido = planos.get(escolhaPlano - 1);
        }

        Paciente novoPaciente = PacienteFactory.criarPaciente(nome, cpf, planoEscolhido, pcd, dataNascimento);
        hospital.cadastrarPaciente(novoPaciente);

        pacienteLogado = novoPaciente;
        System.out.println("\nCadastro realizado com sucesso! Bem-vindo(a), " + pacienteLogado.getNome() + ".");
    }

    private static void exibirMenuPrincipalPaciente() {
        System.out.println("\n--- MENU DO PACIENTE ---");
        System.out.println("1. Marcar uma Consulta");
        System.out.println("2. Ver meu Histórico de Consultas");
        System.out.println("0. Fazer Logout (Voltar)");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                realizarAgendamento();
                break;
            case 2:
                exibirHistoricoPaciente();
                break;
            case 0:
                System.out.println(pacienteLogado.getNome() + ", fazendo logout...");
                pacienteLogado = null;
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }
    private static void exibirHistoricoPaciente() {
        System.out.println("\n--- Meu Histórico de Consultas ---");
        List<Consulta> historico = pacienteLogado.getHistoricoDoPaciente(hospital.getAgenda());
        if (historico.isEmpty()) {
            System.out.println("Você ainda não possui consultas em seu histórico.");
            return;
        }
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

        for (Consulta consulta : historico) {
            String dataFormatada = consulta.getDataHora().format(formatador);
            Medico medico = consulta.getMedico();

            System.out.println("------------------------------------");
            System.out.println("Data: " + dataFormatada);
            System.out.println("Médico(a): Dr(a). " + medico.getNome());
            System.out.println("Especialidade: " + medico.getEspecialidade());
        }
        System.out.println("------------------------------------");
    }
    private static void exibirMenuPrincipalAdmin() {
        System.out.println("\n--- PAINEL DO ADMINISTRADOR ---");
        System.out.println("1. Cadastrar Novo Médico");
        System.out.println("2. Cadastrar Novo Plano de Saúde");
        System.out.println("3. Agendar Consulta para um Paciente");
        System.out.println("4. Gerenciar Internações"); // NOVO MENU
        System.out.println("5. Listar todos os Pacientes");
        System.out.println("6. Listar todos os Médicos");
        System.out.println("0. Fazer Logout (Voltar)");
        System.out.print("Escolha uma opção: ");

        String input = scanner.nextLine();
        try {
            int opcao = Integer.parseInt(input);
            switch (opcao) {
                case 1: cadastrarNovoMedico(); break;
                case 2: cadastrarNovoPlano(); break;
                case 3: realizarAgendamento(); break;
                case 4: menuGerenciarInternacoes(); break; // NOVO
                case 5: listarPacientes(); break;
                case 6: listarMedicos(); break;
                case 0:
                    isAdminLogado = false;
                    System.out.println("Saindo do painel de administrador...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida. Por favor, digite um número.");
        }
    }
    private static void menuGerenciarInternacoes() {
        System.out.println("\n--- Gerenciamento de Internações ---");
        System.out.println("1. Iniciar Nova Internação");
        System.out.println("2. Finalizar Internação (Dar Alta)");
        System.out.println("3. Listar Internações Ativas");
        System.out.println("0. Voltar ao menu principal");
        System.out.print("Escolha uma opção: ");

        String input = scanner.nextLine();
        try {
            int opcao = Integer.parseInt(input);
            switch (opcao) {
                case 1: iniciarNovaInternacao(); break;
                case 2: finalizarInternacao(); break;
                case 3: listarInternacoesAtivas(); break;
                case 0: return; // Volta para o menu do ADM
                default: System.out.println("Opção inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
        }
    }
    private static void listarInternacoesAtivas() {
        System.out.println("\n--- Internações Ativas ---");
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        boolean encontrouAtivas = false;

        for (Internacao i : hospital.getInternacoes()) {
            if (i.isAtiva()) {
                encontrouAtivas = true;
                System.out.println("------------------------------------");
                System.out.println("Paciente: " + i.getPaciente().getNome() + " (CPF: " + i.getPaciente().getCpf() + ")");
                System.out.println("Quarto: " + i.getQuarto());
                System.out.println("Médico Resp.: " + i.getMedicoResponsavel().getNome());
                System.out.println("Data de Entrada: " + i.getDataEntrada().format(formatador));
            }
        }

        if (!encontrouAtivas) {
            System.out.println("Nenhuma internação ativa no momento.");
        }
        System.out.println("------------------------------------");
    }
    private static void finalizarInternacao() {
        System.out.println("\n--- Finalizar Internação (Dar Alta) ---");
        listarInternacoesAtivas();
        System.out.print("Digite o número do quarto para dar alta: ");
        int quarto = Integer.parseInt(scanner.nextLine());

        Internacao internacaoParaFinalizar = null;
        for (Internacao i : hospital.getInternacoes()) {
            if (i.getQuarto() == quarto && i.isAtiva()) {
                internacaoParaFinalizar = i;
                break;
            }
        }

        if (internacaoParaFinalizar != null) {
            double custoDiaria = 500.0; // Você pode tornar isso mais dinâmico
            hospital.finalizarInternacao(internacaoParaFinalizar, custoDiaria);
        } else {
            System.out.println("ERRO: Nenhuma internação ativa encontrada para o quarto " + quarto + ".");
        }
    }
    private static void iniciarNovaInternacao() {
        System.out.println("\n--- Iniciar Nova Internação ---");

        System.out.println("Selecione o paciente a ser internado:");
        listarPacientes();
        System.out.print("Digite o CPF do paciente: ");
        String cpf = scanner.nextLine();
        Paciente pacienteParaInternar = hospital.buscarPacientePorCpf(cpf);
        if (pacienteParaInternar == null) {
            System.out.println("ERRO: Paciente não encontrado.");
            return;
        }

        System.out.println("\nSelecione o médico responsável:");
        listarMedicos();
        System.out.print("Escolha o médico pelo número: ");
        int escolhaMedico = Integer.parseInt(scanner.nextLine());
        Medico medicoResponsavel = hospital.getMedicos().get(escolhaMedico - 1);

        List<Integer> quartosDisponiveis = hospital.getQuartosDisponiveis();
        if (quartosDisponiveis.isEmpty()) {
            System.out.println("ERRO: Não há quartos disponíveis no momento.");
            return;
        }
        System.out.println("\nQuartos disponíveis: " + quartosDisponiveis);
        System.out.print("Digite o número do quarto para a internação: ");
        int quartoEscolhido = Integer.parseInt(scanner.nextLine());

        hospital.iniciarInternacao(pacienteParaInternar, medicoResponsavel, quartoEscolhido);
    }
    private static void cadastrarNovoMedico() {
        System.out.println("\n--- Cadastro de Novo Médico ---");
        System.out.print("Nome completo: ");
        String nome = scanner.nextLine();
        System.out.print("CRM: ");
        String crm = scanner.nextLine();
        System.out.println("Especialidades disponíveis:");
        for (Especialidade e : Especialidade.values()) {
            System.out.println("- " + e.name());
        }
        System.out.print("Digite a especialidade: ");
        Especialidade especialidade = Especialidade.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Rating (1 a 5): ");
        int rating = scanner.nextInt();
        System.out.print("Está de plantão? (true/false): ");
        boolean plantao = scanner.nextBoolean();
        scanner.nextLine(); // Limpa o buffer

        Medico novoMedico = new Medico(nome, crm, especialidade, rating, plantao);
        hospital.cadastrarMedico(novoMedico); // O hospital já tinha esse método
    }

    private static void cadastrarNovoPlano() {
        System.out.println("\n--- Cadastro de Novo Plano ---");
        System.out.print("Nome do plano: ");
        String nome = scanner.nextLine();
        System.out.print("Cobre internação curta? (true/false): ");
        boolean cobreInternacao = scanner.nextBoolean();
        scanner.nextLine();
        System.out.println("Tipos disponíveis:");
        for (TipoPlano t : TipoPlano.values()) {
            System.out.println("- " + t.name());
        }
        System.out.print("Digite o tipo do plano: ");
        TipoPlano tipo = TipoPlano.valueOf(scanner.nextLine().toUpperCase());

        PlanoDeSaude novoPlano = new PlanoDeSaude(nome, cobreInternacao, tipo);
        hospital.cadastrarPlano(novoPlano); // O hospital já tinha esse método
    }

    private static void listarPacientes() {
        System.out.println("\n--- Lista de Pacientes Cadastrados ---");
        List<Paciente> pacientes = hospital.getPacientes();
        if (pacientes.isEmpty()) {
            System.out.println("Nenhum paciente cadastrado.");
            return;
        }
        for (Paciente p : pacientes) {
            String plano = (p.getPlano() != null) ? p.getPlano().getNome() : "Sem Plano";
            System.out.printf("Nome: %-20s | CPF: %-15s | Idade: %-3d | Plano: %s\n", p.getNome(), p.getCpf(), p.getIdade(), plano);
        }
    }

    private static void listarMedicos() {
        System.out.println("\n--- Lista de Médicos Cadastrados ---");
        List<Medico> medicos = hospital.getMedicos();
        if (medicos.isEmpty()) {
            System.out.println("Nenhum médico cadastrado.");
            return;
        }
        for (Medico m : medicos) {
            System.out.printf("Nome: %-25s | CRM: %-15s | Especialidade: %s\n", m.getNome(), m.getCrm(), m.getEspecialidade());
        }
    }

    private static void realizarAgendamento() {
        System.out.println("\n--- Agendamento de Consulta ---");
        Paciente pacienteDaConsulta = null;

        if (pacienteLogado != null) {
            pacienteDaConsulta = pacienteLogado;
        } else if (isAdminLogado) {
            System.out.println("Agendamento como Administrador. Selecione o paciente:");
            List<Paciente> pacientes = hospital.getPacientes();
            if (pacientes.isEmpty()) {
                System.out.println("Não há pacientes cadastrados para agendar.");
                return;
            }
            listarPacientes();
            System.out.print("\nDigite o CPF do paciente para a consulta: ");
            String cpf = scanner.nextLine();
            pacienteDaConsulta = hospital.buscarPacientePorCpf(cpf);

            if (pacienteDaConsulta == null) {
                System.out.println("ERRO: Paciente com o CPF informado não foi encontrado.");
                return;
            }
            System.out.println("Agendando para o paciente: " + pacienteDaConsulta.getNome());
        } else {
            System.out.println("ERRO: Ninguém está logado para realizar um agendamento.");
            return;
        }
        List<Medico> medicos = hospital.getMedicos();
        if (medicos.isEmpty()) {
            System.out.println("Não há médicos cadastrados no momento.");
            return;
        }
        System.out.println("\nMédicos disponíveis:");
        System.out.printf("%-4s %-25s | %-18s | %-7s | %s\n", "Nº", "Nome", "Especialidade", "Rating", "Valor para você");
        System.out.println("----------------------------------------------------------------------------------");

        for (int i = 0; i < medicos.size(); i++) {
            Medico m = medicos.get(i);
            String estrelas = "★".repeat(m.getRating());
            Consulta consultaHipotetica = new Consulta(m, pacienteDaConsulta, null);
            double custoParaPaciente = pacienteDaConsulta.calcularCustoConsulta(consultaHipotetica);

            System.out.printf("%-4d %-25s | %-18s | %-7s | R$ %.2f\n",
                    (i + 1),
                    m.getNome(),
                    m.getEspecialidade(),
                    estrelas,
                    custoParaPaciente);
        }
        System.out.println("----------------------------------------------------------------------------------");
        System.out.print("Escolha o médico pelo número: ");
        String inputMedico = scanner.nextLine();
        int escolhaMedico;
        try {
            escolhaMedico = Integer.parseInt(inputMedico);
        } catch (NumberFormatException e) {
            System.out.println("ERRO: Escolha de médico inválida. Digite apenas o número.");
            return;
        }


        if (escolhaMedico < 1 || escolhaMedico > medicos.size()) {
            System.out.println("ERRO: Opção de médico inválida.");
            return;
        }
        Medico medicoEscolhido = medicos.get(escolhaMedico - 1);
        System.out.print("Digite a data da consulta (formato AAAA-MM-DD): ");
        String dataInput = scanner.nextLine();
        System.out.print("Digite a hora da consulta (formato HH:MM): ");
        String horaInput = scanner.nextLine();

        LocalDateTime horarioDesejado;
        try {
            horarioDesejado = LocalDateTime.parse(dataInput + "T" + horaInput);
        } catch (Exception e) {
            System.out.println("ERRO: Formato de data ou hora inválido.");
            return;
        }

        if (horarioDesejado.isBefore(LocalDateTime.now())) {
            System.out.println("ERRO: Não é possível agendar consultas em datas ou horários passados.");
            return;
        }
        Consulta novaConsulta = new Consulta(medicoEscolhido, pacienteDaConsulta, horarioDesejado);
        double custo = pacienteDaConsulta.calcularCustoConsulta(novaConsulta);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        String dataFormatada = horarioDesejado.format(formatador);
        System.out.println("\n--- RESUMO DA CONSULTA ---");
        System.out.println("Paciente: " + pacienteDaConsulta.getNome());
        System.out.println("Consulta marcada para: " + dataFormatada);
        System.out.println("Com o(a) Médico(a): Dr(a). " + medicoEscolhido.getNome());
        System.out.println("Especialidade: " + medicoEscolhido.getEspecialidade());
        System.out.printf("Custo total da consulta: R$ %.2f\n", custo); // %.2f formata para 2 casas decimais
        System.out.println("--------------------------");
        System.out.print("Deseja confirmar o agendamento? (s/n): ");
        String confirmacao = scanner.nextLine();
        if (confirmacao.equalsIgnoreCase("s")) {
            hospital.getAgenda().marcarConsulta(novaConsulta);
        } else {
            System.out.println("Agendamento cancelado.");
        }
    }
    }
