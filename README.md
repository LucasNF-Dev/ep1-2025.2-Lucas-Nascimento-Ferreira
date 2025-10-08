# 🏥 Trabalho Prático – Sistema de Gerenciamento Hospitalar  

### 🎯 Objetivo  
Implementar um *Sistema de Gerenciamento Hospitalar* em *Java, aplicando conceitos avançados de **Programação Orientada a Objetos (POO), com foco em **herança, polimorfismo, encapsulamento, persistência de dados* e *regras de negócio mais complexas*.  

---
## Descrição do Projeto

Desenvolvimento de um sistema de gerenciamento hospitalar orientado a objetos com interface de linha de comando. O projeto simula as operações de um hospital, permitindo o cadastro de pacientes, médicos e planos de saúde, agendamento de consultas e gerenciamento de internações. O sistema possui dois níveis de acesso (Paciente e Administrador) e garante a persistência de todos os dados em arquivos .csv, carregando-os na inicialização e salvando-os ao final da execução.
## Dados do Aluno

- **Nome completo:** [Lucas Nascimento Ferreira]
- **Matrícula:** [242024262]
- **Curso:** [Engenharia de Software]
- **Turma:** [Preencher aqui]

---

## Instruções para Compilação e Execução

1. **Compilação:**  
   [A compilação é gerenciada automaticamente pela IDE ao executar o projeto.
Para compilação manual via terminal, navegue até a raiz do projeto e execute:
javac -d out/production -cp src src/br/com/lucasferreira/sh/**/*.java]

2. **Execução:**  
   [Via IDE: Localize o arquivo Main.java, clique com o botão direito e selecione a opção "Run".
Via Linha de Comando (após compilar): Navegue até a raiz do projeto e execute:
java -cp out/production br.com.lucasferreira.sh.Main]

