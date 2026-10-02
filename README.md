# Transportadora GW

Projeto Java desenvolvido para praticar programação orientada a objetos e modelagem de Banco de Dados com foco em cadastro de clientes, registro de produtos e gerenciamento de entregas.

## Funcionalidades

- Cadastro de clientes com nome, CPF, e-mail e endereço completo.
- Registro de produtos com nome, descrição e preço.
- Criação de entregas vinculando um cliente a um ou mais produtos.
- Exibição do resumo da entrega com rastreio, endereço, destinatário e total.
- Validação básica de dados de entrada, feitos em cada etapa.
- Menu interativo via terminal para repetir operações até o usuário encerrar o programa.
- Banco de dados via Postgres para modelagem

## Estrutura do projeto

```text
src/
├── app/
│   └── Main.java                 # Ponto de entrada da aplicação e menu principal
├── controllers/
│   ├── ClienteController.java     # Cadastro de Clientes
│   └── ProdutoController.java     # Cadastro de Produtos
└── db/
│   ├── ClienteDAO.java            # preparando para próxima fase*
│   ├── ConnectionService.java
│   ├── EntregaDAO.java            # *
│   ├── Schema.sql
├── models/
│   ├── Cliente.java               # Modelo do cliente
│   ├── Endereco.java              # Modelo do endereço
│   ├── Entrega.java               # Modelo da entrega e cálculo do total
│   └── Produto.java               # Modelo do produto
├── service/
│   └── Sistema.java               # Lógica principal do sistema
├── utils/
|   ├── CodRastreioGen.java        # Utilitário para geração de códigos de rastreio de encomendas
│   └── cpfUtil.java               # Utilitário para CPF
├── validation/
│   ├── ClienteValid.java          # Validação de cliente
│   ├── EntregaValid.java          # Validação de entrega
│   ├── ProdutoValid.java          # Validação de produto
│   └── Valid.java                 # Interface genérica de validação
└── db/
    └── 
```

## Como executar

Na raiz do projeto, compile todos os arquivos Java:

```bash
find src -name '*.java' -print0 | xargs -0 javac -d out
```

Depois, execute a aplicação:

```bash
java -cp out src.app.Main
```

Também é possível abrir o projeto em uma IDE Java e executar a classe `src.app.Main`.

## Como usar

Ao iniciar o programa, o menu principal oferece as opções:

```text
1 - Cadastrar Cliente
2 - Registrar Produto
3 - Nova Entrega
4 - Sair
```

Fluxo típico:

1. Cadastre pelo menos um cliente.
2. Cadastre pelo menos um produto.
3. Selecione o cliente e o produto para registrar a entrega.
4. O sistema mostra o resumo da entrega com o total calculado.
5. O menu volta para nova operação ou encerra ao escolher `4`.

## Observações importantes

- O projeto funciona em modo terminal.
- A entrada é feita via `Scanner` no console.
- A validação de CPF ainda precisa ser reforçada para uma validação real e completa.
- O sistema foi estruturado como prática de orientação a objetos e não como aplicação de produção.

## Conceitos praticados

- Classes, atributos e métodos.
- Encapsulamento com getters e setters.
- Associação entre objetos (`Cliente`, `Endereco`, `Produto`, `Entrega`).
- Coleções com `ArrayList` e `List`.
- Entrada e saída em console com `Scanner`.
- Estruturas condicionais e de repetição.
- Validação de dados em classes separadas.