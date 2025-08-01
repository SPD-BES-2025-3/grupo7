
# 🐾 Sistema Petshop - Relatório Individual

**Pedro Lucas**  
Entrega referente à etapa de **modelagem de integração e comunicação entre sistemas**.

---

## 📐 Diagramas Desenvolvidos

Nesta fase, foram desenvolvidos dois diagramas UML que representam a arquitetura distribuída e os fluxos de comunicação entre os componentes do sistema Petshop.

### 🧩 Diagrama de Componentes
- Representação clara da arquitetura do sistema, composta por:
  - Aplicação Desktop (interface do usuário)
  - API intermediária com persistência em MongoDB
  - Integração assíncrona via Redis
  - Middleware responsável por repassar dados para um serviço local
  - Serviço SQLite, que atualiza o banco de dados local.
- As setas de comunicação e chamadas entre os blocos representam os fluxos lógicos das mensagens entre os módulos.

### 🔄 Diagrama de Sequência
- Demonstra o fluxo completo de uma ação iniciada pelo funcionário na aplicação desktop.
- Inclui todos os participantes envolvidos no processo: API, MongoDB, Redis, Middleware, Serviço SQLite e Banco SQLite.
- Mensagens como `enviarMensagem()`, `publicaAcao()` e `atualizaBanco()` mostram a sequência exata e o tipo de operação realizada por cada componente.

---

## 📚 Aprendizado com os Diagramas

O desenvolvimento desses diagramas permitiu compreender com mais profundidade:

- Como sistemas heterogêneos (com bancos distintos como MongoDB e SQLite) podem se comunicar de forma coordenada.
- A importância da mensageria (Redis) na desacoplagem entre sistemas.
- A utilidade do Middleware como ponte entre uma API REST e serviços locais.
- A diferença entre **comunicação síncrona** (ex: salvar em MongoDB) e **assíncrona** (ex: Redis → Middleware → SQLite).
- Como representar visualmente não só o **comportamento**, mas também a **estrutura física e lógica** do sistema.
