# Arquitetura Pub/Sub com Redis

## Visão Geral

Esta implementação refatora a API do petshop para usar uma arquitetura de publicação/assinatura (pub/sub) usando Redis como broker de mensagens.

## Componentes da Arquitetura

### 1. **Eventos (Events)**
- `BaseEvent`: Classe base para todos os eventos
- `VendaEvent`: Eventos relacionados a vendas (CRIADA, FINALIZADA, CANCELADA)
- `EstoqueEvent`: Eventos de mudanças no estoque

### 2. **Publicador (Publisher)**
- `EventPublisherService`: Responsável por publicar eventos nos canais do Redis

### 3. **Assinantes (Subscribers)**
- `VendaEventListener`: Processa eventos de venda
- `EstoqueEventListener`: Processa eventos de estoque

### 4. **Configuração**
- `RedisConfig`: Configuração do Redis e serialização JSON
- `IntegrationConfig`: Configuração dos listeners de integração

## Canais de Eventos

| Canal | Descrição | Eventos |
|-------|-----------|---------|
| `venda-events` | Eventos de vendas | CRIADA, FINALIZADA, CANCELADA |
| `estoque-events` | Eventos de estoque | Atualizações de quantidade |
| `cliente-events` | Eventos de clientes | CRIADO, ATUALIZADO, REMOVIDO |
| `pet-events` | Eventos de pets | CRIADO, ATUALIZADO, REMOVIDO |
| `agendamento-events` | Eventos de agendamentos | CRIADO, CONFIRMADO, CANCELADO |
| `produto-events` | Eventos de produtos | CRIADO, ATUALIZADO, REMOVIDO |

## Fluxo de Eventos

### Venda Criada
1. `VendaController.createVenda()` é chamado
2. `VendaService.save()` salva a venda no MongoDB
3. `EventPublisherService.publishVendaEvent()` publica evento no canal `venda-events`
4. `VendaEventListener` recebe e processa o evento

### Venda Finalizada
1. `VendaController.finalizarVenda()` é chamado
2. `VendaService.finalizarVenda()` atualiza status e estoque
3. `ProdutoService.atualizarEstoque()` atualiza estoque e publica evento
4. `EventPublisherService.publishVendaEvent()` publica evento de venda finalizada
5. Listeners processam os eventos

## Benefícios da Arquitetura

### 1. **Desacoplamento**
- Produtores não precisam conhecer os consumidores
- Mudanças em um componente não afetam outros

### 2. **Escalabilidade**
- Múltiplos consumidores podem processar o mesmo evento
- Fácil adição de novos processadores

### 3. **Resiliência**
- Eventos são processados de forma assíncrona
- Falhas em um consumidor não afetam outros

### 4. **Monitoramento**
- Eventos podem ser logados e monitorados
- Fácil rastreamento de operações

## Como Usar

### 1. Iniciar Redis
```bash
cd redis
docker-compose up -d
```

### 2. Testar Eventos
```bash
# Testar evento de venda
curl -X POST http://localhost:8080/api/events/test/venda \
  -H "Content-Type: application/json" \
  -d '{"action": "CRIADA", "vendaId": "123"}'

# Testar evento de estoque
curl -X POST http://localhost:8080/api/events/test/estoque \
  -H "Content-Type: application/json" \
  -d '{
    "produtoId": "456",
    "produtoNome": "Ração Premium",
    "quantidadeAnterior": 10,
    "quantidadeAtual": 8,
    "motivo": "VENDA"
  }'

# Verificar status
curl http://localhost:8080/api/events/status
```

### 3. Monitorar Logs
Os eventos são logados automaticamente. Exemplo:
```
2024-01-15 10:30:00 - Evento publicado no canal venda-events: VendaEvent
2024-01-15 10:30:00 - Evento de venda recebido: VendaEvent - Ação: CRIADA
2024-01-15 10:30:00 - Nova venda criada: 123
```

## Próximos Passos

### 1. **Implementar Eventos Adicionais**
- Eventos para Cliente, Pet, Agendamento, Produto
- Eventos de auditoria e logs

### 2. **Melhorar Processamento**
- Implementar retry em caso de falhas
- Adicionar dead letter queue
- Implementar processamento em lote

### 3. **Monitoramento Avançado**
- Métricas de eventos por segundo
- Alertas para eventos críticos
- Dashboard de monitoramento

### 4. **Integração com Frontend**
- WebSocket para notificações em tempo real
- Dashboard de eventos em tempo real
- Notificações push

## Configuração de Produção

### Redis Cluster
Para produção, considere usar Redis Cluster para alta disponibilidade:

```yaml
# docker-compose.prod.yml
version: '3.8'
services:
  redis-master:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    command: redis-server --appendonly yes
    
  redis-slave:
    image: redis:7-alpine
    ports:
      - "6380:6379"
    command: redis-server --slaveof redis-master 6379
```

### Configuração de Segurança
```yaml
# application-prod.yml
spring:
  data:
    redis:
      host: ${REDIS_HOST}
      port: ${REDIS_PORT}
      password: ${REDIS_PASSWORD}
      ssl: true
```

## Troubleshooting

### Problemas Comuns

1. **Redis não conecta**
   - Verificar se Redis está rodando: `docker ps`
   - Verificar configuração no `application.yml`

2. **Eventos não são processados**
   - Verificar logs da aplicação
   - Verificar se listeners estão registrados
   - Testar conectividade com Redis

3. **Serialização de eventos**
   - Verificar se classes de evento são serializáveis
   - Verificar configuração do Jackson

### Comandos Úteis

```bash
# Verificar Redis
redis-cli ping

# Monitorar canais
redis-cli monitor

# Verificar chaves
redis-cli keys "*"

# Limpar Redis
redis-cli flushall
``` 