3. **Estrutura de Pastas:**  
   [├── src/                     # Pasta principal com o código-fonte
   │   └── br/com/lucasferreira/sh/
   │       ├── enums/           # Contém as enumerações (Especialidade, TipoPlano)
   │       ├── Models/          # Contém as classes de modelo (Paciente, Medico, Hospital, etc.)
   │       └── utils/           # Contém as classes utilitárias (CPFUtil, GerenciadorDeDados)
   │       └── Main.java        # Classe principal que executa o sistema
   ├── database/                # Pasta que armazena os arquivos de dados (.csv)
   └── ...                      # Outros arquivos de configuração do projeto]

3. **Versão do JAVA utilizada:**  
   [O projeto foi desenvolvido e testado utilizando Java 24 (LTS). Recomenda-se o uso da versão 17 ou superior.]

---

## Vídeo de Demonstração

- [https://www.youtube.com/watch?v=PSolkVej6Ds]

---

## Prints da Execução

1. Menu Principal:  
   ![Inserir Print 1](caminho/do/print1.png)

2. Cadastro de Médico:  
   ![Inserir Print 2](caminho/do/print2.png)

3. Relatório de ?:  
   ![Inserir Print 3](caminho/do/print3.png)

---

---

## Observações (Extras ou Dificuldades)

- [Observações (Extras ou Dificuldades)
  Funcionalidades e Destaques:
Arquitetura Robusta: O sistema foi projetado com uma clara separação de responsabilidades, utilizando pacotes para Models, utils e enums, o que facilita a manutenção e escalabilidade.
Padrão de Projeto Factory: A criação de pacientes (Comum vs. Especial) é gerenciada pela PacienteFactory, abstraindo a lógica de decisão e promovendo o baixo acoplamento entre as classes.
Sistema de Acesso Duplo: Implementação de dois perfis de usuário (Paciente e Administrador), cada um com seu próprio menu e conjunto de permissões, controlando o acesso a funcionalidades críticas.
Persistência Completa: Todas as entidades do sistema (Médicos, Pacientes, Planos, Consultas e Internações) são salvas em arquivos .csv ao final da execução e recarregadas no início, garantindo a continuidade dos dados entre as sessões.
Dificuldades Enfrentadas:
Uma das principais dificuldades iniciais foi a modelagem da classe Agenda e a implementação da lógica de validação de horários. Como ainda não estava totalmente familiarizado com a sintaxe da linguagem e conceitos mais profundos de Orientação a Objetos, foram necessárias várias refatorações para chegar a uma solução robusta.
No começo do projeto, a cada nova funcionalidade implementada, percebia-se a necessidade de ajustar atributos e métodos em classes já existentes. Esse processo de refatoração constante foi desafiador, mas fundamental para aprender na prática sobre coesão, acoplamento e a importância de um bom planejamento inicial da arquitetura do software.]

---

## Contato

- [lucasnferreira.dev@gmail.com]

---

### 🖥️ Descrição do Sistema  

O sistema deve simular o funcionamento de um hospital com cadastro de *pacientes, médicos, especialidades, consultas e internações*.  

1. *Cadastro de Pacientes*  
   - Pacientes comuns e pacientes especiais (ex: com plano de saúde).  
   - Cada paciente deve ter: nome, CPF, idade, histórico de consultas e internações.  

2. *Cadastro de Médicos*  
   - Médicos podem ter especialidades (ex: cardiologia, pediatria, ortopedia).  
   - Cada médico deve ter: nome, CRM, especialidade, custo da consulta e agenda de horários.  

3. *Agendamento de Consultas*  
   - Um paciente pode agendar uma consulta com um médico disponível.  
   - Consultas devem registrar: paciente, médico, data/hora, local, status (agendada, concluída, cancelada).  
   - Pacientes especiais (plano de saúde) podem ter *vantagens*, como desconto.  
   - Duas consultas não podem estar agendadas com o mesmo médico na mesma hora, ou no mesmo local e hora

4. *Consultas e Diagnósticos*  
   - Ao concluir uma consulta, o médico pode registrar *diagnóstico* e/ou *prescrição de medicamentos*.  
   - Cada consulta deve ser registrada no *histórico do paciente*.  

5. *Internações*  
   - Pacientes podem ser internados.  
   - Registrar: paciente, médico responsável, data de entrada, data de saída (se já liberado), quarto e custo da internação.  
   - Deve existir controle de *ocupação dos quartos* (não permitir duas internações no mesmo quarto simultaneamente).  
   - Internações devem poder ser canceladas, quando isso ocorrer, o sistema deve ser atualizado automaticamente.

6. *Planos de saúde*    
   -  Planos de saude podem ser cadastrados.
   -  Cada plano pode oferecer *descontos* para *especializações* diferentes, com possibilidade de descontos variados.
   -  Um paciente que tenha o plano de saúde deve ter o desconto aplicado.
   -  Deve existir a possibilidade de um plano *especial* que torna internação de menos de uma semana de duração gratuita.
   -  Pacientes com 60+ anos de idade devem ter descontos diferentes.

7. *Relatórios*  
   - Pacientes cadastrados (com histórico de consultas e internações).  
   - Médicos cadastrados (com agenda e número de consultas realizadas).  
   - Consultas futuras e passadas (com filtros por paciente, médico ou especialidade).  
   - Pacientes internados no momento (com tempo de internação).  
   - Estatísticas gerais (ex: médico que mais atendeu, especialidade mais procurada).  
   - Quantidade de pessoas em um determinado plano de saúde e quanto aquele plano *economizou* das pessoas que o usam.  


---

### ⚙️ Requisitos Técnicos  
- O sistema deve ser implementado em *Java*.  
- Interface via *terminal (linha de comando)*.  
- Os dados devem ser persistidos em *arquivos* (.txt ou .csv).  
- Deve existir *menu interativo*, permitindo navegar entre as opções principais.  

---

### 📊 Critérios de Avaliação  

1. *Modos da Aplicação (1,5)* → Cadastro de pacientes, médicos, planos de saúde, consultas e internações.  
2. *Armazenamento em arquivo (1,0)* → Dados persistidos corretamente, leitura e escrita funcional.  
3. *Herança (1,0)* → Ex.: Paciente e PacienteEspecial, Consulta e ConsultaEspecial, Médico e subclasses por especialidade.  
4. *Polimorfismo (1,0)* → Ex.: regras diferentes para agendamento, preços de consultas.
5. *Encapsulamento (1,0)* → Atributos privados, getters e setters adequados.  
6. *Modelagem (1,0)* → Estrutura de classes clara, bem planejada e com relacionamentos consistentes.  
7. *Execução (0,5)* → Sistema compila, roda sem erros e possui menus funcionais.  
8. *Qualidade do Código (1,0)* → Código limpo, organizado, nomes adequados e boas práticas.  
9. *Repositório (1,0)* → Uso adequado de versionamento, commits frequentes com mensagens claras.  
10. *README (1,0)* → Vídeo curto (máx. 5 min) demonstrando as funcionalidades + prints de execução + explicação da modelagem.  

🔹 *Total = 10 pontos*  
🔹 *Pontuação extra (até 1,5)* → Melhorias relevantes, como:  
- Sistema de triagem automática com fila de prioridade.  
- Estatísticas avançadas (tempo médio de internação, taxa de ocupação por especialidade).  
- Exportação de relatórios em formato .csv ou .pdf.  
- Implementação de testes unitários para classes principais.  
- Menu visual.
