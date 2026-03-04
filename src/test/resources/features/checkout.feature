# language: pt
@checkout @regression
Funcionalidade: Checkout - Preenchimento de Formulário e Envio de Pedido

  Contexto:
    Dado o usuário está autenticado com e-mail "standard_user" e senha "secret_sauce"
    E o usuário adicionou um produto ao carrinho
    E o usuário está na tela do carrinho

  @smoke
  Cenário: Checkout completo com dados válidos
    Quando o usuário toca no botão de checkout
    E o usuário preenche o formulário com os dados:
      | firstName | lastName | postalCode |
      | João      | Silva    |  01310-100 |
    E o usuário toca em continuar
    Então o usuário deve ver o resumo do pedido
    Quando o usuário confirma o pedido
    Então o pedido deve ser confirmado com sucesso
    E a mensagem de confirmação deve ser exibida

  @regression
  Cenário: Checkout com primeiro nome em branco
    Quando o usuário toca no botão de checkout
    E o usuário preenche o formulário com os dados:
      | firstName | lastName | postalCode |
      |           | Silva    |  01310-100 |
    E o usuário toca em continuar
    Então uma mensagem de erro de formulário deve ser exibida
    E a mensagem deve conter "First Name is required"

  @regression
  Cenário: Checkout com sobrenome em branco
    Quando o usuário toca no botão de checkout
    E o usuário preenche o formulário com os dados:
      | firstName | lastName | postalCode |
      | João      |          |  01310-100 |
    E o usuário toca em continuar
    Então uma mensagem de erro de formulário deve ser exibida
    E a mensagem deve conter "Last Name is required"

  @regression
  Cenário: Checkout com código postal em branco
    Quando o usuário toca no botão de checkout
    E o usuário preenche o formulário com os dados:
      | firstName | lastName | postalCode |
      | João      | Silva    |            |
    E o usuário toca em continuar
    Então uma mensagem de erro de formulário deve ser exibida
    E a mensagem deve conter "Postal Code is required"

  @regression
  Esquema do Cenário: Checkout com múltiplos usuários
    Quando o usuário toca no botão de checkout
    E o usuário insere o primeiro nome "<firstName>"
    E o usuário insere o sobrenome "<lastName>"
    E o usuário insere o código postal "<postalCode>"
    E o usuário toca em continuar
    Então o usuário deve ver o resumo do pedido
    Quando o usuário confirma o pedido
    Então o pedido deve ser confirmado com sucesso

    Exemplos:
      | firstName | lastName | postalCode |
      | Maria     | Oliveira |  20040-020 |
      | Carlos    | Mendes   |  30130-010 |
      | Ana       | Costa    |  40301-110 |
