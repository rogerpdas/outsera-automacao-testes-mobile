# language: pt
@formulario @regression
Funcionalidade: Formulário de Cadastro - Preenchimento e Envio de Dados Pessoais

  Contexto:
    Dado o usuário está autenticado com e-mail "standard_user" e senha "secret_sauce"
    E o usuário autenticado navega até o formulário de cadastro

  @smoke
  Cenário: Preenchimento completo com dados válidos e envio do pedido
    Quando o usuário preenche o formulário de cadastro com:
      | nome  | sobrenome | cep        |
      | João  | Silva     | 01310-100  |
    E o usuário confirma o formulário
    Então o resumo do pedido deve ser exibido
    Quando o usuário finaliza o pedido
    Então o pedido deve ser concluído com sucesso
    E a mensagem de agradecimento deve ser exibida

  @regression
  Cenário: Envio sem preencher o nome
    Quando o usuário preenche o formulário de cadastro com:
      | nome | sobrenome | cep       |
      |      | Silva     | 01310-100 |
    E o usuário confirma o formulário
    Então um erro de validação deve ser exibido
    E o erro deve informar "First Name is required"

  @regression
  Cenário: Envio sem preencher o sobrenome
    Quando o usuário preenche o formulário de cadastro com:
      | nome | sobrenome | cep       |
      | João |           | 01310-100 |
    E o usuário confirma o formulário
    Então um erro de validação deve ser exibido
    E o erro deve informar "Last Name is required"

  @regression
  Cenário: Envio sem preencher o CEP
    Quando o usuário preenche o formulário de cadastro com:
      | nome | sobrenome | cep |
      | João | Silva     |     |
    E o usuário confirma o formulário
    Então um erro de validação deve ser exibido
    E o erro deve informar "Postal Code is required"

  @regression
  Esquema do Cenário: Cadastro com múltiplos usuários
    Quando o usuário preenche o nome "<nome>"
    E o usuário preenche o sobrenome "<sobrenome>"
    E o usuário preenche o CEP "<cep>"
    E o usuário confirma o formulário
    Então o resumo do pedido deve ser exibido
    Quando o usuário finaliza o pedido
    Então o pedido deve ser concluído com sucesso

    Exemplos:
      | nome   | sobrenome | cep        |
      | Maria  | Oliveira  | 20040-020  |
      | Carlos | Mendes    | 30130-010  |
      | Ana    | Costa     | 40301-110  |
