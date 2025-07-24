
Sistema Petshop - Relatório Individual

Pedro Lucas 

Entrega referente à etapa de modelagem e documentação do sistema.

---

Atividades Realizadas

Durante esta etapa, me dediquei à criação e refinamento dos **diagramas UML** fundamentais para representar a arquitetura e o fluxo do sistema Petshop. Os principais artefatos desenvolvidos foram:

Diagrama de Classes
- Modelei as principais entidades do domínio: `Cliente`, `Pet`, `Funcionário`, `Agendamento`, `Serviço`, `Pagamento`.
- Estruturei herança entre `Pessoa`, `Cliente` e `Funcionário` para evitar repetição de atributos.
- Relacionei corretamente as entidades com cardinalidades coerentes.

Diagrama de Componentes
- Estruturei os principais blocos do sistema: aplicação desktop, API REST, banco de dados e Redis.
- Modelei a integração entre camadas, incluindo comunicação assíncrona.
- Refleti os componentes reais usados no backend da API (ex: controllers, serviços e persistência).

Diagrama de Sequência
- Descrevi o fluxo de criação de um agendamento: da aplicação desktop até o banco, via Redis e API.
- Mostrei a troca de mensagens e o retorno do status da operação.
- Utilizei nomes claros e realistas nas ações (ex: `enviarMensagem()`, `salvarAgendamento()`).

---

Integração com a API REST

Todos os diagramas foram criados com base nas funcionalidades da API desenvolvida pela equipe (Spring Boot + MongoDB). As entidades e operações estão alinhadas com os endpoints documentados, como:
- `/api/clientes`, `/api/pets`, `/api/agendamentos`, `/api/vendas`, entre outros.

---

Participação e Colaboração

- Participei ativamente das conversas em grupo sobre a estrutura do sistema.
- Sugeri melhorias nos relacionamentos entre classes e na divisão dos componentes.
- Contribuí na verificação da consistência entre os diagramas e a implementação da API.

---

Próximos Passos

- Incluir as entidades de produto e venda nos diagramas.
- Adaptar a modelagem à medida que novas funcionalidades forem integradas pela equipe backend.
- Incorporar todos os artefatos gerados à documentação final do projeto.
