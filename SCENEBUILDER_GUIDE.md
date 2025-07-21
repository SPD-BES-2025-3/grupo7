# Guia do SceneBuilder - Sistema Pet Shop

Este guia explica como usar o SceneBuilder para editar e personalizar as telas do sistema de pet shop.

## 📋 Pré-requisitos

1. **SceneBuilder** - Baixe em: https://gluonhq.com/products/scene-builder/
2. **Java 17** ou superior
3. **Projeto JavaFX** configurado

## 🚀 Como Abrir as Telas no SceneBuilder

### 1. Abrir o SceneBuilder
- Execute o SceneBuilder
- Vá em `File > Open` ou use `Ctrl+O`

### 2. Navegar para os Arquivos FXML
Os arquivos FXML estão localizados em:
```
src/main/resources/fxml/
```

### 3. Arquivos Disponíveis para Edição

#### 📊 MainView.fxml
- **Descrição**: Tela principal com menu lateral
- **Funcionalidades**: Navegação entre módulos
- **Elementos principais**:
  - Menu lateral com botões de navegação
  - Área de conteúdo dinâmica
  - Barra superior com título e botão de sair

#### 📈 DashboardView.fxml
- **Descrição**: Dashboard com estatísticas
- **Funcionalidades**: Cards informativos e ações rápidas
- **Elementos principais**:
  - Cards de estatísticas (clientes, pets, agendamentos, vendas)
  - Botões de ações rápidas
  - Tabela de agendamentos recentes

#### 👥 ClienteView.fxml
- **Descrição**: Gestão de clientes
- **Funcionalidades**: CRUD de clientes
- **Elementos principais**:
  - Formulário de cadastro
  - Tabela de clientes
  - Barra de pesquisa

#### 🐕 PetView.fxml
- **Descrição**: Gestão de pets
- **Funcionalidades**: CRUD de pets
- **Elementos principais**:
  - Formulário de cadastro com campos específicos
  - ComboBox para espécie, sexo e cliente
  - DatePicker para data de nascimento
  - Tabela de pets

#### 📦 ProdutoView.fxml
- **Descrição**: Gestão de produtos
- **Funcionalidades**: CRUD de produtos
- **Elementos principais**:
  - Formulário de cadastro
  - ComboBox para categoria
  - Campos para preço e estoque
  - Tabela de produtos

#### 📅 AgendamentoView.fxml
- **Descrição**: Gestão de agendamentos
- **Funcionalidades**: CRUD de agendamentos
- **Elementos principais**:
  - Formulário de agendamento
  - ComboBox para cliente, pet e serviço
  - DatePicker e ComboBox para data/hora
  - Filtros por data e status

#### 💰 VendaView.fxml
- **Descrição**: Gestão de vendas
- **Funcionalidades**: Sistema de vendas
- **Elementos principais**:
  - Formulário de venda
  - Tabela de itens da venda
  - Resumo do total
  - Histórico de vendas

## 🎨 Personalização de Estilos

### Arquivo CSS
O arquivo de estilos está em:
```
src/main/resources/css/styles.css
```

### Principais Classes CSS

#### Botões
```css
.btn-primary    /* Botão azul principal */
.btn-secondary  /* Botão cinza secundário */
.btn-danger     /* Botão vermelho para exclusão */
.btn-success    /* Botão verde para confirmação */
```

#### Formulários
```css
.form-container /* Container dos formulários */
.text-field     /* Campos de texto */
.combo-box      /* Caixas de seleção */
.date-picker    /* Seletores de data */
```

#### Tabelas
```css
.table-view     /* Tabelas */
.table-view .column-header  /* Cabeçalhos */
.table-view .table-row-cell /* Linhas */
```

#### Menu Lateral
```css
.sidebar        /* Menu lateral */
.menu-button    /* Botões do menu */
```

## 🔧 Dicas de Edição

### 1. Manter a Estrutura
- **NÃO** remova os `fx:id` dos elementos
- **NÃO** altere os nomes dos controladores
- **Mantenha** a hierarquia de elementos

### 2. Adicionar Novos Elementos
- Use o painel `Library` para arrastar novos componentes
- Configure as propriedades no painel `Properties`
- Adicione `fx:id` para referenciar no controlador

### 3. Estilização
- Use o painel `CSS` para aplicar estilos
- Teste as mudanças no `Preview`
- Mantenha consistência com o design existente

### 4. Validação
- Sempre teste a funcionalidade após edições
- Verifique se os eventos estão conectados
- Teste a responsividade

## 📱 Responsividade

### Breakpoints Sugeridos
- **Desktop**: 1200px+
- **Tablet**: 768px - 1199px
- **Mobile**: < 768px

### Ajustes Responsivos
```css
@media (max-width: 768px) {
    .sidebar {
        -fx-pref-width: 200;
    }
    
    .menu-button {
        -fx-font-size: 12px;
        -fx-padding: 10 15;
    }
}
```

## 🔗 Conexão com Controladores

### Estrutura de Controladores
```
src/main/java/com/grupo7/petshop/controller/
├── MainController.java
├── DashboardController.java
├── ClienteController.java
├── PetController.java
├── ProdutoController.java
├── AgendamentoController.java
└── VendaController.java
```

### Adicionar Novos Métodos
1. Abra o controlador correspondente
2. Adicione o método com anotação `@FXML`
3. Conecte o evento no SceneBuilder

### Exemplo de Método
```java
@FXML
public void meuNovoMetodo() {
    // Implementação aqui
}
```

## 🎯 Melhores Práticas

### 1. Nomenclatura
- Use nomes descritivos para `fx:id`
- Mantenha padrão camelCase
- Evite abreviações confusas

### 2. Organização
- Agrupe elementos relacionados
- Use containers apropriados (VBox, HBox, GridPane)
- Mantenha hierarquia lógica

### 3. Acessibilidade
- Adicione tooltips para elementos importantes
- Use labels descritivos
- Mantenha contraste adequado

### 4. Performance
- Evite muitos elementos aninhados
- Use `VBox.vgrow` e `HBox.hgrow` adequadamente
- Limite o uso de efeitos visuais pesados

## 🐛 Solução de Problemas

### Problema: Tela não carrega
- Verifique se o `fx:controller` está correto
- Confirme se o arquivo FXML está no local correto
- Teste se o controlador compila

### Problema: Estilos não aplicam
- Verifique se o CSS está sendo carregado
- Confirme se as classes CSS estão corretas
- Teste no `Preview` do SceneBuilder

### Problema: Eventos não funcionam
- Verifique se o método existe no controlador
- Confirme se a anotação `@FXML` está presente
- Teste se o `fx:id` está correto

## 📚 Recursos Adicionais

### Documentação Oficial
- [JavaFX Documentation](https://openjfx.io/)
- [SceneBuilder Guide](https://docs.oracle.com/javase/8/scene-builder-2/get-started-tutorial/overview.htm)

### Tutoriais
- [JavaFX FXML Tutorial](https://docs.oracle.com/javase/8/javafx/fxml-tutorial/)
- [CSS Styling in JavaFX](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/css_tutorial.htm)

### Comunidade
- [JavaFX Community](https://openjfx.io/)
- [Stack Overflow - JavaFX](https://stackoverflow.com/questions/tagged/javafx)

---

**Dica**: Sempre faça backup antes de editar arquivos FXML importantes! 