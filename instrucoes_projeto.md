# Guia de Configuração Profissional - Sistema de Estacionamento

Este projeto foi refatorado para seguir o padrão de arquitetura profissional (similar ao HealthTrack), utilizando **MySQL** como banco de dados principal.

## 1. Estrutura de Conexão
A conexão agora é centralizada na classe:
`src/main/java/com/estacionamento/app/dao/ConnectionFactory.java`

Para alterar as credenciais do banco (usuário e senha), basta editar as constantes nesta classe.

## 2. Configuração do Banco de Dados (MySQL Workbench)
1. Abra o MySQL Workbench.
2. Execute o script `estacionamento_mysql_script.sql` localizado na raiz deste projeto.
3. O script criará o banco `estacionamento`, as tabelas necessárias e os usuários padrão.

## 3. Dependências
O projeto utiliza o `mysql-connector-java` para realizar a ponte entre o Java e o MySQL. Esta dependência já está configurada no arquivo `pom.xml`.

## 4. Credenciais Padrão para Login
*   **Admin**: Usuário: `admin` | Senha: `admin123`
*   **Atendente**: Usuário: `atendente` | Senha: `12345`

---
