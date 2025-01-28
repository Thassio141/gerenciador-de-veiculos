# Gerenciador de Veículos

**Gerenciador de Veículos**, uma aplicação desenvolvida em **Spring Boot** para gerenciar veículos e suas informações. Este projeto utiliza **PostgreSQL** como banco de dados e inclui um frontend para interagir com a API.

---

##  Configuração e Execução

### 1. Configurando o Banco de Dados
1. Certifique-se de ter o Docker instalado em sua máquina.
2. No diretório do projeto, vá para o arquivo de configuração do Docker Compose em:
   ```
   src/main/docker/docker-compose.yml
   ```
3. Execute o seguinte comando para subir o container do banco de dados:
   ```bash
   docker compose up
   ```
4. **Opcional:** Caso prefira utilizar sua própria configuração de banco PostgreSQL, substitua os dados no arquivo de propriedades do Spring Boot:
   ```
   src/main/resources/application.properties
   ```

---

### 2. Criando a Tabela de Veículos
1. Após o banco de dados estar configurado e em execução, crie a tabela necessária utilizando o script SQL disponível no diretório:
   ```
   src/main/resources/db/tb_veiculo.sql
   ```
2. Execute o script diretamente no banco de dados PostgreSQL para criar a tabela.

---

### 3. Executando a Aplicação
Agora que o banco de dados está configurado e a tabela foi criada, você pode executar a aplicação backend.

- **Usando sua IDE**:
  Importe o projeto e execute a classe principal.

- **Usando o Maven**:
  Execute o seguinte comando na raiz do projeto:
  ```bash
  mvn spring-boot:run
  ```

---

## 🌐 Endpoints e Documentação
Após iniciar o backend, você pode acessar os endpoints utilizando o **Swagger UI**:
```
http://localhost:8080/swagger-ui.html
```

## 💻 Utilizando o Frontend
Com o backend em execução, você pode consumir os dados da API através do frontend.

1. Navegue até o diretório do frontend:
   ```
   frontend/
   ```
2. Abra o arquivo `index.html` diretamente no seu navegador.

---

## Tecnologias Utilizadas
### Backend:
- **Spring Boot**: Framework principal.
- **PostgreSQL**: Banco de dados.
- **Docker**: Para configurar e subir o banco de dados.
- **Swagger UI**: Acessar os endpoints da API.

### Frontend:
- **HTML + CSS**: Interface simples para consumir a API.
- **JavaScript**: Para interação com os endpoints.

---

## Recursos do Projeto
- Cadastro de veículos.
- Filtros avançados para busca de veículos (tipo, modelo, ano, cor).
- Atualização de informações de veículos existentes.
- Exclusão de veículos.
- Paginação de resultados para facilitar a navegação.
- Interface para visualização e interação com os dados.

---

## Estrutura do Projeto
```
backend/
│    ├──gerenciadordeveiculos/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/                 # Código-fonte do backend
│       │   │   │   ├── controllers/       # Controladores REST
│       │   │   │   ├── models/             # Entidades 
│       │   │   │   ├── services/           # Serviços
│       │   │   │   ├── repositories/    # Repositórios
│       │   │   │   ├── exceptions/     # Exceções personalizadas
│       │   │   │   ├── enums/             # Enums
│       │   │   │   ├── dtos/                 # DTOs 
│       │   │   ├── resources/
│       │   │   │   ├── application.properties # Configurações do Spring Boot
│       │   │   │   └── db/               # Scripts SQL
│       │   │   └── docker/               # Arquivo docker-compose.yml
│       │   ├── test/                     # Testes automatizados
│       ├── pom.xml                       # Configuração do Maven
│       ├── README.md                     # Este arquivo
frontend/
│    ├── index.html                       # Interface do frontend
│    ├── js/
│    │   ├── script.js                    # Script JavaScript
│    ├── css/
│    │   ├── style.css                    # Estilos CSS
```

---

##  Personalização
Se necessário, ajuste as configurações no arquivo `application.properties` para refletir suas necessidades específicas, como:
- URL do banco de dados.
- Usuário e senha do banco.
---
