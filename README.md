# Sistema GW POO

Projeto desenvolvido em Java para praticar fundamentos de programação orientada a objetos. O sistema funciona no terminal e permite cadastrar clientes, registrar produtos e selecionar os dados de uma nova entrega.

## Funcionalidades

- Cadastro de clientes com nome, CPF, e-mail e endereço.
- Registro de produtos com nome, descrição e preço.
- Seleção de cliente e produto para exibir um resumo de entrega.
- Repetição dos cadastros e do registro de entregas pelo menu.
- Validação de índices de cliente e produto.
- Encerramento do sistema pela opção `4`.

## Estrutura do projeto

```text
src/
├── app/
│   └── Main.java          # Ponto de entrada e menu principal
├── models/
│   ├── Cliente.java       # Dados e operações de clientes
│   ├── Endereco.java      # Dados e formatação de endereços
│   ├── Entrega.java       # Status, produtos e total da entrega
│   └── Produto.java       # Dados e operações de produtos
└── service/
	└── Sistema.java       # Cadastro e fluxo principal do sistema
```

## Como executar

Na raiz do projeto, compile os arquivos:

```bash
javac -d out src/models/*.java src/service/Sistema.java src/app/Main.java
```

Depois, execute a aplicação:

```bash
java -cp out src.app.Main
```

Também é possível abrir o projeto em uma IDE Java e executar a classe `src.app.Main`.

## Como usar

Ao iniciar, o menu oferece estas opções:

```text
1 - Cadastrar Cliente
2 - Registrar Produto
3 - Nova Entrega
4 - Sair
```

Para registrar uma entrega, cadastre pelo menos um cliente e um produto. O sistema apresenta as listas cadastradas, solicita os índices correspondentes e exibe o resumo com o cliente e o produto escolhidos.

## Conceitos praticados

- Classes, objetos, atributos e métodos.
- Encapsulamento com atributos privados, getters e setters.
- Associação entre `Cliente`, `Endereco`, `Produto` e `Entrega`.
- Coleções com `ArrayList` e `List`.
- Entrada de dados com `Scanner`.
- Estruturas condicionais e de repetição.