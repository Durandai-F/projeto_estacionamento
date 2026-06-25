# 🚀 Sistema de Estacionamento Profissional (Versão Infalível)

Esta versão foi redesenhada para eliminar problemas com dependências e facilitar a conexão com o banco de dados.

## 📂 Novidades desta versão:
1.  **Pasta `lib/`**: Agora o driver do MySQL (`mysql-connector-j-8.0.33.jar`) já vem dentro do projeto. Você não precisa baixar nada pelo Maven!
2.  **Configuração Simplificada**: O código está preparado para ler o driver diretamente da pasta lib.
3.  **Scripts de Execução**: Adicionei arquivos para você rodar o sistema com um clique.

## 🛠️ Como configurar o Banco de Dados (MySQL Workbench)
1. Abra o **MySQL Workbench**.
2. Execute o arquivo `estacionamento_mysql_script.sql` que está na raiz desta pasta.
3. Isso criará o banco e os usuários padrão.

## 💻 Como rodar no VS Code
1. Abra a pasta do projeto no VS Code.
2. No arquivo `src/main/java/com/estacionamento/app/dao/ConnectionFactory.java`, verifique se a senha na linha `private static final String PASSWORD = "root";` é a mesma do seu MySQL.
3. **Para rodar:**
   * Clique com o botão direito no arquivo `Main.java` e escolha **Run**.
   * **OU** se o VS Code der erro de biblioteca, use o terminal e digite:
     `java -cp "target/classes;lib/*" com.estacionamento.app.Main`

## 🔑 Acesso ao Sistema
*   **Admin**: `admin` / `admin123`
*   **Atendente**: `atendente` / `12345`

---
